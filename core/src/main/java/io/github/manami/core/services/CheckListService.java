package io.github.manami.core.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import io.github.manami.cache.Cache;
import io.github.manami.core.Manami;
import io.github.manami.core.config.CheckListConfig;
import io.github.manami.core.services.events.AbstractEvent.EventType;
import io.github.manami.core.services.events.*;
import io.github.manami.dto.AnimeType;
import io.github.manami.dto.entities.Anime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * @author manami project
 * @since 2.6.0
 */
public class CheckListService extends AbstractService<Void> {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CheckListService.class);
    private final Cache cache;
    private final List<Anime> list;
    private final CheckListConfig config;
    private final Path currentWorkingDir;
    private int currentProgress = 0;
    private int progressMax = 0;
    private final Manami app;


    /**
     * Constructor.
     *
     * @since 2.6.0
     * @param config
     *            Contains the configuration which features to check.
     * @param file
     *            Currently opened file.
     * @param cache
     *            Cache
     * @param observer
     *            Observer
     */
    public CheckListService(final CheckListConfig config, final Path file, final Cache cache, final Manami app, final Observer observer) {
        this.config = config;
        currentWorkingDir = file;
        this.cache = cache;
        this.app = app;
        list = app.fetchAnimeList();
        addObserver(observer);
    }


    @Override
    public void start() {
        Assert.notNull(list, "List of animes cannot be null");

        reset();
        countProgressMax();
        createAndStartService();
    }


    /**
     * @since 2.6.1
     */
    private void countProgressMax() {
        if (config.isCheckLocations()) {
            progressMax += list.size();
        }

        if (config.isCheckCrc()) {
            for (final Anime entry : list) {
                if (StringUtils.isNotBlank(entry.getLocation())) {
                    Path dir = Paths.get(entry.getLocation());
                    if (!Files.exists(dir) || !Files.isDirectory(dir)) { // absolute
                        dir = createRelativePath(dir);
                        if (!Files.exists(dir) || !Files.isDirectory(dir)) { // relative
                            continue;
                        }
                    }

                    long amount = 0L;
                    try {
                        amount = Files.list(dir).filter(p -> Files.isRegularFile(p)).count();
                    } catch (final IOException e) {
                        LOG.error("An error occurred detecting the amount of files for {}: ", entry.getTitle(), e);
                    }

                    progressMax += (int) amount;
                }
            }
        }

        if (config.isCheckMetaData()) {
            progressMax += list.size();
        }
    }


    private void createAndStartService() {
        service = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        if (config.isCheckLocations()) {
                            checkLocations();
                        }

                        if (config.isCheckCrc()) {
                            checkCrc();
                        }

                        if (config.isCheckMetaData()) {
                            checkMetaData();
                        }
                        return null;
                    }
                };
            }
        };

        service.setOnCancelled(getFailureEvent());
        service.setOnFailed(getFailureEvent());
        service.setOnSucceeded(getSuccessEvent());
        service.start();
    }


    /**
     * Checks every entry. A location must be set, exist and contain at least
     * one file.
     *
     * @since 2.6.0
     */
    private void checkLocations() {
        try {
            for (int index = 0; index < list.size() && !isInterrupt(); index++) {
                updateProgress();
                final Anime anime = list.get(index);
                LOG.debug("Checking location of {}", anime.getTitle());

                // 01 - Is location set?
                if (StringUtils.isBlank(anime.getLocation())) {
                    fireNoLocationEvent(anime);
                    continue;
                }

                // 02 - Does location exist?
                Path dir = Paths.get(anime.getLocation());

                if (!Files.exists(dir) || !Files.isDirectory(dir)) { // absolute
                    dir = createRelativePath(dir);
                    if (!Files.exists(dir) || !Files.isDirectory(dir)) { // relative
                        fireLocationNotFoundEvent(anime);
                        continue;
                    }
                }

                // 03 - Contains at least one file / the exact same amount files
                // as
                // episodes
                try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
                    int counter = 0;
                    for (final Path curPath : dirStream) {
                        if (Files.isRegularFile(curPath)) {
                            counter++;
                        }
                    }

                    if (counter == 0) {
                        fireLocationEmptyEvent(anime);
                    } else if (counter != anime.getEpisodes()) {
                        fireDifferentAmountOfEpisodesEvent(anime);
                    }
                } catch (final Exception e) {
                    LOG.error("An error occurred during file check: ", e);
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @since 2.6.1
     */
    private void updateProgress() {
        currentProgress++;
        setChanged();
        notifyObservers(new ProgressState(currentProgress, progressMax));
    }


    private void checkMetaData() {
        for (int index = 0; index < list.size() && !isInterrupt(); index++) {
            updateProgress();
            final Anime anime = list.get(index);

            if (StringUtils.isBlank(anime.getInfoLink())) {
                continue;
            }

            final Anime cachedEntry = cache.fetchAnime(anime.getInfoLink());

            if (cachedEntry == null) {
                continue;
            }

            if (!anime.getTitle().equals(cachedEntry.getTitle())) {
                fireTitleDiffersEvent(anime, cachedEntry.getTitle());
                continue;
            }

            if (anime.getEpisodes() != cachedEntry.getEpisodes()) {
                fireEpisodesDiffersEvent(anime, cachedEntry.getEpisodes());
                continue;
            }

            if (anime.getType() != cachedEntry.getType()) {
                fireTypeDiffersEvent(anime, cachedEntry.getType());
            }
        }
    }


    private void fireTypeDiffersEvent(final Anime element, final AnimeType newValue) {
        final TypeDifferEvent event = new TypeDifferEvent(element, newValue, app);
        event.setType(EventType.WARNING);
        event.setMessage(String.format("The local type is >>%s<< and the type from the info link is >>%s<<", element.getTypeAsString(), newValue));
        fire(event);
    }


    private void fireEpisodesDiffersEvent(final Anime element, final int newValue) {
        final EpisodesDifferEvent event = new EpisodesDifferEvent(element, newValue, app);
        event.setType(EventType.WARNING);
        event.setMessage(String.format("The local number of episodes is >>%s<< and the amount from the info link is >>%s<<", element.getEpisodes(), newValue));
        fire(event);
    }


    private void fireTitleDiffersEvent(final Anime element, final String newValue) {
        final TitleDifferEvent event = new TitleDifferEvent(element, newValue, app);
        event.setType(EventType.WARNING);
        event.setMessage(String.format("The local title is >>%s<< and title from the info link is >>%s<<", element.getTitle(), newValue));
        fire(event);
    }


    private void checkCrc() {
        for (int index = 0; index < list.size() && !isInterrupt(); index++) {
            final Anime anime = list.get(index);
            if (StringUtils.isBlank(anime.getLocation())) {
                continue;
            }

            LOG.debug("Checking CRC32 sum of {}", anime.getTitle());

            Path dir = Paths.get(anime.getLocation());

            // FIXME: in Refactoring for checklist: extract to method
            if (!Files.exists(dir) || !Files.isDirectory(dir)) { // absolute
                dir = createRelativePath(dir);
                if (!Files.exists(dir) || !Files.isDirectory(dir)) { // relative
                    continue;
                }
            }

            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
                for (final Path path : dirStream) {
                    updateProgress();
                    if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
                        continue;
                    }

                    final CRC32 crc = new CRC32();
                    final InputStream cis = new CheckedInputStream(Files.newInputStream(path), crc);
                    while (cis.read() != -1) {
                    }

                    final Pattern pattern = Pattern.compile("\\[.{8}\\]");
                    final Matcher matcher = pattern.matcher(path.getFileName().toString());
                    final String crcSum = Long.toHexString(crc.getValue());

                    if (matcher.find()) {
                        String titleCrc = matcher.group().replace("[", "").replace("]", "");

                        if (!titleCrc.equalsIgnoreCase(crcSum)) {
                            fireCrcSumsDifferEvent(path);
                        }
                    } else {
                        fireNoCrcSumEvent(path, crcSum);
                    }
                }
            } catch (final Exception e) {
                LOG.error("An error occurred during CRC sum check: ", e);
            }
        }
    }


    private void fireNoCrcSumEvent(final Path path, final String crcSum) {
        final CrcEvent event = new CrcEvent();
        event.setType(EventType.WARNING);
        event.setTitle(path.toAbsolutePath().toString());
        event.setMessage("File has no CRC sum.");
        event.setPath(path);
        event.setCrcSum(crcSum);
        fire(event);
    }


    private void fireCrcSumsDifferEvent(final Path path) {
        final CrcEvent event = new CrcEvent();
        event.setType(EventType.ERROR);
        event.setTitle(path.toAbsolutePath().toString());
        event.setMessage("CRC sums differ!");
        fire(event);
    }


    private void fireNoLocationEvent(final Anime anime) {
        final LocationEvent event = createErrorEvent(anime.getTitle());
        event.setMessage("Location is not set.");
        fire(event);
    }


    private void fireDifferentAmountOfEpisodesEvent(final Anime anime) {
        final LocationEvent event = new LocationEvent();
        event.setType(EventType.WARNING);
        event.setTitle(anime.getTitle());
        event.setMessage("Amount of files differs from amount of episodes.");
        fire(event);
    }


    private void fireLocationEmptyEvent(final Anime anime) {
        final LocationEvent event = createErrorEvent(anime.getTitle());
        event.setMessage("Location is empty.");
        fire(event);
    }


    private void fireLocationNotFoundEvent(final Anime anime) {
        final LocationEvent event = createErrorEvent(anime.getTitle());
        event.setMessage("Location does not exist.");
        fire(event);
    }


    private LocationEvent createErrorEvent(final String title) {
        final LocationEvent event = new LocationEvent();
        event.setType(EventType.ERROR);
        event.setTitle(title);
        return event;
    }


    private void fire(final Object event) {
        if (!isInterrupt()) {
            setChanged();
            notifyObservers(event);
        }
    }


    /**
     * Creates a relative path.
     *
     * @since 2.6.0
     * @param dir
     * @return
     */
    private Path createRelativePath(final Path dir) {
        Path ret = null;
        try {
            ret = currentWorkingDir.getParent().resolve(dir);
        } catch (final Exception e) {
            LOG.error("An error occurred trying to create a relative Path: ", e);
        }

        return ret;
    }
}
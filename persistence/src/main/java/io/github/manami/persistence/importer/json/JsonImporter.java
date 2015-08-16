package io.github.manami.persistence.importer.json;

import com.google.common.collect.Lists;
import io.github.manami.dto.AnimeType;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterEntry;
import io.github.manami.dto.entities.WatchListEntry;
import io.github.manami.persistence.PersistenceFacade;
import io.github.manami.persistence.importer.Importer;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Imports a list from a valid JSON file.
 *
 * @author manami project
 * @since 2.0.0
 */
public class JsonImporter implements Importer {

    /** Logger */
    private final static Logger LOG = LoggerFactory.getLogger(JsonImporter.class);
    private final PersistenceFacade persistence;
    private final List<Anime> animeListEntries;
    private final List<FilterEntry> filterListEntries;
    private final List<WatchListEntry> watchListEntries;


    public JsonImporter(final PersistenceFacade persistence) {
        this.persistence = persistence;
        animeListEntries = Lists.newArrayList();
        filterListEntries = Lists.newArrayList();
        watchListEntries = Lists.newArrayList();
    }


    @Override
    public void importFile(final Path file) {
        try {
            final FileReader fileReader = new FileReader(file.toFile());
            final BufferedReader br = new BufferedReader(fileReader);
            final StringBuilder strBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                strBuilder.append(line).append('\n');
            }

            final JSONTokener tokener = new JSONTokener(strBuilder.toString());
            final JSONArray jsonArr = new JSONArray(tokener);
            extractAnimeList(jsonArr);
            extractWatchList(jsonArr);
            extractFilterList(jsonArr);

            br.close();
        } catch (final IOException e) {
            LOG.error("An error occurred while trying to import JSON file: ", e);
        }
    }


    /**
     * @since 2.7.0
     * @param jsonArr
     */
    private void extractAnimeList(final JSONArray jsonArr) {
        final JSONArray animeListArr = jsonArr.getJSONArray(0);

        for (int i = 0; i < animeListArr.length(); i++) {
            final String title = animeListArr.getJSONObject(i).getString("title").trim();
            final AnimeType type = AnimeType.findByName(animeListArr.getJSONObject(i).getString("type").trim());
            final Integer episodes = animeListArr.getJSONObject(i).getInt("episodes");
            final String infoLink = animeListArr.getJSONObject(i).getString("infoLink").trim();
            final String location = animeListArr.getJSONObject(i).getString("location").trim();

            if (StringUtils.isNotBlank(title) && type != null && episodes != null && StringUtils.isNotBlank(infoLink) && StringUtils.isNotBlank(location)) {
                final Anime curAnime = new Anime(title, type, episodes, infoLink, location);
                animeListEntries.add(curAnime);
            } else {
                LOG.debug("Could not import '{}', because the type is unknown.", title);
            }
        }
        persistence.addAnimeList(animeListEntries);
    }


    /**
     * @since 2.7.0
     * @param jsonArr
     */
    private void extractWatchList(final JSONArray jsonArr) {
        final JSONArray animeListArr = jsonArr.getJSONArray(1);

        for (int i = 0; i < animeListArr.length(); i++) {
            final String thumbnail = animeListArr.getJSONObject(i).getString("thumbnail").trim();
            final String title = animeListArr.getJSONObject(i).getString("title").trim();
            final String infoLink = animeListArr.getJSONObject(i).getString("infoLink").trim();

            if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(infoLink)) {
                watchListEntries.add(new WatchListEntry(title, thumbnail, infoLink));
            } else {
                LOG.debug("Could not import '{}', because the type is unknown.", title);
            }
        }
        persistence.addWatchList(watchListEntries);
    }


    /**
     * @since 2.7.0
     * @param jsonArr
     */
    private void extractFilterList(final JSONArray jsonArr) {
        final JSONArray animeListArr = jsonArr.getJSONArray(2);

        for (int i = 0; i < animeListArr.length(); i++) {
            final String thumbnail = animeListArr.getJSONObject(i).getString("thumbnail").trim();
            final String title = animeListArr.getJSONObject(i).getString("title").trim();
            final String infoLink = animeListArr.getJSONObject(i).getString("infoLink").trim();

            if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(infoLink)) {
                filterListEntries.add(new FilterEntry(title, thumbnail, infoLink));
            } else {
                LOG.debug("Could not import '{}', because the type is unknown.", title);
            }
        }
        persistence.addFilterList(filterListEntries);
    }
}
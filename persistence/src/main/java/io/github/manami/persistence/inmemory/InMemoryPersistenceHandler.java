package io.github.manami.persistence.inmemory;

import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterEntry;
import io.github.manami.dto.entities.MinimalEntry;
import io.github.manami.dto.entities.WatchListEntry;
import io.github.manami.persistence.PersistenceHandler;
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler;
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler;
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.UUID;

/**
 * @author manami project
 * @since 2.7.0
 */
@Named("inMemoryStrategy")
public class InMemoryPersistenceHandler implements PersistenceHandler {

    private final InMemoryFilterListHandler filterListHandler;
    private final InMemoryAnimeListHandler animeListHandler;
    private final InMemoryWatchListHandler watchListHandler;


    /**
     * @since 2.7.0
     * @param animeListHandler
     * @param filterListHandler
     * @param watchListHandler
     */
    @Inject
    public InMemoryPersistenceHandler(final InMemoryAnimeListHandler animeListHandler, final InMemoryFilterListHandler filterListHandler, final InMemoryWatchListHandler watchListHandler) {
        this.animeListHandler = animeListHandler;
        this.filterListHandler = filterListHandler;
        this.watchListHandler = watchListHandler;
    }


    @Override
    public boolean filterAnime(final MinimalEntry anime) {
        watchListHandler.removeFromWatchList(anime.getInfoLink());
        return filterListHandler.filterAnime(anime);
    }


    @Override
    public List<FilterEntry> fetchFilterList() {
        return filterListHandler.fetchFilterList();
    }


    @Override
    public boolean filterEntryExists(final String url) {
        return filterListHandler.filterEntryExists(url);
    }


    @Override
    public boolean removeFromFilterList(final String url) {
        return filterListHandler.removeFromFilterList(url);
    }


    @Override
    public boolean addAnime(final Anime anime) {
        filterListHandler.removeFromFilterList(anime.getInfoLink());
        watchListHandler.removeFromWatchList(anime.getInfoLink());
        return animeListHandler.addAnime(anime);
    }


    @Override
    public List<WatchListEntry> fetchWatchList() {
        return watchListHandler.fetchWatchList();
    }


    @Override
    public boolean watchListEntryExists(final String url) {
        return watchListHandler.watchListEntryExists(url);
    }


    @Override
    public boolean watchAnime(final MinimalEntry anime) {
        filterListHandler.removeFromFilterList(anime.getInfoLink());
        return watchListHandler.watchAnime(anime);
    }


    @Override
    public boolean removeFromWatchList(final String url) {
        return watchListHandler.removeFromWatchList(url);
    }


    @Override
    public List<Anime> fetchAnimeList() {
        return animeListHandler.fetchAnimeList();
    }


    @Override
    public boolean animeEntryExists(final String url) {
        return animeListHandler.animeEntryExists(url);
    }


    @Override
    public boolean removeAnime(final UUID id) {
        return animeListHandler.removeAnime(id);
    }


    @Override
    public void clearAll() {
        animeListHandler.clear();
        watchListHandler.clear();
        filterListHandler.clear();
    }


    @Override
    public void addAnimeList(final List<Anime> list) {
        list.forEach(animeListHandler::addAnime);
    }


    @Override
    public void addFilterList(final List<? extends MinimalEntry> list) {
        list.forEach(filterListHandler::filterAnime);
    }


    @Override
    public void addWatchList(final List<? extends MinimalEntry> list) {
        list.forEach(watchListHandler::watchAnime);
    }
}
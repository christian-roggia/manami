package io.github.manami.persistence.inmemory

import io.github.manami.dto.entities.*
import io.github.manami.persistence.*
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler


internal class InMemoryPersistenceHandler(
        private val animeListHandler: InMemoryAnimeListHandler,
        private val filterListHandler: InMemoryFilterListHandler,
        private val watchListHandler: InMemoryWatchListHandler
) : ApplicationPersistence, InternalPersistenceHandler {


    override fun filterAnime(anime: MinimalEntry): Boolean {
        return filterListHandler.filterAnime(anime)
    }


    override fun fetchFilterList(): MutableList<FilterListEntry> {
        return filterListHandler.fetchFilterList()
    }


    override fun filterListEntryExists(infoLink: InfoLink): Boolean {
        return filterListHandler.filterListEntryExists(infoLink)
    }


    override fun removeFromFilterList(anime: MinimalEntry): Boolean {
        return filterListHandler.removeFromFilterList(anime)
    }


    override fun addAnime(anime: Anime): Boolean {
        return animeListHandler.addAnime(anime)
    }


    override fun fetchAnimeList(): MutableList<Anime> {
        return animeListHandler.fetchAnimeList()
    }


    override fun animeEntryExists(infoLink: InfoLink): Boolean {
        return animeListHandler.animeEntryExists(infoLink)
    }


    override fun removeAnime(anime: Anime): Boolean {
        return animeListHandler.removeAnime(anime)
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        return watchListHandler.watchAnime(anime)
    }


    override fun fetchWatchList(): MutableList<WatchListEntry> {
        return watchListHandler.fetchWatchList()
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return watchListHandler.watchListEntryExists(infoLink)
    }


    override fun removeFromWatchList(anime: MinimalEntry): Boolean {
        return watchListHandler.removeFromWatchList(anime)
    }


    override fun clearAll() {
        animeListHandler.clear()
        watchListHandler.clear()
        filterListHandler.clear()
    }


    override fun addAnimeList(list: MutableList<Anime>) {
        list.forEach { anime -> animeListHandler.addAnime(anime) }
    }


    override fun addFilterList(list: MutableList<FilterListEntry>) {
        list.forEach { anime -> filterListHandler.filterAnime(anime) }
    }


    override fun addWatchList(list: MutableList<WatchListEntry>) {
        list.forEach { anime -> watchListHandler.watchAnime(anime) }
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        when (entry) {
            is Anime -> animeListHandler.updateOrCreate(entry)
            is FilterListEntry -> filterListHandler.updateOrCreate(entry)
            is WatchListEntry -> watchListHandler.updateOrCreate(entry)
        }
    }
}

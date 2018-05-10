package io.github.manamiproject.manami.persistence.inmemory.filterlist

import io.github.manamiproject.manami.entities.*
import io.github.manamiproject.manami.entities.comparator.MinimalEntryCompByTitleAsc
import io.github.manamiproject.manami.persistence.FilterList
import java.util.concurrent.ConcurrentHashMap


internal class InMemoryFilterList : FilterList {

    private val filterList: MutableMap<InfoLink, FilterListEntry> = ConcurrentHashMap()

    override fun filterAnime(anime: MinimalEntry): Boolean {
        if (!anime.isValid() || filterList.containsKey(anime.infoLink)) {
            return false
        }

        val entry = when (anime) {
            is Anime, is WatchListEntry -> FilterListEntry.valueOf(anime)
            is FilterListEntry -> anime
            else -> return false
        }

        filterList[entry.infoLink] = entry

        return true
    }


    override fun fetchFilterList(): List<FilterListEntry> {
        return filterList.values.sortedWith(MinimalEntryCompByTitleAsc).toList()
    }


    override fun filterListEntryExists(infoLink: InfoLink): Boolean {
        return filterList.containsKey(infoLink)
    }


    override fun removeFromFilterList(anime: MinimalEntry): Boolean {
        if (anime.isValid()) {
            return filterList.remove(anime.infoLink) != null
        }

        return false
    }

    fun clear() {
        filterList.clear()
    }


    fun updateOrCreate(entry: FilterListEntry) {
        if (entry.isValid()) {
            filterList[entry.infoLink] = entry
        }
    }
}

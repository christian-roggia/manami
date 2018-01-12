package io.github.manami.dto.entities

import java.net.URL

data class WatchListEntry(
        override var title: String,
        override var infoLink: InfoLink,
        override var thumbnail: URL = MinimalEntry.NO_IMG_THUMB
) : MinimalEntry {

    companion object {
        fun valueOf(anime: MinimalEntry) = WatchListEntry(anime.title, anime.infoLink, anime.thumbnail)
    }
}

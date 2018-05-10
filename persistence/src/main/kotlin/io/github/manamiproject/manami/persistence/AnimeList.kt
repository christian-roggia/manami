package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink


interface AnimeList {

    /**
     * Adds an {@link Anime} if it is not already in the list.
     *
     * @param anime Anime to add to the list of watched anime.
     * @return true if the anime was added.
     */
    fun addAnime(anime: Anime): Boolean


    /**
     * @return A {@link List} of {@link Anime}s which have been watched.
     */
    fun fetchAnimeList(): List<Anime>


    /**
     * @param infoLink URL of the anime's infolink.
     * @return true if an entry with this URL as infolink already exists.
     */
    fun animeEntryExists(infoLink: InfoLink): Boolean


    /**
     * @param anime
     * @return true if an entry was removed.
     */
    fun removeAnime(anime: Anime): Boolean
}

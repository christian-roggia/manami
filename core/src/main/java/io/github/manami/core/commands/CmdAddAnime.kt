package io.github.manami.core.commands

import io.github.manami.dto.entities.Anime
import io.github.manami.persistence.Persistence

/**
 * Command for adding an entry.
 *
 * @param anime Anime that is being added.
 * @param persistence
 */
internal class CmdAddAnime(
        private val anime: Anime,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return persistence.addAnime(anime)
    }


    override fun undo() {
        persistence.removeAnime(anime)
    }
}
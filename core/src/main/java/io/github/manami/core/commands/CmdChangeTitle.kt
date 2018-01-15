package io.github.manami.core.commands

import io.github.manami.dto.entities.Anime
import io.github.manami.persistence.Persistence


/**
 * Command for changing the title.
 *
 * @param anime Anime that is being edited.
 * @param newValue The new title.
 * @param persistence
 */
internal class CmdChangeTitle(
        private val anime: Anime,
        private val newValue: String,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    init {
        oldAnime = anime
        newAnime = oldAnime?.copy(title = newValue)
    }
}

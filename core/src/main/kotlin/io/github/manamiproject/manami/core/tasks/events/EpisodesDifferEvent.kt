package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.commands.CmdChangeEpisodes
import io.github.manamiproject.manami.core.commands.ReversibleCommand
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence

class EpisodesDifferEvent(
        private val animeEntry: Anime,
        private val newValue: Int,
        private val persistence: Persistence
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val cmd: CmdChangeEpisodes = CmdChangeEpisodes(animeEntry, newValue, persistence)


    override fun getCommand(): ReversibleCommand {
        return cmd
    }
}

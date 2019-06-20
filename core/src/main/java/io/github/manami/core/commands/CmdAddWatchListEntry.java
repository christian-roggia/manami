package io.github.manami.core.commands;

import io.github.manami.core.Manami;
import io.github.manami.dto.entities.WatchListEntry;

public class CmdAddWatchListEntry extends AbstractReversibleCommand {

    private final Manami app;
    private final WatchListEntry entry;


    /**
     * @param entry Anime that is being added.
     * @param application Instance of the application which reveals access to the persistence functionality.
     */
    public CmdAddWatchListEntry(final WatchListEntry entry, final Manami application) {
        this.entry = entry;
        app = application;
    }


    @Override
    public boolean execute() {
        return app.watchAnime(entry);
    }


    @Override
    public void undo() {
        app.removeFromWatchList(entry.getInfoLink());
    }
}

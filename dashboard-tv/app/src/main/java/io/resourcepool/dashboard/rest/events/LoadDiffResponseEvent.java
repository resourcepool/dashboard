package io.resourcepool.dashboard.rest.events;

import io.resourcepool.dashboard.rest.dtos.ChangesetWrapper;

/**
 * Created by loicortola on 04/03/2017.
 */

public class LoadDiffResponseEvent {
    private final ChangesetWrapper changeset;

    public LoadDiffResponseEvent(ChangesetWrapper changeset) {
        this.changeset = changeset;
    }

    public ChangesetWrapper getChangeset() {
        return changeset;
    }
}

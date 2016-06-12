package com.excilys.shoofleurs.dashboard.rest.events;

/**
 * Created by tommy on 12/06/16.
 */
public class GetRevisionResponseEvent {
    private long mRevision;

    public GetRevisionResponseEvent(long revision) {
        mRevision = revision;
    }

    public long getRevision() {
        return mRevision;
    }
}


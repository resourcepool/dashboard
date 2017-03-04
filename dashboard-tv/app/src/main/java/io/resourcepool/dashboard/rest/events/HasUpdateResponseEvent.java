package io.resourcepool.dashboard.rest.events;

/**
 * Created by loicortola on 04/03/2017.
 */

public class HasUpdateResponseEvent {
    private final Long currentRevision;
    private final Boolean hasUpdate;

    public HasUpdateResponseEvent(Long currentRevision, Boolean hasUpdate) {
        this.currentRevision = currentRevision;
        this.hasUpdate = hasUpdate;
    }

    public Long getCurrentRevision() {
        return currentRevision;
    }

    public Boolean getHasUpdate() {
        return hasUpdate;
    }
}

package io.resourcepool.dashboard.rest.dtos;

import java.util.List;

/**
 * Created by loicortola on 04/03/2017.
 */
public class ChangesetWrapper {
    private FeedDto feed;
    private List<Revision> changes;

    public ChangesetWrapper() {
    }

    public ChangesetWrapper(FeedDto feed, List<Revision> changes) {
        this.feed = feed;
        this.changes = changes;
    }

    public FeedDto getFeed() {
        return feed;
    }

    public void setFeed(FeedDto feed) {
        this.feed = feed;
    }

    public List<Revision> getChanges() {
        return changes;
    }

    public void setChanges(List<Revision> changes) {
        this.changes = changes;
    }
}

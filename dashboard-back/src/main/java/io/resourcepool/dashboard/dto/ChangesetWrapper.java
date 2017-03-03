package io.resourcepool.dashboard.dto;

import io.resourcepool.dashboard.model.Revision;

import java.util.List;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 03/03/2017
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

package io.resourcepool.dashboard.service;

import io.resourcepool.dashboard.model.metadata.Feed;

import java.util.List;

public interface FeedService {

  List<Feed> getAll();

  /**
   * Create a feed into DB.
   *
   * @param feed the feed
   */
  void save(Feed feed);

  /**
   * Update a feed into DB.
   *
   * @param feed the feed to update
   * @return the new Feed or null if failed
   */
  void update(Feed feed);

  /**
   * Delete feed from DB.
   *
   * @param uuid The feed uuid
   */
  void delete(String uuid);

  /**
   * Get feed from DB.
   *
   * @param uuid the feed uuid
   * @return the new feed or null if none
   */
  Feed get(String uuid);

  /**
   * Replace old tag by new bundle tag inside all relevant feeds.
   * @param oldTag the old tag
   * @param newTag the new tag
   * @return
   */
  void replaceTag(String oldTag, String newTag);

  /**
   * Remove all references to a bundle tag.
   * @param tag the bundle tag
   */
  void deleteBundleTag(String tag);
}

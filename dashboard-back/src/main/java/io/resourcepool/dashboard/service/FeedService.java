package io.resourcepool.dashboard.service;

import io.resourcepool.dashboard.model.metadata.FeedMetaData;

import java.util.List;

public interface FeedService {

  List<FeedMetaData> getAll();

  /**
   * Create a feed into DB.
   *
   * @param feedMetaData the feed
   */
  void save(FeedMetaData feedMetaData);

  /**
   * Update a feed into DB.
   *
   * @param feedMetaData the feed to update
   * @return the new Feed or null if failed
   */
  void update(FeedMetaData feedMetaData);

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
  FeedMetaData get(String uuid);

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

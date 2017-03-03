package io.resourcepool.dashboard.dao;


import io.resourcepool.dashboard.model.metadata.Feed;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface FeedDao {

  String ENTITY_NAME = "feed";

  /**
   * Retrieve feed from DB using its id.
   *
   * @param uuid the feed uuid
   * @return the retrieved feed or null if no match
   */
  Feed get(String uuid);

  /**
   * Retrieve all feeds from DB.
   *
   * @return the retrieved feeds or empty
   */
  List<Feed> getAll();

  /**
   * Save a feed into DB.
   *
   * @param feed the feed meta data
   */
  Feed save(Feed feed);

  /**
   * Delete feed from DB.
   *
   * @param uuid The feed tag
   */
  Feed delete(String uuid);


  /**
   * Retrieve feeds containing bundle tag.
   *
   * @param tag the bundle tag
   * @return the list of feeds or null if none
   */
  List<Feed> getByBundleTag(String tag);
}
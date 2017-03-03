package com.excilys.shooflers.dashboard.server.dao;


import com.excilys.shooflers.dashboard.server.model.metadata.FeedMetaData;

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
  FeedMetaData get(String uuid);

  /**
   * Retrieve all feeds from DB.
   *
   * @return the retrieved feeds or empty
   */
  List<FeedMetaData> getAll();

  /**
   * Save a feed into DB.
   *
   * @param feedMetaData the feed meta data
   */
  FeedMetaData save(FeedMetaData feedMetaData);

  /**
   * Delete feed from DB.
   *
   * @param uuid The feed tag
   */
  FeedMetaData delete(String uuid);


  /**
   * Retrieve feeds containing bundle tag.
   *
   * @param tag the bundle tag
   * @return the list of feeds or null if none
   */
  List<FeedMetaData> getByBundleTag(String tag);
}
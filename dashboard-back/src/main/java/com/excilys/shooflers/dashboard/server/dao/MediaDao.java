package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface MediaDao {

    String ENTITY_NAME = "media";

    /**
     * Retrieve media from DB.
     *
     * @param uuid the media uuid
     * @return the retrieved media or null if no match
     */
    MediaMetadata get(String uuid);

    /**
     * Retrieve all medias from DB.
     *
     * @return the retrieved medias or empty
     */
    List<MediaMetadata> getAll();

    /**
     * Save a media into DB.
     *
     * @param media the media meta data
     */
    void save(MediaMetadata media);

    /**
     * Delete a media from DB.
     *
     * @param uuid The bundle uuid
     */
    boolean delete(String uuid);
}

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
     * Retrieve all medias matching a bundle from DB.
     * @param bundleUuid the bundle Uuid
     * @return null if bundle doesn't exist, empty if bundle is empty, and entries if non-empty
     */
    List<MediaMetadata> getByBundle(String bundleUuid);

    /**
     * Save a media into DB.
     *
     * @param media the media meta data
     */
    MediaMetadata save(MediaMetadata media);

    /**
     * Delete a media from DB.
     *
     * @param uuid The media uuid
     */
    boolean delete(String uuid);
}

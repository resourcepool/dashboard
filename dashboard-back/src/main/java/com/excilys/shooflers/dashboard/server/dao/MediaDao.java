package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface MediaDao {

    String ENTITY_NAME = "media";

    /**
     * Retrieve media from DB.
     *
     * @param uuid the media tag
     * @return the retrieved media or null if no match
     */
    MediaMetadata get(String uuid);

    /**
     * Retrieve content of a media from DB.
     *
     * @param filename the resource filename
     * @return the retrieved media content or null if no match
     */
    Path getContent(String filename);

    /**
     * Retrieve all medias from DB.
     *
     * @return the retrieved medias or empty
     */
    List<MediaMetadata> getAll();

    /**
     * Retrieve all medias matching a bundle from DB.
     *
     * @param bundleUuid the bundle Uuid
     * @return null if bundle doesn't exist, empty if bundle is empty, and entries if non-empty
     */
    List<MediaMetadata> getByBundle(String bundleUuid);

    /**
     * Save a media into DB.
     *
     * @param media the media
     */
    void save(Media media);

    void deleteByBundle(String bundleTag);

    /**
     * Delete a media from DB.
     *
     * @param uuid The media tag
     */
    void delete(String uuid);
}

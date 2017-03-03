package io.resourcepool.dashboard.service;


import io.resourcepool.dashboard.model.Media;
import io.resourcepool.dashboard.model.metadata.MediaMetadata;

import java.nio.file.Path;
import java.util.List;

public interface MediaService {

    /**
     * Persist a new media.
     *
     * @param media the media to persist
     */
    void save(Media media);

    /**
     * Update an existing media.
     *
     * @param media the media to persist
     */
    void update(Media media);

    void deleteByBundleTag(String bundleTag);

    /**
     * Delete a media by tag.
     *
     * @param uuid tag to delete
     */
    void delete(String uuid);

    /**
     * Get a media content file.
     *
     * @param filename the content filename
     * @return the media content file or null if none
     */
    Path getContent(String filename);

    /**
     * Get a media by tag.
     *
     * @param uuid tag to find
     * @return media found or null
     */
    MediaMetadata get(String uuid);

    /**
     * Get all media by bundle.
     *
     * @param bundleTag tag bundle
     * @return list of media
     */
    List<MediaMetadata> getByBundleTag(String bundleTag);

    /**
     * Get all media.
     *
     * @return list of media
     */
    List<MediaMetadata> getAll();
}

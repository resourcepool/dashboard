package com.excilys.shooflers.dashboard.server.service;


import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;

import java.util.List;

public interface MediaService {

    /**
     * Persist a media.
     * @param media the media to persist
     * @return media persisted
     */
    MediaMetadata save(Media media);

    /**
     * Delete a media by uuid.
     * @param uuid uuid to delete
     */
    void delete(String uuid);

    /**
     * Get a media by uuid.
     * @param uuid uuid to find
     * @return media found or null
     */
    MediaMetadata get(String uuid);

    /**
     * Get all media by bundle.
     * @param uuidBundle uuid bundle
     * @return list of media
     */
    List<MediaMetadata> getByBundle(String uuidBundle);

    /**
     * Get all media.
     * @return list of media
     */
    List<MediaMetadata> getAll();
}

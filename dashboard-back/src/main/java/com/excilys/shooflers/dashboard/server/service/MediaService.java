package com.excilys.shooflers.dashboard.server.service;


import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;

import java.util.List;

public interface MediaService {

    /**
     * Convert Json to MediaDataDto.
     *
     * @param json json to convert
     * @return media metadata dto
     */
    MediaMetadataDto fromJson(String json);

    /**
     * Persist a media.
     * @param mediaMetadataDto media to persist
     * @return media persisted
     */
    MediaMetadataDto save(MediaMetadataDto mediaMetadataDto);

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
    MediaMetadataDto get(String uuid);

    /**
     * Get all media by bundle.
     * @param uuidBundle uuid bundle
     * @return list of media
     */
    List<MediaMetadataDto> getByBundle(String uuidBundle);

    /**
     * Get all media.
     * @return list of media
     */
    List<MediaMetadataDto> getAll();
}

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
     * @param uuidBundle uuid bundle of the media
     * @return result of the operation
     */
    boolean delete(String uuid, String uuidBundle);

    /**
     * Get a media by uuid.
     * @param uuid uuid to find
     * @param uuidBundle uuid bundle of the media
     * @return media found or null
     */
    MediaMetadataDto get(String uuid, String uuidBundle);

    /**
     * Get all media by bundle.
     * @param uuidBundle uuid bundle
     * @return list of media
     */
    List<MediaMetadataDto> getByBundle(String uuidBundle);
}

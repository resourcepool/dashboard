package com.excilys.shooflers.dashboard.server.dto.mapper;

import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;

/**
 * Created by Mickael on 09/06/2016.
 */
public interface MediaDtoMapper extends MapperDto<MediaMetadata, MediaMetadataDto> {
    MediaMetadataDto toDto(String json);
}

package io.resourcepool.dashboard.dto.mapper;

import io.resourcepool.dashboard.dto.MediaMetadataDto;
import io.resourcepool.dashboard.model.metadata.MediaMetadata;

/**
 * Created by Mickael on 09/06/2016.
 */
public interface MediaDtoMapper extends MapperDto<MediaMetadata, MediaMetadataDto> {
    MediaMetadataDto toDto(String json);
}

package com.excilys.shooflers.dashboard.server.dto.mapper;

import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaDtoMapperImpl implements MapperDto<MediaMetadata, MediaMetadataDto> {

    @Autowired
    private ValidityDtoMapperImpl validityDtoMapper;

    @Override
    public MediaMetadataDto toDto(MediaMetadata model) {
        return new MediaMetadataDto.Builder()
                .uuid(model.getUuid())
                .name(model.getName())
                .duration(model.getDuration())
                .mediaType(model.getMediaType())
                .validity(validityDtoMapper.toDto(model.getValidity()))
                .url(model.getUrl())
                .uuidBundle(model.getBundleTag())
                .build();
    }

    @Override
    public MediaMetadata fromDto(MediaMetadataDto dto) {
        return new MediaMetadata.Builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .duration(dto.getDuration())
                .mediaType(MediaType.getMediaType(dto.getMediaType()))
                .validity(validityDtoMapper.fromDto(dto.getValidity()))
                .url(dto.getUrl())
                .bundleTag(dto.getUuidBundle())
                .build();
    }
}

package com.excilys.shooflers.dashboard.server.dto.mapper;

import com.excilys.shooflers.dashboard.server.dto.SlideshowMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.model.metadata.SlideshowMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlideshowDtoMapperImpl implements MapperDto<SlideshowMetadata, SlideshowMetadataDto> {

    @Autowired
    private ValidityDtoMapperImpl validityDtoMapper;

    @Autowired
    private MediaDtoMapperImpl mediaDtoMapper;

    @Override
    public SlideshowMetadataDto toDto(SlideshowMetadata model) {
        return new SlideshowMetadataDto.Builder()
                .uuid(model.getUuid())
                .name(model.getName())
                .validity(validityDtoMapper.toDto(model.getValidity()))
                .medias(mediaDtoMapper.toListDto(model.getMedias()))
                .build();
    }

    @Override
    public SlideshowMetadata fromDto(SlideshowMetadataDto dto) {
        return new SlideshowMetadata.Builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .validity(validityDtoMapper.fromDto(dto.getValidityDto()))
                .medias(mediaDtoMapper.fromListDto(dto.getMedias()))
                .build();
    }
}

package com.excilys.shooflers.dashboard.server.dto.mapper;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlideshowDtoMapperImpl implements MapperDto<BundleMetadata, BundleMetadataDto> {

    @Autowired
    private BundleDtoMapperImpl validityDtoMapper;

    @Autowired
    private MediaDtoMapperImpl mediaDtoMapper;

    @Override
    public BundleMetadataDto toDto(BundleMetadata model) {
        return new BundleMetadataDto.Builder()
                .uuid(model.getUuid())
                .name(model.getName())
                .validity(validityDtoMapper.toDto(model.getValidity()))
                .build();
    }

    @Override
    public BundleMetadata fromDto(BundleMetadataDto dto) {
        return new BundleMetadata.Builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .validity(validityDtoMapper.fromDto(dto.getValidity()))
                .build();
    }
}

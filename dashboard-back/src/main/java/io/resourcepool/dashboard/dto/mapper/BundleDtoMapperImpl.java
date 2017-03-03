package io.resourcepool.dashboard.dto.mapper;

import io.resourcepool.dashboard.dto.BundleMetadataDto;
import io.resourcepool.dashboard.model.metadata.BundleMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BundleDtoMapperImpl implements BundleDtoMapper {

    @Autowired
    private ValidityDtoMapperImpl validityDtoMapper;

    @Override
    public BundleMetadataDto toDto(BundleMetadata model) {
        return model != null ? new BundleMetadataDto.Builder()
                .uuid(model.getUuid())
                .tag(model.getTag())
                .name(model.getName())
                .validity(validityDtoMapper.toDto(model.getValidity()))
                .build() : null;
    }

    @Override
    public BundleMetadata fromDto(BundleMetadataDto dto) {
        return dto != null ? new BundleMetadata.Builder()
                .uuid(dto.getUuid())
                .tag(dto.getTag())
                .name(dto.getName())
                .validity(validityDtoMapper.fromDto(dto.getValidity()))
                .build() : null;
    }
}
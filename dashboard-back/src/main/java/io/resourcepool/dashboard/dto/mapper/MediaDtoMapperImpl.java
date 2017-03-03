package io.resourcepool.dashboard.dto.mapper;

import io.resourcepool.dashboard.dto.MediaMetadataDto;
import io.resourcepool.dashboard.model.metadata.MediaMetadata;
import io.resourcepool.dashboard.model.type.MediaType;
import io.resourcepool.dashboard.service.exception.JsonMalformedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MediaDtoMapperImpl implements MediaDtoMapper {

    @Autowired
    private ValidityDtoMapperImpl validityDtoMapper;

    @Override
    public MediaMetadataDto toDto(MediaMetadata model) {
        return model != null ? new MediaMetadataDto.Builder()
                .uuid(model.getUuid())
                .name(model.getName())
                .duration(model.getDuration())
                .mediaType(model.getMediaType())
                .validity(validityDtoMapper.toDto(model.getValidity()))
                .url(model.getUrl())
                .content(model.getContent())
                .bundleTag(model.getBundleTag())
                .build() : null;
    }

    @Override
    public MediaMetadata fromDto(MediaMetadataDto dto) {
        return dto != null ? MediaMetadata.builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .duration(dto.getDuration())
                .mediaType(MediaType.valueOf(dto.getMediaType()))
                .validity(validityDtoMapper.fromDto(dto.getValidity()))
                .url(dto.getUrl())
                .content(dto.getContent())
                .bundleTag(dto.getBundleTag())
                .build() : null;
    }

    @Override
    public MediaMetadataDto toDto(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        MediaMetadataDto mediaMetadataDto;
        try {
            mediaMetadataDto = objectMapper.readValue(json, MediaMetadataDto.class);
        } catch (IOException e) {
            throw new JsonMalformedException("JSON malformed");
        }
        return mediaMetadataDto;
    }

}

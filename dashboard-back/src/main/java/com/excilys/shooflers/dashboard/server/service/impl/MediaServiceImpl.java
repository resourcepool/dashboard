package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.MediaDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.excilys.shooflers.dashboard.server.service.exception.JsonMalformedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private MediaDtoMapperImpl mapper;

    @Autowired
    private DashboardProperties props;

    /**
     * Map a json to a media data dto
     * @param json Json to map
     * @return media metadata dto
     */
    @Override
    public MediaMetadataDto fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        MediaMetadataDto mediaMetadataDto;
        try {
            mediaMetadataDto = objectMapper.readValue(json, MediaMetadataDto.class);
        } catch (IOException e) {
            throw new JsonMalformedException("JSON malformed");
        }
        return mediaMetadataDto;
    }

    @Override
    public MediaMetadataDto get(String uuid, String uuidBundle) {
        return mapper.toDto(mediaDao.get(uuid, uuidBundle));
    }

    @Override
    public List<MediaMetadataDto> getByBundle(String uuidBundle) {
        // Get all media in bundle folder
        File dirBundle = new File(props.getBasePath() + "/media/" + uuidBundle);
        List<MediaMetadataDto> medias = new ArrayList<>();
        File[] files = dirBundle.listFiles();
        if (files != null) {
            for (File file : files) {
                medias.add(mapper.toDto(mediaDao.get(file.getName().substring(0, file.getName().lastIndexOf(".")), uuidBundle)));
            }
        }
        return medias;
    }

    @Override
    public MediaMetadataDto save(MediaMetadataDto media) {
        return mapper.toDto(mediaDao.save(mapper.fromDto(media)));
    }

    @Override
    public boolean delete(String uuid, String uuidBundle) {
        MediaMetadata media = mediaDao.get(uuid, uuidBundle);
        new File(props.getBaseResources() + "/" + uuidBundle + "/" + uuid + MediaType.getMediaType(media.getMediaType().getMimeType()).getExtension()).delete();

        return mediaDao.delete(uuid, uuidBundle);
    }
}

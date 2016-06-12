package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.MediaDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import com.excilys.shooflers.dashboard.server.service.exception.JsonMalformedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaDao mediaDao;
    
    @Autowired
    private RevisionService revisionService;

    @Autowired
    private MediaDtoMapperImpl mapper;

    @Autowired
    private DashboardProperties props;

    /**
     * Map a json to a media data dto
     *
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
    public MediaMetadataDto get(String uuid) {
        return mapper.toDto(mediaDao.get(uuid));
    }


    @Override
    public List<MediaMetadataDto> getAll() {
        return mapper.toListDto(mediaDao.getAll());
    }
    
    @Override
    public List<MediaMetadataDto> getByBundle(String uuidBundle) {
       return mapper.toListDto(mediaDao.getByBundle(uuidBundle));
    }

    @Override
    public MediaMetadataDto save(MediaMetadataDto media) {
        // TODO IDK where the file blob is stored...
        return mapper.toDto(mediaDao.save(mapper.fromDto(media)));
    }

    @Override
    public void delete(String uuid) {
        if(mediaDao.delete(uuid)) {
            // TODO IDK where the file blobs are removed
            // Create a new revision
            revisionService.add(Revision.Action.DELETE, uuid, Revision.Type.MEDIA, null);
        }
    }
}

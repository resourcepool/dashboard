package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.dao.RevisionDao;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.MediaDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.rest.utils.FileHelper;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private MediaDtoMapperImpl mapper;

    @Autowired
    private RevisionDao revisionDao;

    @Autowired
    private DashboardProperties props;

    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    @RequireValidApiKey
    public MediaMetadataDto get(@PathVariable("uuid") String uuid) {
        return mapper.toDto(mediaDao.get(uuid));
    }

    @RequestMapping(method = RequestMethod.POST)
    @RequireValidUser
    public MediaMetadataDto save(@RequestParam("media") String media, @RequestParam("file") MultipartFile multipartFile) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Mapping json to object, if json is malformed, stop
        MediaMetadataDto mediaMetadataDto;
        try {
            mediaMetadataDto = objectMapper.readValue(media, MediaMetadataDto.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        // Save media
        MediaType mediaType = MediaType.getMediaType(mediaMetadataDto.getMediaType());
        // Not expected a file to save, directly create media with url set
        if (mediaType == MediaType.WEB_SITE || mediaType == MediaType.WEB_VIDEO) {
            mediaMetadataDto = mapper.toDto(mediaDao.save(mapper.fromDto(mediaMetadataDto)));
        } else {
            // Expect a file with a correct extension
            if (multipartFile.isEmpty() || mediaType == MediaType.NONE || MediaType.getMediaType(multipartFile.getContentType()) == MediaType.NONE) {
                throw new IllegalArgumentException("File extension not recognized");
            }

            mediaMetadataDto.setUuid(UUID.randomUUID().toString());
            String fileName = FileHelper.saveFile(multipartFile, mediaMetadataDto.getUuid());
            if (fileName != null) {
                mediaMetadataDto.setUrl(props.getBaseUrl() + "/" + fileName);
                mediaMetadataDto = mapper.toDto(mediaDao.save(mapper.fromDto(mediaMetadataDto)));
            }
        }

        // Create a new revision
        Revision revision = new Revision();
        revision.setRevision(revisionDao.getLatest() + 1);
        revision.setType(Revision.Type.MEDIA);
        revision.setTarget(mediaMetadataDto.getUuid());
        revision.setAction(Revision.Action.ADD);
        revisionDao.save(revision);
        mediaMetadataDto.setRevision(revision.getRevision());
        return mediaMetadataDto;
    }
}

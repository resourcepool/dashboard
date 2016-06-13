package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.MediaDtoMapper;
import com.excilys.shooflers.dashboard.server.exception.ResourceNotFoundException;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.excilys.shooflers.dashboard.server.validator.MediaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequireValidUser
@RequestMapping("/media")
public class MediaController {

    public static final String MESSAGE_MEDIA_NOT_FOUND = "Media not found";
    public static final String MESSAGE_BUNDLE_NOT_FOUND = "Bundle not found for this media";
    public static final String MESSAGE_COMPULSORY_FIELD = "The following field can't be empty : name, bundleTag, mediaType";
    public static final String MESSAGE_MEDIA_TYPE_NOT_SUPPORTED = "MimeType or file extension not supported.";
    public static final String MESSAGE_EXTENSION_NAME = "Extension filename not allowed.";
    public static final String MESSAGE_NEED_FILE = "Need file with this type of media \"%s\".";
    public static final String MESSAGE_NEED_FILE_WHEN_NO_MEDIA_TYPE = "Need file when you don't specify a mediatype.";
    public static final String MESSAGE_NOT_CORRESPONDING_MEDIA_TYPE = "Provided MediaType differnete of Provided mediatype in the file";
    public static final String MESSAGE_NEED_URL = "This mediatype %s need an url.";
    public static final String MESSAGE_MALFORMED_URL = "The url provided is malformed";
    
    @Autowired
    private MediaService mediaService;

    @Autowired
    private MediaDtoMapper mapper;
    
    @Autowired
    private MediaValidator validator;

    @RequireValidApiKey
    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    public MediaMetadataDto get(@PathVariable("uuid") String uuid) {
        MediaMetadataDto result = mapper.toDto(mediaService.get(uuid));
        if (result == null) {
            throw new ResourceNotFoundException(MESSAGE_MEDIA_NOT_FOUND);
        } else {
            return result;
        }
    }

    /**
     * Retrieve medias
     * @param bundleUuid an optional filter request parameter "bundle"
     * @return the list of medias matching the query and optional filters
     */
    @RequireValidApiKey
    @RequestMapping(method = RequestMethod.GET)
    public List<MediaMetadataDto> getAll(@RequestParam(name = "bundle", required = false) String bundleUuid) {
        if (bundleUuid != null) {
            return mapper.toListDto(mediaService.getByBundleTag(bundleUuid));
        }
        else return mapper.toListDto(mediaService.getAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public MediaMetadataDto save(@RequestParam("media") String json, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        MediaMetadataDto mediaMetadataDto = mapper.toDto(json);

        // Check Validation of MediaMetadataDto
        validator.validate(mediaMetadataDto, multipartFile);

        // Save or update media
        Media media = Media
                .builder()
                .metadata(mapper.fromDto(mediaMetadataDto))
                .content(multipartFile)
                .build();
        
        mediaService.save(media);
        
        // Get generated Uuid
        mediaMetadataDto.setUuid(media.getMetadata().getUuid());
        
        return mediaMetadataDto;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public MediaMetadataDto update(@RequestParam("media") String json, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        MediaMetadataDto mediaMetadataDto = mapper.toDto(json);

        // Check Validation of MediaMetadataDto
        validator.validate(mediaMetadataDto, multipartFile);

        // Save or update media
        Media media = Media
                .builder()
                .metadata(mapper.fromDto(mediaMetadataDto))
                .content(multipartFile)
                .build();

        mediaService.update(media);

        // Get generated Uuid
        mediaMetadataDto.setUuid(media.getMetadata().getUuid());

        return mediaMetadataDto;
    }

    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uuid") String uuid) {
        if (mediaService.get(uuid) == null) {
            throw new ResourceNotFoundException(MESSAGE_MEDIA_NOT_FOUND);
        }
        
        mediaService.delete(uuid);
    }

}

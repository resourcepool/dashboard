package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.MediaDtoMapper;
import com.excilys.shooflers.dashboard.server.exception.ResourceNotFoundException;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import org.apache.commons.validator.routines.UrlValidator;
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
    public static final String MESSAGE_COMPULSORY_FIELD = "The following field can't be empty : name, uuidBundle, mediaType";
    public static final String MESSAGE_MEDIA_TYPE_NOT_FOUND = "MediaType not allowed.";
    public static final String MESSAGE_EXTENSION_NAME = "Extension filename not allowed.";
    public static final String MESSAGE_NEED_FILE = "Need file with this type of media \"%s\".";
    public static final String MESSAGE_NEED_FILE_WHEN_NO_MEDIA_TYPE = "Need file when you don't specify a mediatype.";
    public static final String MESSAGE_NOT_CORRESPONDING_MEDIA_TYPE = "Provided MediaType differnete of Provided mediatype in the file";
    public static final String MESSAGE_NEED_URL = "This mediatype %s need an url.";
    public static final String MESSAGE_MALFORMED_URL = "The url provided is malformed";
    
    @Autowired
    private MediaService mediaService;
    
    @Autowired
    private BundleService bundleService;

    @Autowired
    private MediaDtoMapper mapper;
    
    /**
     * UrlValidator to validate URL of media, if they are not empty
     */
    private UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

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
            return mapper.toListDto(mediaService.getByBundle(bundleUuid));
        }
        else return mapper.toListDto(mediaService.getAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public MediaMetadataDto save(@RequestParam("media") String json, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        MediaMetadataDto mediaMetadataDto = mapper.toDto(json);
        mediaMetadataDto.setUuid(null);

        // Check Validation of MediaMetadataDto
        assertValidMediaMetadataDto(mediaMetadataDto, multipartFile);

        // Save media
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

    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uuid") String uuid) {
        if (mediaService.get(uuid) == null) {
            throw new ResourceNotFoundException(MESSAGE_MEDIA_NOT_FOUND);
        }
        
        mediaService.delete(uuid);
    }

    /**
     * Validate an MediaMetadataDto
     * * Check if every compulsory field is set
     * * if uuidBundle and mediaType are valid
     *
     * @param mediaMetadataDto Media to validate
     * @param multipartFile file to validate
     */
    private void assertValidMediaMetadataDto(MediaMetadataDto mediaMetadataDto, MultipartFile multipartFile) {
        if (mediaMetadataDto.getName() == null || mediaMetadataDto.getName().isEmpty() ||
                mediaMetadataDto.getUuidBundle() == null || mediaMetadataDto.getUuidBundle().isEmpty() ||
                mediaMetadataDto.getMediaType() == null || mediaMetadataDto.getMediaType().isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_COMPULSORY_FIELD);
        }

        if (bundleService.get(mediaMetadataDto.getUuidBundle()) == null) {
            throw new IllegalArgumentException(MESSAGE_BUNDLE_NOT_FOUND);
        }

        MediaType mediaType = MediaType.getMediaType(mediaMetadataDto.getMediaType()); 
        
        if (mediaType == null) {
            throw new IllegalArgumentException(MESSAGE_MEDIA_NOT_FOUND);
        }

        // Not expected a file to save, directly save media with url set
        if (mediaType == MediaType.WEB_SITE || mediaType == MediaType.WEB_VIDEO) {
            if (mediaMetadataDto.getUrl() == null || mediaMetadataDto.getUrl().isEmpty()) {
                throw new IllegalArgumentException(String.format(MESSAGE_NEED_URL, mediaType.getMimeType()));
            }

            if (!urlValidator.isValid(mediaMetadataDto.getUrl())) {
                throw new IllegalArgumentException(MESSAGE_MALFORMED_URL);
            }
        } else {
            if (multipartFile == null) {
                if (mediaType == MediaType.NONE) {
                    throw new IllegalArgumentException(MESSAGE_NEED_FILE_WHEN_NO_MEDIA_TYPE);
                } else {
                    throw new IllegalArgumentException(String.format(MESSAGE_NEED_FILE, mediaType.getMimeType()));
                }
            }

            MediaType mediaTypeFile = MediaType.getMediaType(multipartFile.getContentType());
            if (mediaTypeFile == MediaType.NONE) {
                throw new IllegalArgumentException(MESSAGE_MEDIA_TYPE_NOT_FOUND);
            }

            if (mediaType != MediaType.NONE && mediaType != mediaTypeFile) {
                throw new IllegalArgumentException(MESSAGE_NOT_CORRESPONDING_MEDIA_TYPE);
            }
        }
    }
}

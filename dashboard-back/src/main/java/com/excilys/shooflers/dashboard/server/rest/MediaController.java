package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.exception.ResourceNotFoundException;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.rest.utils.FileHelper;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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
    private RevisionService revisionService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private DashboardProperties props;

    @Autowired
    private BundleService bundleService;

    /**
     * UrlValidator to validate URL of media, if they are not empty
     */
    private UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    @RequireValidApiKey
    @RequestMapping(value = "{uuidbundle}/{uuid}", method = RequestMethod.GET)
    public MediaMetadataDto get(@PathVariable("uuidbundle") String uuidBundle, @PathVariable("uuid") String uuid) {
        MediaMetadataDto result = mediaService.get(uuid, uuidBundle);
        if (result == null) {
            throw new ResourceNotFoundException(MESSAGE_MEDIA_NOT_FOUND);
        } else {
            return result;
        }
    }

    @RequireValidApiKey
    @RequestMapping(value = "{uuidbundle}", method = RequestMethod.GET)
    public List<MediaMetadataDto> getAllByBundle(@PathVariable("uuidbundle") String uuidBundle) {
        if (bundleService.get(uuidBundle) == null) {
            throw new ResourceNotFoundException(MESSAGE_BUNDLE_NOT_FOUND);
        }
        return mediaService.getByBundle(uuidBundle);
    }

    @RequestMapping(method = RequestMethod.POST)
    public MediaMetadataDto save(@RequestParam("media") String json, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        MediaMetadataDto mediaMetadataDto = mediaService.fromJson(json);
        mediaMetadataDto.setUuid(null);

        // Check Validation of MediaMetadataDto
        assertValisationMediaMetadataDto(mediaMetadataDto);

        // Save media
        MediaType mediaType = MediaType.getMediaType(mediaMetadataDto.getMediaType());

        // Not expected a file to save, directly save media with url set
        if (mediaType == MediaType.WEB_SITE || mediaType == MediaType.WEB_VIDEO) {
            if (mediaMetadataDto.getUrl() == null || mediaMetadataDto.getUrl().isEmpty()) {
                throw new IllegalArgumentException(String.format(MESSAGE_NEED_URL, mediaType.getMimeType()));
            }

            if (!urlValidator.isValid(mediaMetadataDto.getUrl())) {
                throw new IllegalArgumentException(MESSAGE_MALFORMED_URL);
            }
            mediaMetadataDto = mediaService.save(mediaMetadataDto);
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

            mediaMetadataDto.setUuid(UUID.randomUUID().toString());
            if (FileHelper.saveFile(multipartFile, mediaMetadataDto, props.getBaseUrl())) {
                mediaMetadataDto = mediaService.save(mediaMetadataDto);
            }
        }

        // Create a new revision
        mediaMetadataDto.setRevision(revisionService.add(Revision.Action.ADD, mediaMetadataDto.getUuid(), Revision.Type.MEDIA, null).getRevision());
        return mediaMetadataDto;
    }

    @RequestMapping(value = "{uuidbundle}/{uuid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uuidbundle") String uuidBundle, @PathVariable("uuid") String uuid) {
        if (mediaService.get(uuid, uuidBundle) == null) {
            throw new ResourceNotFoundException(MESSAGE_MEDIA_NOT_FOUND);
        }

        if (mediaService.delete(uuid, uuidBundle)) {
            // Create a new revision
            revisionService.add(Revision.Action.DELETE, uuid, Revision.Type.MEDIA, null);
        }
    }

    /**
     * Validate an MediaMetadataDto
     * * Check if every compulsory field is set
     * * if uuidBundle and mediaType are valid
     *
     * @param mediaMetadataDto Media to validate
     */
    private void assertValisationMediaMetadataDto(MediaMetadataDto mediaMetadataDto) {
        if (mediaMetadataDto.getName() == null || mediaMetadataDto.getName().isEmpty() ||
                mediaMetadataDto.getUuidBundle() == null || mediaMetadataDto.getUuidBundle().isEmpty() ||
                mediaMetadataDto.getMediaType() == null || mediaMetadataDto.getMediaType().isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_COMPULSORY_FIELD);
        }

        if (bundleService.get(mediaMetadataDto.getUuidBundle()) == null) {
            throw new IllegalArgumentException(MESSAGE_BUNDLE_NOT_FOUND);
        }

        if (MediaType.getMediaType(mediaMetadataDto.getMediaType()) == null) {
            throw new IllegalArgumentException(MESSAGE_MEDIA_NOT_FOUND);
        }
    }
}

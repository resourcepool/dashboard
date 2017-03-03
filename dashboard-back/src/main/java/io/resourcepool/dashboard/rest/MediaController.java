package io.resourcepool.dashboard.rest;

import io.resourcepool.dashboard.dto.MediaMetadataDto;
import io.resourcepool.dashboard.dto.mapper.MediaDtoMapper;
import io.resourcepool.dashboard.exception.ResourceNotFoundException;
import io.resourcepool.dashboard.model.Content;
import io.resourcepool.dashboard.model.Media;
import io.resourcepool.dashboard.model.metadata.MediaMetadata;
import io.resourcepool.dashboard.model.type.MediaType;
import io.resourcepool.dashboard.security.annotation.RequireValidApiKey;
import io.resourcepool.dashboard.security.annotation.RequireValidUser;
import io.resourcepool.dashboard.service.MediaService;
import io.resourcepool.dashboard.validator.MediaValidator;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public static final String MESSAGE_NEED_URL = "This mediatype %s requires an url.";
    public static final String MESSAGE_NEED_CONTENT = "This mediatype %s requires a text content attribute.";
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
     *
     * @param bundleTag an optional filter request parameter "bundle"
     * @return the list of medias matching the query and optional filters
     */
    @RequireValidApiKey
    @RequestMapping(method = RequestMethod.GET)
    public List<MediaMetadataDto> getAll(@RequestParam(name = "bundle", required = false) String bundleTag) {
        if (bundleTag != null) {
            return mapper.toListDto(mediaService.getByBundleTag(bundleTag));
        } else {
            return mapper.toListDto(mediaService.getAll());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public MediaMetadataDto save(@RequestParam("media") String json, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        MediaMetadataDto mediaMetadataDto = mapper.toDto(json);

        // Check Validation of MediaMetadataDto
        validator.validate(mediaMetadataDto, multipartFile);

        MediaMetadata meta = mapper.fromDto(mediaMetadataDto);
        // Save or update media
        Media media = Media
                .builder()
                .metadata(meta)
                .content(multipartFile == null ? null : new Content(multipartFile, meta.getMediaType()))
                .build();

        mediaService.save(media);

        // Get generated Uuid
        mediaMetadataDto.setUuid(media.getMetadata().getUuid());

        // Get generated Url si non media web
        mediaMetadataDto.setUrl(media.getMetadata().getUrl());

        return mediaMetadataDto;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public MediaMetadataDto update(@RequestBody MediaMetadataDto mediaMetadataDto) {
        MediaMetadata mediaMetadataFromDb;
        if (mediaMetadataDto.getUuid() == null || (mediaMetadataFromDb = mediaService.get(mediaMetadataDto.getUuid())) == null) {
            throw new ResourceNotFoundException("A valid uuid is required to edit a bundle.");
        }

        // Check Validation of MediaMetadataDto
        validator.validate(mediaMetadataDto, null);

        // Additionnal Validations
        if (MediaMetadata.hasFile(mediaMetadataDto.getMediaType())
                && !StringUtils.equals(mediaMetadataFromDb.getUrl(), mediaMetadataDto.getUrl())) {
            throw new IllegalArgumentException("You can set the URL if the media has a file.");
        }

        // Mapper
        MediaMetadata meta = mapper.fromDto(mediaMetadataDto);

        // Save or update media
        Media media = Media
                .builder()
                .metadata(meta)
                .build();

        mediaService.update(media);

        // Get generated Uuid
        mediaMetadataDto.setUuid(media.getMetadata().getUuid());

        return mediaMetadataDto;
    }

    @RequestMapping(value = "{uuid}/file", method = RequestMethod.POST)
    @ApiOperation(value = "Permet de mettre à jour le fichier d'un media et de changer aussi son type.")
    public MediaMetadataDto updateWithFile(@PathVariable("uuid") String uuid, @RequestParam("file") MultipartFile multipartFile) {
        MediaMetadata mediaMetadata = mediaService.get(uuid);

        if (mediaMetadata == null) {
            throw new ResourceNotFoundException("A valid uuid is required to edit a bundle.");
        }

        MediaType mediaType = MediaType.parseMimeType(multipartFile.getContentType());
        if (mediaType == null) {
            throw new IllegalArgumentException(MESSAGE_MEDIA_TYPE_NOT_SUPPORTED);
        }

        MediaMetadataDto mediaMetadataDto = mapper.toDto(mediaMetadata);

        mediaMetadataDto.setMediaType(mediaType.toString());

        // Check Validation of MediaMetadataDto
        validator.validate(mediaMetadataDto, multipartFile);

        // Mapper
        MediaMetadata meta = mapper.fromDto(mediaMetadataDto);

        // Save or update media
        Media media = Media
                .builder()
                .metadata(meta)
                .content(new Content(multipartFile, meta.getMediaType()))
                .build();

        mediaService.update(media);

        // Get generated Uuid
        mediaMetadataDto.setUuid(media.getMetadata().getUuid());

        // Get generated Url si non media web
        mediaMetadataDto.setUrl(media.getMetadata().getUrl());

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

package com.excilys.shooflers.dashboard.server.validator;

import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.excilys.shooflers.dashboard.server.rest.MediaController.*;

/**
 * @author Loïc Ortola on 13/06/2016.
 */
@Component
public class MediaValidator {

    /**
     * UrlValidator to validate URL of media, if they are not empty
     */
    private UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    /**
     * Validate an MediaMetadataDto
     * * Check if every compulsory field is set
     * * if bundleTag and mediaType are valid
     *
     * @param mediaMetadataDto Media to validate
     * @param multipartFile    file to validate
     */
    public void validate(MediaMetadataDto mediaMetadataDto, MultipartFile multipartFile) {
        if (mediaMetadataDto.getName() == null || mediaMetadataDto.getName().isEmpty() ||
                mediaMetadataDto.getBundleTag() == null || mediaMetadataDto.getBundleTag().isEmpty() ||
                mediaMetadataDto.getMediaType() == null || mediaMetadataDto.getMediaType().isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_COMPULSORY_FIELD);
        }

        MediaType mediaType = MediaType.valueOf(mediaMetadataDto.getMediaType());

        if (mediaType == null) {
            throw new IllegalArgumentException(MESSAGE_MEDIA_NOT_FOUND);
        }

        // No file expected
        if (mediaType == MediaType.WEB) {
            // Check url validity
            if (mediaMetadataDto.getUrl() == null || mediaMetadataDto.getUrl().isEmpty()) {
                throw new IllegalArgumentException(String.format(MESSAGE_NEED_URL, mediaType.toString()));
            }
            if (!urlValidator.isValid(mediaMetadataDto.getUrl())) {
                throw new IllegalArgumentException(MESSAGE_MALFORMED_URL);
            }
            return;
        }
        
        // File expected
        if (multipartFile == null) {
            if (mediaType == null) {
                throw new IllegalArgumentException(MESSAGE_NEED_FILE_WHEN_NO_MEDIA_TYPE);
            } else {
                throw new IllegalArgumentException(String.format(MESSAGE_NEED_FILE, mediaType.getValidMimeTypes()));
            }
        }

        // Test file mime type cohesion with meta data
        if (!mediaType.supports(multipartFile.getContentType())) {
            throw new IllegalArgumentException(MESSAGE_NOT_CORRESPONDING_MEDIA_TYPE);
        }
        
    }
}

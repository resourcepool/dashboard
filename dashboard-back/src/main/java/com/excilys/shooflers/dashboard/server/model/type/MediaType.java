package com.excilys.shooflers.dashboard.server.model.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Type of a media.
 */
public enum MediaType {

    IMAGE(new String[]{"image/png", ".png"},
            new String[]{"image/jpg", ".jpg"},
            new String[]{"image/jpeg", ".jpg"},
            new String[]{"image/gif", ".gif"}),
    VIDEO(new String[]{"video/mpeg", ".mpg"},
            new String[]{"video/mp4", ".mp4"},
            new String[]{"video/x-msvideo", ".avi"},
            new String[]{"video/x-flv", ".flv"}),
    DOCUMENT(new String[]{"application/vnd.ms-powerpoint", ".pptx"},
            new String[]{"application/pdf", ".pdf"}),
    WEB(new String[]{"application/web-site", ""}),
    NEWS(new String[]{"text/plain", ""});

    private Map<String, String> mimeTypes;

    MediaType(String[]... mimeTypes) {
        this.mimeTypes = Arrays
                .stream(mimeTypes)
                .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));
    }

    /**
     * @param mimeType the mime type (image/png is a valid input)
     * @return true if mime type is supported by media type, false otherwise
     */
    public boolean supports(String mimeType) {
        return mimeTypes.containsKey(mimeType);
    }

    /**
     * @param ext the extension (with or without a prefixed dot (.png or png are both valid inputs)
     * @return true if extension is supported by media type, false otherwise
     */
    public boolean supportsExtension(String ext) {
        return mimeTypes.containsValue(ext.startsWith(".") ? ext : "." + ext);
    }

    /**
     * MimeType to MediaType
     *
     * @param mimeType mime type to convert
     * @return MediaType
     */
    public static MediaType parseMimeType(String mimeType) {
        if (mimeType == null) {
            return null;
        }

        for (MediaType m : MediaType.values()) {
            if (m.supports(mimeType)) {
                return m;
            }
        }
        return null;
    }

    /**
     * @param mimeType the mime type
     * @return the valid extension for the mime type of current media
     */
    public String getExtension(String mimeType) {
        return mimeTypes.get(mimeType);
    }

    public Collection<String> getValidMimeTypes() {
        return mimeTypes.keySet();
    }
}
package com.excilys.shooflers.dashboard.server.model.type;

/**
 * Type of a media.
 */
public enum MediaType {
    IMAGE_PNG("image/png", ".png", "image"),
    IMAGE_JPG("image/jpeg", ".jpg", "image"),
    IMAGE_GIF("image/gif", ".gif", "image"),
    VIDEO_MPEG("video/mpeg", ".mpg", "video"),
    VIDEO_MP4("video/mp4", ".mp4", "video"),
    VIDEO_AVI("video/x-msvideo", ".avi", "video"),
    VIDEO_FLV("video/x-flv", ".flv", "video"),
    PDF("application/pdf", ".pdf", "document"),
    POWERPOINT("application/vnd.ms-powerpoint", ".pptx", "document"),
    WEB_SITE("application/web-site", null, "web"),
    WEB_VIDEO("application/web-video", null, "web"),
    NONE("none", null, "none");

    private String mimeType;

    private String extension;

    private String globalType;

    MediaType(String mimeType, String extension, String globalType) {
        this.mimeType = mimeType;
        this.extension = extension;
        this.globalType = globalType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getGlobalType() {
        return globalType;
    }

    /**
     * MimeType to MediaType
     * @param mimeType mime type to convert
     * @return MediaType
     */
    public static MediaType getMediaType(String mimeType) {
        MediaType mediaType = NONE;
        if (mimeType == null) {
            return mediaType;
        }

        for (MediaType m : MediaType.values()) {
            if (mimeType.equals(m.getMimeType())) {
                mediaType = m;
                break;
            }
        }
        return mediaType;
    }

    /**
     * MimeType to extension
     * @param mimeType mime type to convert
     * @return Extension
     */
    public static String getExtension(String mimeType) {
        String extension = "";
        if (mimeType == null) {
            return extension;
        }

        for (MediaType m : MediaType.values()) {
            if (mimeType.equals(m.getMimeType())) {
                extension = m.getExtension();
                break;
            }
        }
        return extension;
    }
}
package io.resourcepool.dashboard.model.type;

/**
 * Type of a media.
 */
public enum MediaType {
    IMAGE_PNG("image/png"),
    IMAGE_JPG("image/jpeg"),
    IMAGE_GIF("image/gif"),
    VIDEO_MPEG("video/mpeg"),
    VIDEO_MP4("video/mp4"),
    VIDEO_AVI("video/x-msvideo"),
    VIDEO_FLV("video/x-flv"),
    PDF("application/pdf"),
    POWERPOINT("application/vnd.ms-powerpoint"),
    WEB_SITE("application/web-site"),
    WEB_VIDEO("application/web-video"),
    NONE("none");

    private String mimeType;

    MediaType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    /**
     * MimeType to MediaType
     * @param mimeType mime type to convert
     * @return MediaType
     */
    public static MediaType getMediaType(String mimeType) {
        MediaType mediaType;
        switch (mimeType) {
            case "image/png":
                mediaType = IMAGE_PNG;
                break;
            case "image/jpeg":
                mediaType = IMAGE_JPG;
                break;
            case "image/gif":
                mediaType = IMAGE_GIF;
                break;
            case "video/mpeg":
                mediaType = VIDEO_MPEG;
                break;
            case "video/mp4":
                mediaType = VIDEO_MP4;
                break;
            case "video/avi":
                mediaType = VIDEO_AVI;
                break;
            case "video/flv":
                mediaType = VIDEO_FLV;
                break;
            case "application/pdf":
                mediaType = PDF;
                break;
            case  "application/vnd.ms-powerpoint":
                mediaType = POWERPOINT;
                break;
            case "application/web-site":
                mediaType = WEB_SITE;
                break;
            case "application/web-video":
                mediaType = WEB_VIDEO;
                break;
            default:
                mediaType = NONE;
        }

        return mediaType;
    }

    /**
     * MimeType to extension
     * @param mimeType mime type to convert
     * @return Extension
     */
    public static String getExtension(String mimeType) {
        String extension;
        switch (mimeType) {
            case "image/png":
                extension = ".png";
                break;
            case "image/jpeg":
                extension = ".jpg";
                break;
            case "image/gif":
                extension = ".gif";
                break;
            case "video/mpeg":
                extension = ".mpg";
                break;
            case "video/mp4":
                extension = ".mp4";
                break;
            case "video/avi":
                extension = ".avi";
                break;
            case "video/flv":
                extension = ".flv";
                break;
            case "application/pdf":
                extension = ".pdf";
                break;
            case  "application/vnd.ms-powerpoint":
                extension = ".pptx";
                break;
            default:
                extension = "";
        }

        return extension;
    }
}
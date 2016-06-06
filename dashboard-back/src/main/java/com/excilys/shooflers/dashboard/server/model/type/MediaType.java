package com.excilys.shooflers.dashboard.server.model.type;

/**
 * Type of a media.
 */
public enum MediaType {
    IMAGE_PNG("image/png"),
    IMAGE_JPG("image/jpeg"),
    VIDEO("video"),
    WEB("web"),
    PDF("pdf");

    private String mimeType;

    MediaType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
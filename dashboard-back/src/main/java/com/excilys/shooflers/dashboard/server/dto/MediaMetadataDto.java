package com.excilys.shooflers.dashboard.server.dto;


import com.excilys.shooflers.dashboard.server.model.type.MediaType;


public class MediaMetadataDto {

    private String uuid;

    private String name;

    private int duration;

    private String mediaType;

    private ValidityDto validity;

    private String url;

    private String uuidBundle;

    public MediaMetadataDto() { }

    private MediaMetadataDto(String uuid, String name, int duration, MediaType mediaType, ValidityDto validity, String url, String uuidBundle) {
        this.uuid = uuid;
        this.name = name;
        this.duration = duration;
        this.mediaType = mediaType.toString();
        this.validity = validity;
        this.url = url;
        this.uuidBundle = uuidBundle;
    }

    public static class Builder {

        private String name;

        private int duration;

        private MediaType mediaType;

        private ValidityDto validityDto;

        private String uuid;

        private String url;

        private String uuidBundle;

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder validity(ValidityDto validity) {
            this.validityDto = validity;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder uuidBundle(String uuidBundle) {
            this.uuidBundle = uuidBundle;
            return this;
        }

        public MediaMetadataDto build() {
            return new MediaMetadataDto(uuid, name, duration, mediaType, validityDto, url, uuidBundle);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public ValidityDto getValidity() {
        return validity;
    }

    public void setValidity(ValidityDto validity) {
        this.validity = validity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuidBundle() {
        return uuidBundle;
    }
}

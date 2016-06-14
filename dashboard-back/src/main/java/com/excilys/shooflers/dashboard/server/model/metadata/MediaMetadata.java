package com.excilys.shooflers.dashboard.server.model.metadata;

import com.excilys.shooflers.dashboard.server.model.Validity;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * A media represent a file in memory (a file can be an image, a pdf, a video or a website).
 */
public class MediaMetadata {

    /**
     * MediaType that no need a file to upload
     */
    public static final List<MediaType> MEDIA_TYPES_NO_FILE = Arrays.asList(MediaType.WEB, MediaType.NEWS);

    private String uuid;

    private String name;

    private int duration;

    private MediaType mediaType;

    private Validity validity;

    // Used only for WEB type
    private String url;

    // Used only for NEWS type
    private String content;

    private String bundleTag;

    public MediaMetadata() {
    }

    private MediaMetadata(String uuid, String name, int duration, MediaType mediaType, Validity validity, String url, String content, String bundleTag) {
        this.uuid = uuid;
        this.name = name;
        this.duration = duration;
        this.mediaType = mediaType;
        this.validity = validity;
        this.url = url;
        this.content = content;
        this.bundleTag = bundleTag;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;

        private int duration;

        private MediaType mediaType;

        private Validity validity;

        private String uuid;

        private String url;

        private String content;

        private String bundleTag;

        private Builder() {
        }

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

        public Builder validity(Validity validity) {
            this.validity = validity;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder bundleTag(String bundleTag) {
            this.bundleTag = bundleTag;
            return this;
        }

        public MediaMetadata build() {
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }
            return new MediaMetadata(uuid, name, duration, mediaType, validity, url, content, bundleTag);
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

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBundleTag() {
        return bundleTag;
    }

    public void setBundleTag(String bundleTag) {
        this.bundleTag = bundleTag;
    }

    public boolean hasFile() {
        return !MEDIA_TYPES_NO_FILE.contains(mediaType);
    }
}

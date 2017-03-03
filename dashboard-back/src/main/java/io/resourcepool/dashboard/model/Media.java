package io.resourcepool.dashboard.model;

import io.resourcepool.dashboard.model.metadata.MediaMetadata;

/**
 * @author Loïc Ortola on 13/06/2016.
 */
public class Media {
    private MediaMetadata metadata;
    private Content content;

    public MediaMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(MediaMetadata metadata) {
        this.metadata = metadata;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private MediaMetadata metadata;
        private Content content;

        private Builder() {
        }

        public Builder metadata(MediaMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder content(Content content) {
            this.content = content;
            return this;
        }

        public Media build() {
            Media media = new Media();
            media.setMetadata(metadata);
            media.setContent(content);
            return media;
        }
    }
}

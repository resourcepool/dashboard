package com.excilys.shooflers.dashboard.server.model;

import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Loïc Ortola on 13/06/2016.
 */
public class Media {
    private MediaMetadata metadata;
    private MultipartFile content;

    public MediaMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(MediaMetadata metadata) {
        this.metadata = metadata;
    }

    public MultipartFile getContent() {
        return content;
    }

    public void setContent(MultipartFile content) {
        this.content = content;
    }



    public static Builder builder() {
        return new Builder();
    }
    
    public static final class Builder {
        private MediaMetadata metadata;
        private MultipartFile content;

        private Builder() {
        }

        public Builder metadata(MediaMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder content(MultipartFile content) {
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

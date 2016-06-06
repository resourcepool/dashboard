package com.excilys.shooflers.dashboard.server.model.metadata;


import com.excilys.shooflers.dashboard.server.model.Validity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Slideshow contains a list of media to display.
 */
public class SlideshowMetadata {

    private String uuid;

    private String name;

    private Validity validity;

    private List<MediaMetadata> medias;

    private SlideshowMetadata(String uuid, String name, Validity validity, List<MediaMetadata> medias) {
        this.uuid = uuid;
        this.name = name;
        this.validity = validity;
        this.medias = medias;
    }

    public static class Builder {
        private String name;

        private Validity validity;

        private List<MediaMetadata> medias;

        private String uuid;

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder validity(Validity validity) {
            this.validity = validity;
            return this;
        }

        public Builder medias(List<MediaMetadata> medias) {
            this.medias = medias;
            return this;
        }

        public SlideshowMetadata build() {
            if (medias == null) {
                medias = new ArrayList<>();
            }

            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }

            if (validity == null) {
                validity = new Validity.Builder().build();
            }
            return new SlideshowMetadata(uuid, name, validity, medias);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public List<MediaMetadata> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaMetadata> medias) {
        this.medias = medias;
    }
}

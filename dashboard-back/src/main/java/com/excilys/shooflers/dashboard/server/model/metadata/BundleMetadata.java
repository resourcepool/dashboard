package com.excilys.shooflers.dashboard.server.model.metadata;


import com.excilys.shooflers.dashboard.server.model.Validity;

import java.util.UUID;

/**
 * Slideshow contains a list of media to display.
 */
public class BundleMetadata {

    private String uuid;

    private String tag;

    private String name;

    private Validity validity;

    public BundleMetadata() {
    }

    private BundleMetadata(String uuid, String tag, String name, Validity validity) {
        this.uuid = uuid;
        this.tag = tag;
        this.name = name;
        this.validity = validity;
    }

    public static class Builder {

        private String uuid;

        private String name;

        private Validity validity;

        private String tag;

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
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

        public BundleMetadata build() {
            if (tag == null) {
                tag = UUID.randomUUID().toString();
            }
            return new BundleMetadata(uuid, tag, name, validity);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
}

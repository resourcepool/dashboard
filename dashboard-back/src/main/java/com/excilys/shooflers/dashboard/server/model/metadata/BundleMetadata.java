package com.excilys.shooflers.dashboard.server.model.metadata;


import com.excilys.shooflers.dashboard.server.model.Validity;

import java.util.UUID;

/**
 * Slideshow contains a list of media to display.
 */
public class BundleMetadata {

    private String uuid;

    private String name;

    private Validity validity;

    private BundleMetadata(String uuid, String name, Validity validity) {
        this.uuid = uuid;
        this.name = name;
        this.validity = validity;
    }

    public static class Builder {
        private String name;

        private Validity validity;

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

        public BundleMetadata build() {
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }
            return new BundleMetadata(uuid, name, validity);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

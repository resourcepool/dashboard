package com.excilys.shooflers.dashboard.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BundleMetadataDto {

    private String uuid;

    private String name;

    private ValidityDto validity;

    private long revision;

    @SuppressWarnings("unused")
    public BundleMetadataDto() { }

    private BundleMetadataDto(String uuid, String name, ValidityDto validity) {
        this.uuid = uuid;
        this.name = name;
        this.validity = validity;
    }

    public static class Builder {
        private String name;

        private ValidityDto validityDto;

        private String uuid;

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder validity(ValidityDto validityDto) {
            this.validityDto = validityDto;
            return this;
        }

        public BundleMetadataDto build() {
            return new BundleMetadataDto(uuid, name, validityDto);
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

    public ValidityDto getValidity() {
        return validity;
    }

    public void setValidity(ValidityDto validity) {
        this.validity = validity;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }
}

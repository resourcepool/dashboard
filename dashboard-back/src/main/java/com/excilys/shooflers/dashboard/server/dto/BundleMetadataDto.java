package com.excilys.shooflers.dashboard.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BundleMetadataDto {

    private String uuid;

    private String tag;

    private String name;

    private ValidityDto validity;

    @SuppressWarnings("unused")
    public BundleMetadataDto() {
    }

    private BundleMetadataDto(String uuid, String tag, String name, ValidityDto validity) {
        this.uuid = uuid;
        this.tag = tag;
        this.name = name;
        this.validity = validity;
    }

    public static class Builder {
        private String uuid;

        private String name;

        private ValidityDto validityDto;

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

        public Builder validity(ValidityDto validityDto) {
            this.validityDto = validityDto;
            return this;
        }

        public BundleMetadataDto build() {
            return new BundleMetadataDto(uuid, tag, name, validityDto);
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

    public void setTag(String tag) {
        this.tag = tag;
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

}

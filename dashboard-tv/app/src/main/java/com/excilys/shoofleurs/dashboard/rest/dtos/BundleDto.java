package com.excilys.shoofleurs.dashboard.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by excilys on 09/06/16.
 */
public class BundleDto {
    @JsonProperty("uuid")
    private String mUuid;

    @JsonProperty("name")
    private String mName;

    @JsonProperty("validity")
    private ValidityDto mValidity;

    @JsonProperty("revision")
    private long mRevision;

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String uuid) {
        mUuid = uuid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ValidityDto getValidity() {
        return mValidity;
    }

    public void setValidity(ValidityDto validity) {
        mValidity = validity;
    }

    public long getRevision() {
        return mRevision;
    }

    public void setRevision(long revision) {
        mRevision = revision;
    }
}

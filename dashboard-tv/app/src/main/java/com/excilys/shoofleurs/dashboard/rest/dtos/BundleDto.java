package com.excilys.shoofleurs.dashboard.rest.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by excilys on 09/06/16.
 */
public class BundleDto {
    @SerializedName("uuid")
    private String mUuid;

    @SerializedName("name")
    private String mName;

    @SerializedName("validity")
    private ValidityDto mValidity;

    @SerializedName("revision")
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

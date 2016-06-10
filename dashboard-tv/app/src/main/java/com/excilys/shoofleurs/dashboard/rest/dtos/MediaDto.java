package com.excilys.shoofleurs.dashboard.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by excilys on 09/06/16.
 */
public class MediaDto {
    @JsonProperty("uuid")
    private String mUuid;

    @JsonProperty("name")
    private String mName;

    @JsonProperty("mediaType")
    private String mMediaType;

    @JsonProperty("validity")
    private ValidityDto mValidity;

    @JsonProperty("url")
    private String mUrl;

    @JsonProperty("duration")
    private int mDuration;

    @JsonProperty("uuidBundle")
    private String mUuidBundle;

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

    public String getMediaType() {
        return mMediaType;
    }

    public void setMediaType(String mediaType) {
        mMediaType = mediaType;
    }

    public ValidityDto getValidity() {
        return mValidity;
    }

    public void setValidity(ValidityDto validity) {
        mValidity = validity;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getUuidBundle() {
        return mUuidBundle;
    }

    public void setUuidBundle(String uuidBundle) {
        mUuidBundle = uuidBundle;
    }

    public long getRevision() {
        return mRevision;
    }

    public void setRevision(long revision) {
        mRevision = revision;
    }
}

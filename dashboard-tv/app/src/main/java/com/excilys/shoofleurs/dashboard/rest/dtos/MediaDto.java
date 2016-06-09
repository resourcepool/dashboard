package com.excilys.shoofleurs.dashboard.rest.dtos;

import com.excilys.shoofleurs.dashboard.model.entities.Validity;
import com.excilys.shoofleurs.dashboard.model.type.MediaType;
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
    private MediaType mMediaType;

    @JsonProperty("validity")
    private Validity mValidity;

    @JsonProperty("url")
    private String mUrl;

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

    public MediaType getMediaType() {
        return mMediaType;
    }

    public void setMediaType(MediaType mediaType) {
        mMediaType = mediaType;
    }

    public Validity getValidity() {
        return mValidity;
    }

    public void setValidity(Validity validity) {
        mValidity = validity;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}

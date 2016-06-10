package com.excilys.shoofleurs.dashboard.model.entities;

import com.excilys.shoofleurs.dashboard.model.type.MediaType;

/**
 * Created by excilys on 09/06/16.
 */
public class Media {
    private String mUuid;

    private String mName;

    private MediaType mMediaType;

    private Validity mValidity;

    private String mUrl;

    private int mDuration;

    private String mUuidBundle;

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

    @Override
    public String toString() {
        return "Media{" +
                "mUuid='" + mUuid + '\'' +
                ", mName='" + mName + '\'' +
                ", mMediaType=" + mMediaType +
                ", mValidity=" + mValidity +
                ", mUrl='" + mUrl + '\'' +
                ", mDuration=" + mDuration +
                ", mUuidBundle='" + mUuidBundle + '\'' +
                ", mRevision=" + mRevision +
                '}';
    }
}

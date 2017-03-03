package io.resourcepool.dashboard.rest.events;

import io.resourcepool.dashboard.model.entities.Media;

import java.util.List;


public class GetMediaResponseEvent {
    private List<Media> mMedias;

    public GetMediaResponseEvent(List<Media> medias) {
        mMedias = medias;
    }

    public List<Media> getMedias() {
        return mMedias;
    }
}

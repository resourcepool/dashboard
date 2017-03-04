package io.resourcepool.dashboard.rest.events;

import io.resourcepool.dashboard.model.entities.Media;


public class GetMediaResponseEvent {
    private Media mMedia;

    public GetMediaResponseEvent(Media media) {
        mMedia = media;
    }

    public Media getMedia() {
        return mMedia;
    }
}

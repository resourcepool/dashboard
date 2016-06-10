package com.excilys.shoofleurs.dashboard.rest.events;

import com.excilys.shoofleurs.dashboard.model.entities.Media;

import java.util.List;

/**
 * Created by excilys on 09/06/16.
 */
public class GetMediaResponseEvent {
    private List<Media> mMedias;

    public GetMediaResponseEvent(List<Media> medias) {
        mMedias = medias;
    }

    public List<Media> getMedias() {
        return mMedias;
    }
}

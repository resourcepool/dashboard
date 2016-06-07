package com.excilys.shoofleurs.dashboard.rest.events;

import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;

import java.util.List;

/**
 * Created by excilys on 07/06/16.
 */
public class SlideShowUpdatesResponseEvent {
    private List<SlideShow> mSlideShows;

    public SlideShowUpdatesResponseEvent(List<SlideShow> slideShows) {
        mSlideShows = slideShows;
    }

    public List<SlideShow> getSlideShows() {
        return mSlideShows;
    }
}

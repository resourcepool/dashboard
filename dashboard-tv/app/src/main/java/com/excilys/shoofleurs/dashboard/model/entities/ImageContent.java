package com.excilys.shoofleurs.dashboard.model.entities;

import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Image content extends AbstractContent. This class adds a property, the duration display.
 */
public class ImageContent extends AbstractContent {

    /**
     * This field is the duration display. Must be > 0, if not precise,
     * the default value is 5.
     */
    @JsonProperty("durationInSlideShow")
    @JsonView(Views.FullContent.class)
    private int mDurationInSlideShow = 5;

    public ImageContent() {
        super();
    }

    public ImageContent(String title) {
        super(title);
    }

    public ImageContent(String title, String url) {
        super(title, url);
    }

    public int getDurationInSlideShow() {
        return mDurationInSlideShow;
    }

    public void setDurationInSlideShow(int durationInSlideShow) {
        mDurationInSlideShow = durationInSlideShow;
    }


}

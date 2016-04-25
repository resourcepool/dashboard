package com.excilys.shoofleurs.dashboard.model.entities;

import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * WebContent is a website to show. It adds to extra property.
 */
public class WebContent extends AbstractContent {

	/**
	 * Duration display, by default 20.
	 */
	@JsonProperty("durationInSlideShow")
	@JsonView(Views.FullContent.class)
	private int mDurationInSlideShow = 20;

	/**
	 * If the website doesn't fit into the screen, enable auto scroll
	 * to the the page. Default set to true.
	 */
	@JsonProperty("isAutoScroll")
	@JsonView(Views.FullContent.class)
	private boolean mIsAutoScroll = true;


	public WebContent() {
		super();
	}

	public WebContent(String title) {
		super(title);
	}

	public WebContent(String title, String url) {
		super(title, url);
	}

	public int getDurationInSlideshow() {
		return mDurationInSlideShow;
	}

	public void setDurationInSlideShow(int durationInSlideShow) {
		mDurationInSlideShow = durationInSlideShow;
	}

	public boolean getIsAutoScroll() {
		return mIsAutoScroll;
	}

	public void setIsAutoScroll(boolean autoScroll) {
		mIsAutoScroll = autoScroll;
	}
}


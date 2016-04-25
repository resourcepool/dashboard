package com.excilys.shoofleurs.dashboard.entities;

import com.excilys.shoofleurs.dashboard.json.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * WebContent is a website to show. It adds to extra property.
 */
@Entity(name = "web")
public class WebContent extends AbstractContent {

	/**
	 * Duration display, by default 20.
	 */
	@Column(name = "duration_in_slideshow")
	@JsonProperty("durationInSlideShow")
	@JsonView(Views.FullContent.class)
	private int mDurationInSlideShow = 20;

	/**
	 * If the website doesn't fit into the screen, enable auto scroll
	 * to the the page. Default set to true.
	 */
	@Column(name = "auto_scroll")
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

	public int getDurationInSlideShow() {
		return mDurationInSlideShow;
	}

	public void setDurationInSlideShow(int durationInSlideshow) {
		mDurationInSlideShow = durationInSlideshow;
	}

	public boolean getIsAutoScroll() {
		return mIsAutoScroll;
	}

	public void setAutoScroll(boolean autoScroll) {
		mIsAutoScroll = autoScroll;
	}
}

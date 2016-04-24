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
	@JsonProperty("durationInSlideshow")
	@JsonView(Views.FullContent.class)
	private int mDurationInSlideshow = 20;

	/**
	 * If the website doesn't fit into the screen, enable auto scroll
	 * to the the page. Default set to true.
	 */
	@Column(name = "auto_scroll")
	@JsonProperty("autoScroll")
	@JsonView(Views.FullContent.class)
	private boolean mAutoScroll = true;


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
		return mDurationInSlideshow;
	}

	public void setDurationInSlideshow(int durationInSlideshow) {
		mDurationInSlideshow = durationInSlideshow;
	}

	public boolean isAutoScroll() {
		return mAutoScroll;
	}

	public void setAutoScroll(boolean autoScroll) {
		mAutoScroll = autoScroll;
	}
}

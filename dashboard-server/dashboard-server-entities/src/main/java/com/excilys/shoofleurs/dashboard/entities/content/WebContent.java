package com.excilys.shoofleurs.dashboard.entities.content;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	@Column
	@JsonProperty
	private int durationInSlideshow = 20;

	/**
	 * If the website doesn't fit into the screen, enable auto scroll
	 * to the the page. Default set to true.
	 */
	@Column
	@JsonProperty
	private boolean isAutoScroll = true;


	public WebContent() {
		super();
	}

	public WebContent(String title) {
		super(title);
	}

	public WebContent(String title, String url) {
		super(title, url);
	}

	public int getdurationinslideshow() {
		return durationInSlideshow;
	}

	public void setdurationinslideshow(int durationinslideshow) {
		this.durationInSlideshow = durationinslideshow;
	}

	public boolean getIsAutoScroll() {
		return isAutoScroll;
	}

	public void setAutoScroll(boolean autoScroll) {
		isAutoScroll = autoScroll;
	}
}

package com.excilys.shoofleurs.dashboard.entities.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Image content extends AbstractContent. This class adds a property, the duration display.
 */
@Entity(name = "image")
public class ImageContent extends AbstractContent {

	/**
	 * This field is the duration display. Must be > 0, if not precise,
	 * the default value is 5.
	 */
	@Column
	@JsonProperty
	private int durationInSlideshow = 5;


	public ImageContent() {
		super();
	}

	public ImageContent(String title) {
		super(title);
	}

	public ImageContent(String title, String url) {
		super(title, url);
	}


	public int getDurationInSlideshow() {
		return durationInSlideshow;
	}

	public void setDurationInSlideshow(int durationinslideshow) {
		durationInSlideshow = durationinslideshow;
	}
}

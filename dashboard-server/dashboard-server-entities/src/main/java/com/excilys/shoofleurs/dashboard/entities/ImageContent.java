package com.excilys.shoofleurs.dashboard.entities;

import com.excilys.shoofleurs.dashboard.json.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "image")
public class ImageContent extends AbstractContent {

	@Column(name = "duration_in_slideshow")
	@JsonProperty("durationInSlideShow")
	@JsonView(Views.FullContent.class)
	private int mDurationInSlideShow;

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

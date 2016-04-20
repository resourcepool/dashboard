package com.excilys.shoofleurs.dashboard.entities;

import com.excilys.shoofleurs.dashboard.json.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "web")
public class WebContent extends AbstractContent {

	@Column(name = "duration_in_diaporama")
	@JsonProperty("durationInDiaporama")
	@JsonView(Views.FullContent.class)
	private int mDurationInDiaporama;

	@Column(name = "auto_scroll")
	@JsonProperty("autoScroll")
	@JsonView(Views.FullContent.class)
	private boolean mAutoScroll;


	public WebContent() {
		super();
	}

	public WebContent(String title) {
		super(title);
	}

	public WebContent(String title, String url) {
		super(title, url);
	}

	public int getDurationInDiaporama() {
		return mDurationInDiaporama;
	}

	public void setDurationInDiaporama(int durationInDiaporama) {
		mDurationInDiaporama = durationInDiaporama;
	}

	public boolean isAutoScroll() {
		return mAutoScroll;
	}

	public void setAutoScroll(boolean autoScroll) {
		mAutoScroll = autoScroll;
	}
}

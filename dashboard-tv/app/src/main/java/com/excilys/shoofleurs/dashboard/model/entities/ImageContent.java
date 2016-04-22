package com.excilys.shoofleurs.dashboard.model.entities;

import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

public class ImageContent extends AbstractContent {

	@JsonView(Views.FullContent.class)
	@JsonProperty("durationInDiaporama")
	private int mDurationInDiaporama;

	public ImageContent() {
		super();
	}

	public ImageContent(String title) {
		super(title);
	}

	public ImageContent(String title, String url) {
		super(title, url);
	}

	public int getDurationInDiaporama() {
		return mDurationInDiaporama;
	}

	public void setDurationInDiaporama(int durationInDiaporama) {
		mDurationInDiaporama = durationInDiaporama;
	}
}

package com.excilys.shoofleurs.dashboard.model.entities;

import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class WebContent extends AbstractContent {
	@JsonView(Views.FullContent.class)
	private int mDurationInDiaporama;

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

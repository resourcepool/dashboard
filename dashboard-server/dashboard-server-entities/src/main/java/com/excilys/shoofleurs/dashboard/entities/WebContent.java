package com.excilys.shoofleurs.dashboard.entities;


public class WebContent extends AbstractContent {

	private int mDurationInDiaporama;

	private boolean mAutoScroll;


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

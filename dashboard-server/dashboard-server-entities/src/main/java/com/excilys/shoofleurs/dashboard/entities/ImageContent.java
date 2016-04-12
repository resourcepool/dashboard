package com.excilys.shoofleurs.dashboard.entities;


public class ImageContent extends AbstractContent {

	private int mDurationInDiaporama;


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

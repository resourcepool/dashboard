package com.excilys.shoofleurs.dashboard.entities;


public class PdfContent extends AbstractContent {

	private int mDurationPerPage;


	public PdfContent(String title) {
		super(title);
	}

	public PdfContent(String title, String url) {
		super(title, url);
	}

	public int getDurationPerPage() {
		return mDurationPerPage;
	}

	public void setDurationPerPage(int durationPerPage) {
		mDurationPerPage = durationPerPage;
	}
}

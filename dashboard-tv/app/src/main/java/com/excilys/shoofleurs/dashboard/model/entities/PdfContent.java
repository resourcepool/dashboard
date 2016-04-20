package com.excilys.shoofleurs.dashboard.model.entities;


import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class PdfContent extends AbstractContent {

	@JsonView(Views.FullContent.class)
	private int mDurationPerPage;

	public PdfContent() {
		super();
	}

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

package com.excilys.shoofleurs.dashboard.model.entities;

import com.excilys.shoofleurs.dashboard.json.Views;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "pdf")
public class PdfContent extends AbstractContent {

	@Column(name = "page_duration")
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

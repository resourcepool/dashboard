package com.excilys.shoofleurs.dashboard.entities.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Represents a PDF file with an extra property.
 */
@Entity
public class PdfContent extends AbstractContent {

	/**
	 * Duration display of each page. By default, 10.
	 */
	@Column
	@JsonProperty
	private int durationPerPage = 10;

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
		return durationPerPage;
	}

	public void setDurationPerPage(int durationPerPage) {
		this.durationPerPage = durationPerPage;
	}
}

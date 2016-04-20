package com.excilys.shoofleurs.dashboard.model.entities;

import com.excilys.shoofleurs.dashboard.json.Views;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "image")
public class ImageContent extends AbstractContent {

	@Column(name = "duration_in_diaporama")
	@JsonView(Views.FullContent.class)
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

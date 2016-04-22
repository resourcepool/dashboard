package com.excilys.shoofleurs.dashboard.entities;


import javax.persistence.Entity;

/**
 * Video content. No extra property.
 */
@Entity(name = "video")
public class VideoContent extends AbstractContent {

	public VideoContent() {
		super();
	}

	public VideoContent(String title) {
		super(title);
	}

	public VideoContent(String title, String url) {
		super(title, url);
	}
}

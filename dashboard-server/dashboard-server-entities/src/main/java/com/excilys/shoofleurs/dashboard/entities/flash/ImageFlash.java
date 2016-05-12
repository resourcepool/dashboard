package com.excilys.shoofleurs.dashboard.entities.flash;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "image_flash")
public class ImageFlash extends Flash {


	@Column(name = "url")
	@JsonProperty("url")
	private String mUrl;

	public ImageFlash() { }

	public ImageFlash(String url) {
		mUrl = url;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}
}

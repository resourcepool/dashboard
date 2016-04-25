package com.excilys.shoofleurs.dashboard.entities.flash;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "image_flash")
public class ImageFlash {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "image_id")
	@JsonProperty("imageFlashId")
	private int mImageFlashId;

	@Column(name = "url")
	@JsonProperty("url")
	private String mUrl;

	public ImageFlash() { }

	public ImageFlash(String url) {
		mUrl = url;
	}

	public int getImageFlashId() {
		return mImageFlashId;
	}

	public void setImageFlashId(int imageFlashId) {
		mImageFlashId = imageFlashId;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}
}

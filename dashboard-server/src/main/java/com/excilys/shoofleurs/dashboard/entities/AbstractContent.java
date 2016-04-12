package com.excilys.shoofleurs.dashboard.entities;


public abstract class AbstractContent {


	private int mId;

	private String mTitle;

	private String mUrl;

	private int mGlobalDuration;


	public AbstractContent(String title) {
		mTitle = title;
	}

	public AbstractContent(String title, String url) {
		this(title);
		mUrl = url;
	}


	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public int getGlobalDuration() {
		return mGlobalDuration;
	}

	public void setGlobalDuration(int globalDuration) {
		mGlobalDuration = globalDuration;
	}
}

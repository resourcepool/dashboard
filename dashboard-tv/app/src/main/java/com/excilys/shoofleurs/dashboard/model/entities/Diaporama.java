package com.excilys.shoofleurs.dashboard.model.entities;


import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

public class Diaporama {

	@JsonProperty("id")
	@JsonView(Views.LightContent.class)
	private int mId;

	@JsonProperty("title")
	@JsonView(Views.LightContent.class)
	private String mTitle;

	@JsonProperty("start")
	@JsonView(Views.FullContent.class)
	private String mStartDateTime;

	@JsonProperty("end")
	@JsonView(Views.FullContent.class)
	private String mEndDateTime;

	@JsonProperty("contents")
	@JsonView(Views.TvContent.class)
	private List<AbstractContent> mContents = new ArrayList<>();

	public Diaporama() {

	}

	public Diaporama(String title, String startDateTime, String endDateTime) {
		mTitle = title;
		mStartDateTime = startDateTime;
		mEndDateTime = endDateTime;
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

	public String getStartDateTime() {
		return mStartDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		mStartDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return mEndDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		mEndDateTime = endDateTime;
	}

	public List<AbstractContent> getContents() {
		return mContents;
	}

	public void setContents(List<AbstractContent> contents) {
		mContents = contents;
	}

	@Override
	public String toString() {
		return "Diaporama{" +
				"mId=" + mId +
				", mTitle='" + mTitle + '\'' +
				", mStartDateTime='" + mStartDateTime + '\'' +
				", mEndDateTime='" + mEndDateTime + '\'' +
				", mContents=" + mContents +
				'}';
	}
}
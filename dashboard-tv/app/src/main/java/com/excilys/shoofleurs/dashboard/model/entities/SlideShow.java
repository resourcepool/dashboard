package com.excilys.shoofleurs.dashboard.model.entities;


import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

/**
 * A slideshow contains a list of content to display. It has a start date time and an end date time.
 * By default, if date are not set, the startDateTime is now, and the end date time is the date very
 * very far away ! Unless an other slideshow is set to start after, a slideshow with a default endDateTime
 * never stop.
 */
public class SlideShow {

	@JsonProperty("id")
	@JsonView(Views.LightContent.class)
	private int mId;

	@JsonProperty("title")
	@JsonView(Views.LightContent.class)
	private String mTitle;

	@JsonProperty("startDateTime")
	@JsonView(Views.FullContent.class)
	private String mStartDateTime;

	@JsonProperty("endDateTime")
	@JsonView(Views.FullContent.class)
	private String mEndDateTime;

	@JsonProperty("contents")
	@JsonView(Views.TvContent.class)
	@JsonManagedReference
	private List<AbstractContent> mContents = new ArrayList<>();

	public SlideShow() {

	}

	public SlideShow(String title, String startDateTime, String endDateTime) {
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

	public void addContent(AbstractContent content) {
		mContents.add(content);
	}

	@Override
	public String toString() {
		return "SlideShow{" +
				"mId=" + mId +
				", mTitle='" + mTitle + '\'' +
				", mStartDateTime='" + mStartDateTime + '\'' +
				", mEndDateTime='" + mEndDateTime + '\'' +
				", mContents=" + mContents +
				'}';
	}
}


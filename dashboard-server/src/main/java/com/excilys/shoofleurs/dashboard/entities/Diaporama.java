package com.excilys.shoofleurs.dashboard.entities;


import java.time.LocalDateTime;

public class Diaporama {

	private int mId;

	private String mTitle;

	private LocalDateTime mStartDateTime;

	private LocalDateTime mEndDateTime;

	public Diaporama(String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
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

	public LocalDateTime getStartDateTime() {
		return mStartDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		mStartDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return mEndDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		mEndDateTime = endDateTime;
	}
}

package com.excilys.shoofleurs.dashboard.entities;



import com.excilys.shoofleurs.dashboard.json.Views;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "diaporama")
public class Diaporama {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "diaporama_id")
	@JsonProperty("id")
	@JsonView(Views.LightContent.class)
	private int mId;

	@Column(name = "title")
	@JsonProperty("title")
	@JsonView(Views.LightContent.class)
	private String mTitle;

	@Column(name = "start")
	@JsonProperty("start")
	@JsonView(Views.FullContent.class)
	private LocalDateTime mStartDateTime;

	@Column(name = "end")
	@JsonProperty("end")
	@JsonView(Views.FullContent.class)
	private LocalDateTime mEndDateTime;

	public Diaporama() {

	}

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

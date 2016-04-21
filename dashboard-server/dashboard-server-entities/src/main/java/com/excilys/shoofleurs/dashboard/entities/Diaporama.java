package com.excilys.shoofleurs.dashboard.entities;



import com.excilys.shoofleurs.dashboard.json.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "diaporama")
@NamedQueries({@NamedQuery(name = "diaporamas.findAll", query = "Select d FROM diaporama d")})
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
	@JsonProperty("startDateTime")
	@JsonView(Views.FullContent.class)
	private String mStartDateTime;

	@Column(name = "end")
	@JsonProperty("endDateTime")
	@JsonView(Views.FullContent.class)
	private String mEndDateTime;

	@OneToMany(mappedBy = "mDiaporama", cascade = CascadeType.ALL)
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
}

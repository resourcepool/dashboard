package com.excilys.shoofleurs.dashboard.entities;


import com.excilys.shoofleurs.dashboard.json.Views;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;

@Entity(name = "abstract_content")
@NamedQuery(name = "findAll", query = "SELECT a FROM abstract_content a")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractContent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "content_id")
	@JsonView(Views.LightContent.class)
	private int mId;

	@Column(name = "title")
	@JsonView(Views.LightContent.class)
	private String mTitle;

	@Column(name = "url")
	@JsonView(Views.LightContent.class)
	private String mUrl;

	@JsonView(Views.FullContent.class)
	@Column(name = "global_duration")
	private int mGlobalDuration;

	public AbstractContent() {

	}

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

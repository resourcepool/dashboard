package com.excilys.shoofleurs.dashboard.entities;


import com.excilys.shoofleurs.dashboard.json.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity(name = "abstract_content")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({@JsonSubTypes.Type(value = ImageContent.class, name = "ImageContent")})
public abstract class AbstractContent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "content_id")
	@JsonProperty("id")
	@JsonView(Views.LightContent.class)
	private int mId;

	@Column(name = "title")
	@JsonProperty("title")
	@JsonView(Views.LightContent.class)
	private String mTitle;

	@Column(name = "url")
	@JsonProperty("url")
	@JsonView(Views.LightContent.class)
	private String mUrl;

	@Column(name = "global_duration")
	@JsonProperty("globalDuration")
	@JsonView(Views.FullContent.class)
	private int mGlobalDuration;

	@ManyToOne
	@JoinColumn(name = "diaporama_id")
	@JsonProperty("diaporama")
	@JsonBackReference
	private Diaporama mDiaporama;

	public AbstractContent() { }

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

	public Diaporama getDiaporama() {
		return mDiaporama;
	}

	public void setDiaporama(Diaporama diaporama) {
		mDiaporama = diaporama;
	}
}

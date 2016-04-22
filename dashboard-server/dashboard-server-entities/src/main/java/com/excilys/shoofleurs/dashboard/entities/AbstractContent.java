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


/**
 * This class represents an abstract content with commons properties between content.
 * A content can be an image, a video, a pdf, or a web page. A content is associated
 * with a slideshow. It can only exist with a slideshow.
 */
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

	/**
	 * Global duration is the time of life of the content. If the slideshow is displaying all
	 * day, a content can be display less time. Set to 0 by default, it means the content lives
	 * the total duration of the slideshow.
	 */
	@Column(name = "global_duration")
	@JsonProperty("globalDuration")
	@JsonView(Views.FullContent.class)
	private int mGlobalDuration;

	@ManyToOne
	@JoinColumn(name = "slideshow_id")
	@JsonProperty("slideShow")
	@JsonBackReference
	private SlideShow mSlideShow;

	/**
	 * Default constructor for Jersey/JPA.
	 */
	public AbstractContent() { }

	/**
	 * Construct a content object with a title.
	 * @param title Content's title
	 */
	public AbstractContent(String title) {
		mTitle = title;
	}

	/**
	 * Construct a content object with a title and an access url.
	 * @param title Content's title
	 * @param url Access url used by the app for download the content
	 */
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

	public SlideShow getSlideShow() {
		return mSlideShow;
	}

	public void setSlideShow(SlideShow slideShow) {
		mSlideShow = slideShow;
	}
}

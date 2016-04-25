package com.excilys.shoofleurs.dashboard.model.entities;


import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * This class represents an abstract content with commons properties between content.
 * A content can be an image, a video, a pdf, or a web page. A content is associated
 * with a slideshow. It can only exist with a slideshow.
 */
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE,
		isGetterVisibility=JsonAutoDetect.Visibility.NONE, setterVisibility= JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = ImageContent.class, name = "ImageContent"),
		@JsonSubTypes.Type(value = WebContent.class, name = "WebContent"),
		@JsonSubTypes.Type(value = PdfContent.class, name = "PdfContent"),
		@JsonSubTypes.Type(value = VideoContent.class, name = "VideoContent"),
})
public abstract class AbstractContent {
	@JsonProperty("id")
	@JsonView(Views.LightContent.class)
	private int mId;

	@JsonProperty("title")
	@JsonView(Views.LightContent.class)
	private String mTitle;

	@JsonProperty("url")
	@JsonView(Views.LightContent.class)
	private String mUrl;

	/**
	 * Global duration is the time of life of the content. If the slideshow is displaying all
	 * day, a content can be display less time. Set to 0 by default, it means the content lives
	 * the total duration of the slideshow.
	 */
	@JsonProperty("globalDuration")
	@JsonView(Views.FullContent.class)
	private int mGlobalDuration;

	@JsonProperty("slideShow")
	@JsonBackReference
	private SlideShow mSlideShow;

	@JsonProperty("positionInSlideShow")
	private int mPositionInSlideShow;

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

	public int getPositionInSlideShow() {
		return mPositionInSlideShow;
	}

	public void setPositionInSlideShow(int mPositionInSlideShow) {
		this.mPositionInSlideShow = mPositionInSlideShow;
	}

	@Override
	public String toString() {
		return "AbstractContent{" +
				"mId=" + mId +
				", mTitle='" + mTitle + '\'' +
				", mUrl='" + mUrl + '\'' +
				", mGlobalDuration=" + mGlobalDuration +
				'}';
	}
}
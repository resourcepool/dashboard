package com.excilys.shoofleurs.dashboard.model.entities;


import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;

@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE,
		isGetterVisibility=JsonAutoDetect.Visibility.NONE, setterVisibility= JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
		include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = ImageContent.class, name = "ImageContent")
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

	@JsonProperty("globalDuration")
	@JsonView(Views.FullContent.class)
	private int mGlobalDuration;

	private Diaporama mDiaporama;

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

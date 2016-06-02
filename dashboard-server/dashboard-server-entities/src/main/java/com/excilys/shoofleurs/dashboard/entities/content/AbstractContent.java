package com.excilys.shoofleurs.dashboard.entities.content;


import com.excilys.shoofleurs.dashboard.entities.Slideshow;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@JsonSubTypes({
		@JsonSubTypes.Type(value = ImageContent.class, name = "ImageContent"),
		@JsonSubTypes.Type(value = WebContent.class, name = "WebContent"),
		@JsonSubTypes.Type(value = PdfContent.class, name = "PdfContent"),
		@JsonSubTypes.Type(value = VideoContent.class, name = "VideoContent"),
})
public abstract class AbstractContent {

	@Id
	@GeneratedValue
	@JsonProperty
	private int id;

	@JsonProperty
	private String title;

	@Column
	@JsonProperty
	private String url;

	/**
	 * Global duration is the time of life of the content. If the slideshow is displaying all
	 * day, a content can be display less time. Set to 0 by default, it means the content lives
	 * the total duration of the slideshow.
	 */
	@Column
	@JsonProperty
	private int globalDuration;

	@ManyToOne
	@JoinColumn(name = "slideshow_id")
	@JsonProperty
	@JsonBackReference
	private Slideshow slideshow;

	@Column
	@JsonProperty
	private int positionInSlideshow;


	public AbstractContent() { }


	public AbstractContent(String title) {
		this.title = title;
	}


	public AbstractContent(String title, String url) {
		this(title);
		this.url = url;
	}


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AbstractContent content = (AbstractContent) obj;
        return id == content.getId();
    }


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getGlobalDuration() {
		return globalDuration;
	}

	public void setGlobalDuration(int globalDuration) {
		this.globalDuration = globalDuration;
	}

	public Slideshow getSlideshow() {
		return slideshow;
	}

	public void setSlideshow(Slideshow slideshow) {
		this.slideshow = slideshow;
	}

	public int getPositionInSlideShow() {
		return positionInSlideshow;
	}

	public void setPositionInSlideShow(int positionInSlideShow) {
		positionInSlideshow = positionInSlideShow;
	}
}

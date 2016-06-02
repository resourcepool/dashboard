package com.excilys.shoofleurs.dashboard.entities;


import com.excilys.shoofleurs.dashboard.entities.content.AbstractContent;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


/**
 * A slideshow contains a list of content to display. It has a start date time and an end date time.
 * By default, if date are not set, the startDateTime is now, and the end date time is the date very
 * very far away ! Unless an other slideshow is set to start after, a slideshow with a default endDateTime
 * never stop.
 */
@Entity(name = "slideshow")
public class Slideshow {

	@Id
	@GeneratedValue
	@Column
	@JsonProperty
	private int id;

	@Column
	@JsonProperty
	private String title;

	@Column
	@JsonProperty
	private String startDateTime;

	@Column
	@JsonProperty
	private String endDateTime;

	@OneToMany(mappedBy = "slideshow", cascade = CascadeType.ALL)
	@JsonProperty
	@JsonManagedReference
	private List<AbstractContent> contents = new ArrayList<>();


	public Slideshow() {

	}

	public Slideshow(String title, String startDateTime, String endDateTime) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
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

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public List<AbstractContent> getContents() {
		return contents;
	}

	public void setContents(List<AbstractContent> contents) {
		this.contents = contents;
	}

	public void addContent(AbstractContent content) {
		contents.add(content);
	}
}

package com.excilys.shoofleurs.dashboard.entities.notif;


import com.excilys.shoofleurs.dashboard.entities.notif.enums.ObjectType;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue
	@Column
	@JsonProperty
	private int id;

	@Column
	@JsonProperty
	private ObjectType objectType;

	@Column(name = "object_id")
	@JsonProperty("objectId")
	private int objectId;


	public Notification() { }

	public Notification(ObjectType objectType, int objectId) {
		this.objectType = objectType;
		this.objectId = objectId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
}

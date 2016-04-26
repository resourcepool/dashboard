package com.excilys.shoofleurs.dashboard.entities.notif;


import com.excilys.shoofleurs.dashboard.entities.notif.enums.ObjectType;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity(name = "notifications")
@NamedQueries({
		@NamedQuery(name = "findBySlideShowId", query = "SELECT n FROM notifications n WHERE n.mObjectId = :id"),
		@NamedQuery(name = "findAll.notifications", query = "SELECT n FROM notifications n")
})
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "notification_id")
	@JsonProperty("id")
	private int mId;

	@Column(name = "object_type")
	@JsonProperty("objectType")
	private ObjectType mObjectType;

	@Column(name = "object_id")
	@JsonProperty("objectId")
	private int mObjectId;


	public Notification() { }

	public Notification(ObjectType objectType, int objectId) {
		mObjectType = objectType;
		mObjectId = objectId;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public ObjectType getObjectType() {
		return mObjectType;
	}

	public void setObjectType(ObjectType objectType) {
		mObjectType = objectType;
	}

	public int getObjectId() {
		return mObjectId;
	}

	public void setObjectId(int objectId) {
		mObjectId = objectId;
	}
}

package com.excilys.shoofleurs.dashboard.entities.notif;


import com.excilys.shoofleurs.dashboard.entities.notif.enums.Operation;
import com.excilys.shoofleurs.dashboard.entities.notif.enums.ObjectType;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "notification_id")
	@JsonProperty("id")
	private int mId;

	@Column(name = "operation")
	@JsonProperty("operation")
	private Operation mOperation;

	@Column(name = "object_type")
	@JsonProperty("objectType")
	private ObjectType mObjectType;

	@Column(name = "object_id")
	@JsonProperty("objectId")
	private int mObjectId;

	@Column(name = "associateObjectId")
	@JsonProperty("associateObjectId")
	private int mAssociateObjectId;

	public Notification() { }

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public Operation getOperation() {
		return mOperation;
	}

	public void setOperation(Operation operation) {
		mOperation = operation;
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

	public int getAssociateObjectId() {
		return mAssociateObjectId;
	}

	public void setAssociateObjectId(int associateObjectId) {
		mAssociateObjectId = associateObjectId;
	}
}

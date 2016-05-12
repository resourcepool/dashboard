package com.excilys.shoofleurs.dashboard.entities.flash;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = MessageFlash.class, name = "MessageFlash"),
		@JsonSubTypes.Type(value = ImageFlash.class, name = "ImageFlash")
})
public abstract class Flash {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "flashId")
	@JsonProperty("flashId")
	private int mFlashId;

	public int getFlashId() {
		return mFlashId;
	}

	public void setFlashId(int flashId) {
		mFlashId = flashId;
	}
}

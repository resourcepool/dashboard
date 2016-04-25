package com.excilys.shoofleurs.dashboard.entities.flash;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "message_flash")
public class MessageFlash {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "message_id")
	@JsonProperty("messageFlashId")
	private int mMessageFlashId;

	@Column(name = "messageFlash")
	@JsonProperty("messageFlash")
	private String mMessageFlash;

	public MessageFlash() {
	}

	public MessageFlash(String messageFlash) {
		mMessageFlash = messageFlash;
	}

	public int getMessageFlashId() {
		return mMessageFlashId;
	}

	public void setMessageFlashId(int messageFlashId) {
		mMessageFlashId = messageFlashId;
	}

	public String getMessageFlash() {
		return mMessageFlash;
	}

	public void setMessageFlash(String messageFlash) {
		mMessageFlash = messageFlash;
	}
}

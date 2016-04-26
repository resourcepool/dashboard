package com.excilys.shoofleurs.dashboard.entities.flash;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "message_flash")
public class MessageFlash extends Flash {

	@Column(name = "messageFlash")
	@JsonProperty("messageFlash")
	private String mMessageFlash;

	public MessageFlash() {
	}

	public MessageFlash(String messageFlash) {
		mMessageFlash = messageFlash;
	}

	public String getMessageFlash() {
		return mMessageFlash;
	}

	public void setMessageFlash(String messageFlash) {
		mMessageFlash = messageFlash;
	}
}

package com.excilys.shoofleurs.dashboard.entities.flash;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "message_flash")
public class MessageFlash extends Flash {

	@Column
	@JsonProperty
	private String messageFlash;

	public MessageFlash() {
	}

	public MessageFlash(String messageFlash) {
		this.messageFlash = messageFlash;
	}

	public String getMessageFlash() {
		return messageFlash;
	}

	public void setMessageFlash(String messageFlash) {
		this.messageFlash = messageFlash;
	}
}

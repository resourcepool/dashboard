package com.excilys.shoofleurs.dashboard.webapp.rest.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response is used like a wrapper. It contains the json object or the error and a info code
 * to send to the client.
 */
public class Response {

	@JsonProperty("objectAsJson")
	private String mObjectAsJson;

	@JsonProperty("infoCode")
	private int mInfoCode;


	public Response(String objectAsJson, int errorCode) {
		mObjectAsJson = objectAsJson;
		mInfoCode = errorCode;
	}

	public String getObjectAsJson() {
		return mObjectAsJson;
	}

	public void setObjectAsJson(String objectAsJson) {
		mObjectAsJson = objectAsJson;
	}

	public int getInfoCode() {
		return mInfoCode;
	}

	public void setInfoCode(int infoCode) {
		mInfoCode = infoCode;
	}
}

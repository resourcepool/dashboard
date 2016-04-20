package com.excilys.shoofleurs.dashboard.webapp.rest.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("response")
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

package com.excilys.shoofleurs.dashboard.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("response")
public class ServerResponse {
	@JsonProperty("objectAsJson")
	private String mObjectAsJson;

	@JsonProperty("infoCode")
	private int mInfoCode;

	public ServerResponse() {}

	public ServerResponse(String objectAsJson, int errorCode) {
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

	@Override
	public String toString() {
		return "ServerResponse{" +
				"mObjectAsJson='" + mObjectAsJson + '\'' +
				", mInfoCode=" + mInfoCode +
				'}';
	}
}

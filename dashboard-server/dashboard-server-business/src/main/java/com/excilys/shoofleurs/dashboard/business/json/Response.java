package com.excilys.shoofleurs.dashboard.business.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("response")
public class Response {

	@JsonProperty("object")
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

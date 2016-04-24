package com.excilys.shoofleurs.dashboard.webapp.rest.utils;


import com.excilys.shoofleurs.dashboard.json.Views;

/**
 * Valid REST param.
 */
public class ParamValidator {

	/**
	 * Valid and get the value of the type of jsonview to return.
	 * @param jsonView Type of jsonview as string
	 * @return JsonView as class (used by the object mapper)
	 */
	public static Class getJsonView(String jsonView) {
		if (jsonView == null) {
			return Views.LightContent.class;
		}
		Class jsonType;
		switch(jsonView){
			case "tv":
				jsonType = Views.TvContent.class;
				break;
			case "full":
				jsonType = Views.FullContent.class;
				break;
			default:
				jsonType = Views.LightContent.class;
				break;
		}
		return jsonType;
	}

	public static int superiorOrEqualsAs(int valueToTest, int condition, int defaultValue) {
		if (valueToTest >= condition) {
			valueToTest = defaultValue;
		}
		return valueToTest;
	}

	public static int inferiorOrEqualsAs(int valueToTest, int condition, int defaultValue) {
		if (valueToTest <= condition) {
			valueToTest = defaultValue;
		}
		return valueToTest;
	}
}

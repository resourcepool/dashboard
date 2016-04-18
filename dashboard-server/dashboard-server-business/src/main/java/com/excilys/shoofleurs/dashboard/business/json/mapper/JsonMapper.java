package com.excilys.shoofleurs.dashboard.business.json.mapper;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;


public class JsonMapper {

	public static String objectAsJson(Object object, Class viewsClass) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
		String objectAsJson = null;
		try {
			objectAsJson = objectMapper.writerWithView(viewsClass).writeValueAsString(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objectAsJson;
	}
}

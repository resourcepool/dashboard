package com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper;



import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class JsonMapper {

	public static String objectAsJson(Object object, Class viewsClass) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		String objectAsJson = null;
		try {
			objectAsJson = objectMapper.writerWithView(viewsClass).writeValueAsString(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objectAsJson;
	}
}

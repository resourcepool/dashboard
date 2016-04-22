package com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper;

import com.excilys.shoofleurs.dashboard.entities.AbstractContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Map object to json and json to object.
 */
public class JsonMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class.getSimpleName());

	/**
	 * Get a json string from an object.
	 * @param object Object to transform
	 * @param viewsClass Indicates fields to serialize or not
	 * @return JSON of the object
	 */
	public static String objectAsJson(Object object, Class viewsClass) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		String objectAsJson = null;
		try {
			objectAsJson = objectMapper.writerWithView(viewsClass).writeValueAsString(object);
		} catch (IOException e) {
			LOGGER.error("Error during parsing to json. Caused by : " + e.getMessage());
		}
		return objectAsJson;
	}

	/**
	 * Get an abstract content from json string.
	 * @param objectAsJson Abstract content.
	 * @return Abstract content from JSON.
	 */
	public static AbstractContent jsonAsAbstractContent(String objectAsJson) {
		ObjectMapper objectMapper = new ObjectMapper();
		AbstractContent object = null;
		try {
			object = objectMapper.readValue(objectAsJson, AbstractContent.class);
		} catch (IOException e) {
			LOGGER.error("Error during parsing to object. Caused by : " + e.getMessage());
		}
		return object;
	}
}

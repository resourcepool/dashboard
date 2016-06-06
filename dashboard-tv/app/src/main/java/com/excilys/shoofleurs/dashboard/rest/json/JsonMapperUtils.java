package com.excilys.shoofleurs.dashboard.rest.json;

import com.excilys.shoofleurs.dashboard.model.json.ServerResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by tommy on 10/05/16.
 */
public class JsonMapperUtils {
    private static ObjectMapper sObjectMapper = new ObjectMapper();
    public static <T> T getServerResponseContent(ServerResponse serverResponse, Class<T> clazz) {
        if (serverResponse != null) {
            try {
                return sObjectMapper.readValue(serverResponse.getObjectAsJson(), clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T getServerResponseContent(ServerResponse serverResponse, TypeReference<?> typeReference) {
        if (serverResponse != null) {
            try {
                return sObjectMapper.readValue(serverResponse.getObjectAsJson(), typeReference);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

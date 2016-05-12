package excilys.dashboardadministrator.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import excilys.dashboardadministrator.model.json.ServerResponse;

public class JsonMapperUtils {
    private static ObjectMapper sObjectMapper = new ObjectMapper();
    public static <T> T getServerResponseContent(ServerResponse serverResponse, Class<T> clazz) {
        if (serverResponse != null && serverResponse.getInfoCode() == 200) {
            try {
                return sObjectMapper.readValue(serverResponse.getObjectAsJson(), clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T getServerResponseContent(ServerResponse serverResponse, TypeReference<?> typeReference) {
        if (serverResponse != null && serverResponse.getInfoCode() == 200) {
            try {
                return sObjectMapper.readValue(serverResponse.getObjectAsJson(), typeReference);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String writeValueAsString(Object object) {
        try {
            return sObjectMapper.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }
}

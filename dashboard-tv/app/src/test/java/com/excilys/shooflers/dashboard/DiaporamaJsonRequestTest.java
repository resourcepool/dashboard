package com.excilys.shooflers.dashboard;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.Diaporama;
import com.excilys.shoofleurs.dashboard.model.entities.ImageContent;
import com.excilys.shoofleurs.dashboard.model.json.ServerResponse;
import com.excilys.shoofleurs.dashboard.requests.JsonRequest;
import com.excilys.shoofleurs.dashboard.singletons.VolleySingleton;
import com.excilys.shoofleurs.dashboard.utils.Data;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tommy on 20/04/16.
 */
public class DiaporamaJsonRequestTest {
    @Test
    public void parseDiaporamaFromRemoteRequest() throws Exception {
        String responseJson = "{\"objectAsJson\":\"[{\\\"contents\\\":[{\\\"durationInDiaporama\\\":100,\\\"globalDuration\\\":200,\\\"title\\\":\\\"title\\\",\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard-server-webapp-1.0-SNAPSHOT/img/img.jpg\\\",\\\"id\\\":52}],\\\"startDateTime\\\":\\\"01-01-2001\\\",\\\"endDateTime\\\":\\\"02-02-2002\\\",\\\"title\\\":\\\"title\\\",\\\"id\\\":51}]\",\"infoCode\":200}";
        ObjectMapper objectMapper = new ObjectMapper();
        ServerResponse response = objectMapper.readValue(responseJson, ServerResponse.class);
        assertNotNull(response);
        System.out.println(response);

        Diaporama[] diaporamas = objectMapper.readValue(response.getObjectAsJson(), Diaporama[].class);

        System.out.println(diaporamas);
    }

    @Test
    public void parseDiaporamaFromLocal() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Diaporama diaporama = new Diaporama("Diaporama1", "01-01-2001", "01-01-2001");
        ImageContent imageContent = new ImageContent("title", "http://vps229493.ovh.net:8080/dashboard-server-webapp-1.0-SNAPSHOT/img/img.jpg");
        imageContent.setId(51);
        imageContent.setDurationInDiaporama(100);
        imageContent.setGlobalDuration(200);
        diaporama.getContents().add(imageContent);

        ServerResponse responseToParse = new ServerResponse(objectMapper.writeValueAsString(Arrays.asList(diaporama)), 200);

        String responseJson = objectMapper.writeValueAsString(responseToParse);

        System.out.println(responseJson);

        ServerResponse response = objectMapper.readValue(responseJson, ServerResponse.class);

        assertNotNull(response);
        System.out.println(response);

//        List<Diaporama> diaporamas = objectMapper.readValue(response.getObjectAsJson(), List.class);
        Diaporama[] diaporamas = objectMapper.readValue(response.getObjectAsJson(), Diaporama[].class);

        System.out.println(diaporamas[0].getContents().get(0).getClass().getSimpleName());
    }
}

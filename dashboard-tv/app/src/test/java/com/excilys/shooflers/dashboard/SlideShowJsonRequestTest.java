package com.excilys.shooflers.dashboard;

import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;
import com.excilys.shoofleurs.dashboard.model.entities.ImageContent;
import com.excilys.shoofleurs.dashboard.model.json.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SlideShowJsonRequestTest {
    @Test
    public void parseSlideShowFromRemoteRequest() throws Exception {
        String responseJson = "{\"objectAsJson\":\"[{\\\"contents\\\":[{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/7.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":7},{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/8.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":8},{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/9.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":9},{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/10.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":10},{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/11.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":11}],\\\"title\\\":\\\"La Folie Douce\\\",\\\"startDateTime\\\":\\\"21-04-2016 00:00:00\\\",\\\"endDateTime\\\":\\\"21-04-2018 00:00:00\\\",\\\"id\\\":1},{\\\"contents\\\":[],\\\"title\\\":\\\"Marseille\\\",\\\"startDateTime\\\":\\\"21-04-2016 00:00:00\\\",\\\"endDateTime\\\":\\\"21-04-2018 00:00:00\\\",\\\"id\\\":2},{\\\"contents\\\":[{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/15.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":15},{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/16.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":16},{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/17.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":17}],\\\"title\\\":\\\"Excilys\\\",\\\"startDateTime\\\":\\\"21-04-2016 00:00:00\\\",\\\"endDateTime\\\":\\\"21-04-2018 00:00:00\\\",\\\"id\\\":3},{\\\"contents\\\":[],\\\"title\\\":\\\"Paris\\\",\\\"startDateTime\\\":\\\"21-04-2016 00:00:00\\\",\\\"endDateTime\\\":\\\"21-04-2018 00:00:00\\\",\\\"id\\\":4},{\\\"contents\\\":[{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/12.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":12},{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/13.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":13},{\\\"@type\\\":\\\"ImageContent\\\",\\\"durationInSlideShow\\\":5,\\\"url\\\":\\\"http://vps229493.ovh.net:8080/dashboard/img/14.jpg\\\",\\\"title\\\":\\\"Titre\\\",\\\"globalDuration\\\":3000,\\\"id\\\":14}],\\\"title\\\":\\\"Nature\\\",\\\"startDateTime\\\":\\\"21-04-2016 00:00:00\\\",\\\"endDateTime\\\":\\\"21-04-2018 00:00:00\\\",\\\"id\\\":5},{\\\"contents\\\":[],\\\"title\\\":\\\"Paysage\\\",\\\"startDateTime\\\":\\\"21-04-2016 00:00:00\\\",\\\"endDateTime\\\":\\\"21-04-2018 00:00:00\\\",\\\"id\\\":6}]\",\"infoCode\":200}";
        ObjectMapper objectMapper = new ObjectMapper();
        ServerResponse response = objectMapper.readValue(responseJson, ServerResponse.class);
        assertNotNull(response);


        SlideShow[] slideShows = objectMapper.readValue(response.getObjectAsJson(), SlideShow[].class);
    }

    @Test
    public void parseSlideShowFromLocal() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        SlideShow slideShow = new SlideShow("SlideShow1", "01-01-2001", "01-01-2001");
        ImageContent imageContent = new ImageContent("title", "http://vps229493.ovh.net:8080/dashboard-server-webapp-1.0-SNAPSHOT/img/img.jpg");
        imageContent.setId(51);
        imageContent.setDurationInSlideShow(100);
        imageContent.setGlobalDuration(200);
        slideShow.getContents().add(imageContent);

        ServerResponse responseToParse = new ServerResponse(objectMapper.writeValueAsString(Arrays.asList(slideShow)), 200);

        String responseJson = objectMapper.writeValueAsString(responseToParse);

        System.out.println(responseJson);

        ServerResponse response = objectMapper.readValue(responseJson, ServerResponse.class);

        assertNotNull(response);
        System.out.println(response);

        SlideShow[] slideShows = objectMapper.readValue(response.getObjectAsJson(), SlideShow[].class);

        System.out.println(slideShows[0].getContents().get(0).getClass().getSimpleName());
    }
}

package io.resourcepool.dashboard.mediacontroller.edit;

import io.resourcepool.dashboard.dto.MediaMetadataDto;
import io.resourcepool.dashboard.mediacontroller.AbstractMediaControllerTest;
import io.resourcepool.dashboard.model.Media;
import io.resourcepool.dashboard.model.Revision;
import io.resourcepool.dashboard.model.metadata.MediaMetadata;
import io.resourcepool.dashboard.model.type.MediaType;
import io.resourcepool.dashboard.rest.MediaController;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author VIEGAS Mickael
 */
public class MediaControllerMediaWebEditTest extends AbstractMediaControllerTest {

    private MediaMetadata formMediaMetadata;

    private MediaMetadataDto formMediaMetadataDto;

    private final String name = "needToTest";
    private final String url = "http://www.perdu.com/";
    private final MediaType mediaType = MediaType.WEB;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();


        // Créer un bundle de base pour l'édition
        formMediaMetadata = MediaMetadata.builder()
                .name(name)
                .mediaType(mediaType)
                .url(url)
                .bundleTag(globalBundleMetadataDto.getTag())
                .build();

        Media media = Media.builder().metadata(formMediaMetadata).build();
        mediaService.save(media);

        formMediaMetadataDto = mediaDtoMapper.toDto(formMediaMetadata);

        assertNotNull(formMediaMetadata.getUuid());
    }

    @Test
    public void failedOnMediaWithUuidInUrl() throws Exception {
        mockMvc.perform(putAuthenticated("/media/ssdfsdf"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void failedFirstlyWithWrongDto() throws Exception {
        formMediaMetadataDto.setUuid("ezazae");
        mockMvc.perform(putAuthenticated("/media")
                .param("media", toJson(formMediaMetadataDto))
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void failedFirstlyWithWrongUuid() throws Exception {
        formMediaMetadataDto.setUuid("ezazae");
        mockMvc.perform(putAuthenticated("/media")
                .content(toJson(formMediaMetadataDto))
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void successNewUrl() throws Exception {
        final String newUrl = "http://newurl.com";
        assertNotEquals(url, newUrl);
        final long previousRevision = revisionService.getLatest();
        final long previousSize = mediaService.getAll().size();

        formMediaMetadataDto.setUrl(newUrl);

        MvcResult result = mockMvc.perform(putAuthenticated("/media")
                .content(toJson(formMediaMetadataDto))
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto newMediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(newMediaMetadataDto.getName(), name);
        assertEquals(newMediaMetadataDto.getMediaType(), mediaType.toString());
        assertEquals(newMediaMetadataDto.getUrl(), newUrl);

        assertNull(mediaService.get(formMediaMetadata.getUuid()));
        assertNotNull(mediaService.get(newMediaMetadataDto.getUuid()));

        Assert.assertEquals(previousSize, mediaService.getAll().size());
        Assert.assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        revisions.sort(Comparator.comparing(Revision::getAction));
        assertThat(revisions, Matchers.hasSize(2));

        Revision revision = revisions.get(1);
        assertEquals(Revision.Action.DELETE, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), formMediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

        revision = revisions.get(0);
        assertEquals(Revision.Action.ADD, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), newMediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

    }

    @Test
    public void successWebToImage() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final long previousSize = mediaService.getAll().size();

        final MediaType newMediaType = MediaType.IMAGE;

        final MediaType mediaType = MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpeg", "image/jpeg", "{json:null}".getBytes());

        formMediaMetadataDto.setMediaType(newMediaType.toString());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media/" + formMediaMetadataDto.getUuid() + "/file")
                .file(jsonFile)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto newMediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(newMediaMetadataDto.getName(), name);
        assertEquals(newMediaMetadataDto.getMediaType(), mediaType.toString());
        assertNotEquals(url, newMediaMetadataDto.getUrl());
        assertThat(newMediaMetadataDto.getUrl(), Matchers.startsWith("http://localhost:8080/file/"));

        assertNull(mediaService.get(formMediaMetadata.getUuid()));
        assertNotNull(mediaService.get(newMediaMetadataDto.getUuid()));

        Assert.assertEquals(previousSize, mediaService.getAll().size());
        Assert.assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        revisions.sort(Comparator.comparing(Revision::getAction));
        assertThat(revisions, Matchers.hasSize(2));

        Revision revision = revisions.get(1);
        assertEquals(Revision.Action.DELETE, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), formMediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

        revision = revisions.get(0);
        assertEquals(Revision.Action.ADD, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), newMediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void failedWebToUnknownMimeType() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final long previousSize = mediaService.getAll().size();

        final MediaType newMediaType = MediaType.IMAGE;

        final MediaType mediaType = MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpeg", "sdfsdf", "{json:null}".getBytes());

        formMediaMetadataDto.setMediaType(newMediaType.toString());

        mockMvc.perform(fileUploadAuthenticated("/media/" + formMediaMetadataDto.getUuid() + "/file")
                .file(jsonFile)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_MEDIA_TYPE_NOT_SUPPORTED))
                .andReturn();

        Assert.assertEquals(previousSize, mediaService.getAll().size());
        Assert.assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void failedPutFileWithWrongUuid() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final long previousSize = mediaService.getAll().size();

        final MediaType newMediaType = MediaType.IMAGE;

        final MediaType mediaType = MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpeg", "sdfsdf", "{json:null}".getBytes());

        formMediaMetadataDto.setMediaType(newMediaType.toString());

        mockMvc.perform(fileUploadAuthenticated("/media/sdpfosdfiosd/file")
                .file(jsonFile)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();

        Assert.assertEquals(previousSize, mediaService.getAll().size());
        Assert.assertEquals(previousRevision, revisionService.getLatest());
    }

}

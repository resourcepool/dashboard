package com.excilys.shooflers.dashboard.server.mediacontroller.create;

import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.mediacontroller.AbstractMediaControllerTest;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.rest.MediaController;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Florian Castelain
 */
public class MediaControllerCreateTest extends AbstractMediaControllerTest {

    // ============================================================
    //	Tests
    // ============================================================

    @Test
    public void failedEmptyBody1() throws Exception {
        mockMvc.perform(fileUploadAuthenticated("/media"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void failedOnlyName() throws Exception {
        final String name = "UnNom";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder().mediaType(MediaType.IMAGE).name(name).build())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void failedWithoutTagBundle() throws Exception {
        final String name = "Purinsessu Keni";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder().name(name).mediaType(com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE).build())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_COMPULSORY_FIELD))
                .andReturn();
    }

    @Test
    public void failedWithUnvalidTagBundle() throws Exception {
        final String name = "UnNom";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder().name(name).mediaType(com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE).bundleTag("bunduru").build())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void failedWithUnvalidMediaTypeWithoutFile() throws Exception {
        final String name = "UnNom";
        final MediaMetadataDto mediaMetadataDto = new MediaMetadataDto.Builder().mediaType(MediaType.WEB).name(name).bundleTag(globalBundleMetadataDto.getUuid()).build();
        mediaMetadataDto.setMediaType("mediatype");

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(mediaMetadataDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void successMediaWeb() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Killua Zoldik";
        final String url = "http://www.google.fr";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB;

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .url(url)
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(mediaType, com.excilys.shooflers.dashboard.server.model.type.MediaType.valueOf(mediaMetadataDto.getMediaType()));
        assertNull(mediaMetadataDto.getValidity());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());

        Path path = fileSystem.getPath(props.getBasePath(), MediaDao.ENTITY_NAME, mediaMetadataDto.getBundleTag(), mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(Files.isRegularFile(path));

        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.ADD);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), mediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }


    @Test
    public void failedMediaImageWithoutFile() throws Exception {
        final String name = "Gon Freecss";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE;


        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .build())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void failedMediaImageWithWrongExtension() throws Exception {
        final String name = "UnNom";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.json", "application/json", "{json:null}".getBytes());

        mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .build())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void failedMediaWebWithoutUrl() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Killua Zoldik";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB;

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format(MediaController.MESSAGE_NEED_URL, com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB)));

        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void failedMediaWebWithMalformedUrl() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Killua Zoldik";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB;
        final String url = "hhtp::www.google.fr";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .url(url)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_MALFORMED_URL));

        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void successMediaWebComplete() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Killua Zoldik";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB;
        final LocalDateTime startDateTime = LocalDateTime.now().minusMonths(3).minusDays(10);
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(10);
        final String url = "http://www.google.fr";

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .validity(makeValidityDto(startDateTime, endDateTime))
                        .mediaType(mediaType)
                        .url(url)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertNotNull(mediaMetadataDto.getValidity());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(startDateTime, toLocalDateTime(mediaMetadataDto.getValidity().getStart()));
        assertEquals(endDateTime, toLocalDateTime(mediaMetadataDto.getValidity().getEnd()));

        Path path = fileSystem.getPath(props.getBasePath(), MediaDao.ENTITY_NAME, mediaMetadataDto.getBundleTag(), mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(Files.isRegularFile(path));

        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.ADD);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), mediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void successMediaWebDespiteTryingFixUuid() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Zelda";
        final String chosenUuid = UUID.randomUUID().toString();
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB;
        final String url = "http://www.google.fr";

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .uuid(chosenUuid)
                        .name(name)
                        .mediaType(mediaType)
                        .url(url)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertNotEquals(chosenUuid, mediaMetadataDto.getUuid());
        assertEquals(name, mediaMetadataDto.getName());
        assertNull(mediaMetadataDto.getValidity());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());

        Path path = fileSystem.getPath(props.getBasePath(), MediaDao.ENTITY_NAME, mediaMetadataDto.getBundleTag(), mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(Files.isRegularFile(path));

        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.ADD);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), mediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void successMediaImage() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final long size = mediaService.getAll().size();
        final String name = "Zelda";
        final String chosenUuid = UUID.randomUUID().toString();
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpeg", "image/jpeg", "{json:null}".getBytes());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .uuid(chosenUuid)
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        // Vérification de l'objet Retour
        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertNotEquals(chosenUuid, mediaMetadataDto.getUuid());
        assertEquals(name, mediaMetadataDto.getName());
        assertNull(mediaMetadataDto.getValidity());
        assertNotNull(mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());

        // Vérification de la persistence
        Path path = fileSystem.getPath(props.getBasePath(), MediaDao.ENTITY_NAME, mediaMetadataDto.getBundleTag(), mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(Files.isRegularFile(path));

        assertEquals(size + 1, mediaService.getAll().size());

        // Vérification de la peristence de l'écriture
        path = fileSystem.getPath(props.getBaseResources(), mediaMetadataDto.getUrl().substring(mediaMetadataDto.getUrl().lastIndexOf("/") + 1));
        assertTrue(Files.isRegularFile(path));

        // Vérification de la persistence de l'historique
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.ADD);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), mediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }
}

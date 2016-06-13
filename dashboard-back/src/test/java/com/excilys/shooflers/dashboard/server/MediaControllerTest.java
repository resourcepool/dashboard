package com.excilys.shooflers.dashboard.server;

import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.rest.MediaController;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mickael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class MediaControllerTest extends BaseControllerTest {

    // ============================================================
    //	Attributes
    // ============================================================

    @Autowired
    private MediaService mediaService;

    @Autowired
    private BundleService bundleService;

    /**
     * GlobalBund
     */
    private static BundleMetadataDto globalBundleMetadataDto;

    // ============================================================
    //	Callback for Tests
    // ============================================================
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (globalBundleMetadataDto == null) {
            globalBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name("Bundle").build());
        }
    }

    // ============================================================
    //	Tests
    // ============================================================

    @Test
    public void mediaFindAll() throws Exception {
        mockMvc.perform(getAuthenticated("/media"))
                .andExpect(status().isMethodNotAllowed());
    }

    /**
     * The main goal is to check that an empty list doesn't generate an error
     */
    @Test
    public void mediaFindAllByBundleEmpty() throws Exception {
        BundleMetadataDto bundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name("Bundle").build());
        mockMvc.perform(getAuthenticated("/media/" + bundleMetadataDto.getUuid()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void mediaFindAllByBundleNotEmpty() throws Exception {
        BundleMetadataDto bundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name("Bundle").build());
        assertEquals(0, mediaService.getByBundle(bundleMetadataDto.getUuid()).size());

        String stringResult;

        stringResult = mockMvc.perform(getAuthenticated("/media/" + bundleMetadataDto.getUuid()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<MediaMetadataDto> list = objectMapper.readValue(stringResult, new TypeReference<List<MediaMetadataDto>>() {
        });
        assertEquals(0, list.size());

        mediaService.save(Media.builder().metadata(MediaMetadata.builder().name("pao").bundleTag(bundleMetadataDto.getUuid()).build()).build());
        mediaService.save(Media.builder().metadata(MediaMetadata.builder().name("pao").bundleTag(bundleMetadataDto.getUuid()).build()).build());
        assertEquals(2, mediaService.getByBundle(bundleMetadataDto.getUuid()).size());

        stringResult = mockMvc.perform(getAuthenticated("/media/" + bundleMetadataDto.getUuid()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        objectMapper = new ObjectMapper();
        list = objectMapper.readValue(stringResult, new TypeReference<List<MediaMetadataDto>>() {
        });

        // assertEquals(mediaService.getByBundleTag(bundleMetadataDto.getTag()), list);
        assertEquals(2, list.size());
    }

    @Test
    public void mediaFindAllByBundleNotFoundMalformedUuid() throws Exception {
        mockMvc.perform(getAuthenticated("/media/dsfsdf"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MediaController.MESSAGE_BUNDLE_NOT_FOUND));
    }

    @Test
    public void mediaFindNotFoundMalformedUuid() throws Exception {
        mockMvc.perform(getAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/osdiufosdiuf"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MediaController.MESSAGE_MEDIA_NOT_FOUND));
    }

    @Test
    public void mediaFindNotFoundReally() throws Exception {
        mockMvc.perform(getAuthenticated("/media/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MediaController.MESSAGE_BUNDLE_NOT_FOUND))
        ;
    }

    @Test
    public void mediaFindFound() throws Exception {
        final String name = "bundleMetadataDto";
        Media media = Media.builder().metadata(MediaMetadata.builder().name(name).bundleTag(globalBundleMetadataDto.getUuid()).build()).build();
        mediaService.save(media);

        MvcResult result = mockMvc.perform(getAuthenticated(("/media/" + globalBundleMetadataDto.getUuid() + "/" + media.getMetadata().getUuid())))
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto newMediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);
        assertEquals(name, newMediaMetadataDto.getName());
        assertEquals(globalBundleMetadataDto.getUuid(), newMediaMetadataDto.getBundleTag());
        assertNull(newMediaMetadataDto.getUrl());
        assertNull(newMediaMetadataDto.getValidity());
    }

    @Test
    public void mediaCreateFailedEmptyBody1() throws Exception {
        mockMvc.perform(fileUploadAuthenticated("/media"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void mediaCreateFailedOnlyName() throws Exception {
        final String name = "UnNom";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder().name(name).build())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_COMPULSORY_FIELD))
                .andReturn();
    }

    @Test
    public void mediaCreateFailedWithoutUuidBundle() throws Exception {
        final String name = "Purinsessu Keni";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder().name(name).mediaType(MediaType.IMAGE).build())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_COMPULSORY_FIELD))
                .andReturn();
    }

    @Test
    public void mediaImageCreateFailedWithUnvalidUuidBundle() throws Exception {
        final String name = "UnNom";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder().name(name).mediaType(MediaType.IMAGE).bundleTag("bunduru").build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_BUNDLE_NOT_FOUND))

                .andReturn();
    }

    @Test
    public void mediaCreateFailedWithUnvalidMediaTypeWithoutFile() throws Exception {
        final String name = "UnNom";
        final MediaMetadataDto mediaMetadataDto = new MediaMetadataDto.Builder().name(name).bundleTag(globalBundleMetadataDto.getUuid()).build();
        mediaMetadataDto.setMediaType("mediatype");

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(mediaMetadataDto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_NEED_FILE_WHEN_NO_MEDIA_TYPE))
                .andReturn();
    }

    @Test
    @Ignore("need rules")
    public void mediaCreateFailedWithExtensionFileDifferentOfMimeType() throws Exception {
        final BundleMetadataDto bundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name("Bundle").build());

        final long previousRevision = revisionService.getLatest();
        final String name = "UnNom";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.png", "image/png", "{json:null}".getBytes());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(bundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_EXTENSION_NAME))
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(mediaType, com.excilys.shooflers.dashboard.server.model.type.MediaType.valueOf(mediaMetadataDto.getMediaType()));
        assertNull(mediaMetadataDto.getValidity());
        assertNull(mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        fail("Need to fix a default Duration");
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getBundleTag() + "/" + mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

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
    public void mediaImageCreateFailedWithNotCorrespondingMediaTypeToFile() throws Exception {
        final String name = "UnNom";
        final String contentFile = "{json:null}";
        final MediaMetadataDto mediaMetadataDto = new MediaMetadataDto.Builder()
                .name(name)
                .bundleTag(globalBundleMetadataDto.getUuid())
                .mediaType(com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE)
                .build();
        final MockMultipartFile fakeImageFile = new MockMultipartFile("file", "texte.jpg", "image/png", contentFile.getBytes());

        mockMvc.perform(fileUploadAuthenticated("/media")
                .file(fakeImageFile)
                .param("media", toJson(mediaMetadataDto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_NOT_CORRESPONDING_MEDIA_TYPE))
                .andReturn();

    }

    @Test
    public void mediaImageCreateSuccessWithUnvalidMediaTypeInMetadataWithFile() throws Exception {
        final String name = "UnNom";
        final String contentFile = "{json:null}";
        final MediaMetadataDto mediaMetadataDto = new MediaMetadataDto.Builder().name(name).bundleTag(globalBundleMetadataDto.getUuid()).mediaType(com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG).build();
        final MockMultipartFile fakeImageFile = new MockMultipartFile("file", "texte.jpg", "image/jpg", "{json:null}".getBytes());
        mediaMetadataDto.setMediaType("mediatype");

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(fakeImageFile)
                .param("media", toJson(mediaMetadataDto))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto newMediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + newMediaMetadataDto.getBundleTag() + "/" + newMediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        file = new File(props.getBaseResources() + newMediaMetadataDto.getUrl());
        assertTrue(file.isFile());

        mockMvc.perform(get(URI.create("/public" + newMediaMetadataDto.getUrl())))
                .andExpect(status().isOk())
                .andExpect(content().string(contentFile));
    }

    @Test
    public void mediaWebCreateSuccess() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Killua Zoldik";
        final String url = "http://www.google.fr";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = MediaType.WEB;

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getUuid())
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
//        fail("Need to fix a default Duration");
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getBundleTag() + "/" + mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

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
    public void mediaWebCreateSuccessWithFile() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Kurapika";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpg", "image/jpg", "{json:null}".getBytes());
        final String url = "http://www.google.fr";

        // Also check if mediatype in mediametadata is overriden by the mediatype in the file in the multipartform
        assertNotEquals(mediaType.toString(), jsonFile.getContentType());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .url(url)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(mediaType.getValidMimeTypes(), mediaMetadataDto.getMediaType());
        assertNull(mediaMetadataDto.getValidity());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getBundleTag() + "/" + mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        file = new File(props.getBaseResources() + mediaMetadataDto.getUrl());
        assertFalse(file.exists());

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
    public void mediaImageCreateSuccessWithFile() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Kurapika";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.NONE;
        final String contentFile = "{json:null}";
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpg", com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_JPG.getMimeType(), contentFile.getBytes());

        // Also check if mediatype in mediametadata is overriden by the mediatype in the file in the multipartform if its none
        assertNotEquals(mediaType.toString(), jsonFile.getContentType());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(jsonFile.getContentType(), mediaMetadataDto.getMediaType());
        assertNull(mediaMetadataDto.getValidity());
        assertThat(mediaMetadataDto.getUrl(), Matchers.startsWith("/" + globalBundleMetadataDto.getUuid() + "/" + mediaMetadataDto.getUuid()));
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getBundleTag() + "/" + mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        file = new File(props.getBaseResources() + mediaMetadataDto.getUrl());
        assertTrue(file.isFile());

        mockMvc.perform(get(URI.create("/public" + mediaMetadataDto.getUrl())))
                .andExpect(status().isOk())
                .andExpect(content().string(contentFile));

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
    public void mediaImageCreateFailedWithoutFile() throws Exception {
        final String name = "Gon Freecss";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG;


        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format(MediaController.MESSAGE_NEED_FILE, com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG.getMimeType())))
                .andReturn();
    }

    @Test
    public void mediaImageCreateFailedWithWrongExtension() throws Exception {
        final BundleMetadataDto bundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name("Bundle").build());
        final String name = "UnNom";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.json", "application/json", "{json:null}".getBytes());

        mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(bundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_MEDIA_TYPE_NOT_SUPPORTED))
                .andReturn();
    }

    @Test
    public void mediaWebCreateFailedWithoutUrl() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Killua Zoldik";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB_SITE;

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format(MediaController.MESSAGE_NEED_URL, com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB_SITE.getMimeType())));

        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void mediaWebCreateFailedWithMalformedUrl() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Killua Zoldik";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB_SITE;
        final String url = "hhtp::www.google.fr";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .url(url)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_MALFORMED_URL));

        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void mediaWebCreateSuccessComplete() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Killua Zoldik";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB_SITE;
        final LocalDateTime startDateTime = LocalDateTime.now().minusMonths(3).minusDays(10);
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(10);
        final String url = "http://www.google.fr";

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .validity(makeValidityDto(startDateTime, endDateTime))
                        .mediaType(mediaType)
                        .url(url)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(mediaType, com.excilys.shooflers.dashboard.server.model.type.MediaType.getMediaType(mediaMetadataDto.getMediaType()));
        assertNotNull(mediaMetadataDto.getValidity());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());
        assertEquals(startDateTime, toLocalDateTime(mediaMetadataDto.getValidity().getStart()));
        assertEquals(endDateTime, toLocalDateTime(mediaMetadataDto.getValidity().getEnd()));

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getBundleTag() + "/" + mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

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
    public void mediaWebCreateSuccessDespiteTryingFixUuid() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "Zelda";
        final String chosenUuid = UUID.randomUUID().toString();
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB_SITE;
        final String url = "http://www.google.fr";

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .uuid(chosenUuid)
                        .name(name)
                        .mediaType(mediaType)
                        .url(url)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertNotEquals(chosenUuid, mediaMetadataDto.getUuid());
        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(mediaType, com.excilys.shooflers.dashboard.server.model.type.MediaType.getMediaType(mediaMetadataDto.getMediaType()));
        assertNull(mediaMetadataDto.getValidity());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getBundleTag() + "/" + mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

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
    public void mediaImageCreateSuccessDespiteTryingFixUuid() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String chosenUuid = UUID.randomUUID().toString();
        final String name = "Kurapika";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.NONE;
        final String contentFile = "{json:null}";
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpg", com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_JPG.getMimeType(), contentFile.getBytes());

        // Also check if mediatype in mediametadata is overriden by the mediatype in the file in the multipartform if its none
        assertNotEquals(mediaType.toString(), jsonFile.getContentType());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .uuid(chosenUuid)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertNotEquals(chosenUuid, mediaMetadataDto.getUuid());
        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(jsonFile.getContentType(), mediaMetadataDto.getMediaType());
        assertNull(mediaMetadataDto.getValidity());
        assertThat(mediaMetadataDto.getUrl(), Matchers.startsWith("/" + globalBundleMetadataDto.getUuid() + "/" + mediaMetadataDto.getUuid()));
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getBundleTag() + "/" + mediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        file = new File(props.getBaseResources() + mediaMetadataDto.getUrl());
        assertTrue(file.isFile());

        mockMvc.perform(get(URI.create("/public" + mediaMetadataDto.getUrl())))
                .andExpect(status().isOk())
                .andExpect(content().string(contentFile));

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

    //
//    @Test
//    public void bundleCreateFailedStartAfterEnd() throws Exception {
//        final String name = "Bouikbouik";
//
//        mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(new BundleMetadataDto.Builder()
//                        .name(name)
//                        .validity(makeValidityDto(LocalDateTime.now(), LocalDateTime.now().minusDays(9)))
//                        .build()))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//    @Test
//    public void bundleCreateSuccesWithValidityWithoutStartWithoutEnd() throws Exception {
//        final String name = "Bouikbouik";
//
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
//                .name(name)
//                .validity(makeValidityDto(null, null))
//                .build();
//        assertNotNull(bundleMetadataDto.getValidity());
//
//        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(bundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
//        assertEquals(name, bundleMetadataDtoAfter.getName());
//        assertNull(bundleMetadataDtoAfter.getValidity());
//    }
//
//    @Test
//    public void bundleCreateSuccesWithValidityWithStartWithoutEnd() throws Exception {
//        final String name = "Bouikbouik";
//        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(6);
//
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
//                .name(name)
//                .validity(makeValidityDto(startDateTime, null))
//                .build();
//        assertNotNull(bundleMetadataDto.getValidity());
//
//        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(bundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
//        assertEquals(name, bundleMetadataDtoAfter.getName());
//        assertNotNull(bundleMetadataDtoAfter.getValidity());
//        assertNull(bundleMetadataDtoAfter.getValidity().getEnd());
//        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()));
//    }
//
//    @Test
//    public void bundleCreateSuccesWithValidityWithoutStartWithEnd() throws Exception {
//        final String name = "Bouikbouik";
//        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(9);
//
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
//                .name(name)
//                .validity(makeValidityDto(null, endDateTime))
//                .build();
//        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(bundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
//        assertEquals(name, bundleMetadataDtoAfter.getName());
//        assertNotNull(bundleMetadataDtoAfter.getValidity());
//        assertThat(toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()), allOf(LocalDateTimeMatchers.before(LocalDateTime.now().plus(Duration.ofSeconds(1))), LocalDateTimeMatchers.after(LocalDateTime.now().minus(Duration.ofSeconds(1)))));
//        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getEnd()));
//    }
//
//    @Test
//    public void bundleCreateFailedWithValidityWithoutStartWithEndAnteriorNow() throws Exception {
//        final String name = "Bouikbouik";
//
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
//                .name(name)
//                .validity(makeValidityDto(null, LocalDateTime.now().minusDays(9)))
//                .build();
//        mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(bundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//    @Test
//    public void bundleCreateFailedWithWrongFormatStart() throws Exception {
//        final String name = "Bouikbouik";
//
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
//                .name(name)
//                .validity(makeValidityDto(null, null))
//                .build();
//        bundleMetadataDto.getValidity().setStart("zesljkl");
//        assertNotNull(bundleMetadataDto.getValidity());
//
//        mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(bundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//    @Test
//    public void bundleCreateFailedWithWronfFormatEnd() throws Exception {
//        final String name = "Bouikbouik";
//
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
//                .name(name)
//                .validity(makeValidityDto(LocalDateTime.now(), null))
//                .build();
//        bundleMetadataDto.getValidity().setEnd("zesljkl");
//        assertNotNull(bundleMetadataDto.getValidity());
//
//        mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(bundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//
//    @Test
//    public void bundleCreateFailedIfUnknownProperty() throws Exception {
//        mockMvc.perform(postAuthenticated(("/bundle"))
//                .content("{unknown:null}")
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//
    @Test
    public void mediaDeleteByUuidBundleFailed() throws Exception {
//        final long previousSize = ;
        final long previousRevision = revisionService.getLatest();

        mockMvc.perform(deleteAuthenticated("/media/" + globalBundleMetadataDto.getUuid()))
                .andExpect(status().isMethodNotAllowed())
        ;

//        assertEquals(previousSize, mediaService.getAll().size());
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void mediaDeleteByUuidBundleFailed2() throws Exception {
//        final long previousSize = ;
        final long previousRevision = revisionService.getLatest();

        mockMvc.perform(deleteAuthenticated("/media/epzirpeoi"))
                .andExpect(status().isMethodNotAllowed())
        ;

//        assertEquals(previousSize, mediaService.getAll().size());
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void mediaDeleteFailedWithUnknownUuidMedia() throws Exception {
//        final long previousSize = ;
        final long previousRevision = revisionService.getLatest();

        mockMvc.perform(deleteAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/wronguuid"))
                .andExpect(status().isNotFound())
        ;

//        assertEquals(previousSize, mediaService.getAll().size());
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void mediaWebDeleteSuccess() throws Exception {
        final String name = "Cagole";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB_SITE;
        final String url = "http://google.fr";
        final MediaMetadataDto mediaMetadataDto = mediaService.save(new MediaMetadataDto.Builder()
                .bundleTag(globalBundleMetadataDto.getUuid())
                .url(url)
                .name(name)
                .mediaType(mediaType)
                .build());
//        final long previousSize = mediaService.getAll().size();
        final long previousRevision = revisionService.getLatest();

        mockMvc.perform(getAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + mediaMetadataDto.getUuid()))
                .andExpect(status().isOk())
        ;

        mockMvc.perform(deleteAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + mediaMetadataDto.getUuid()))
                .andDo(print())
                .andExpect(status().isOk())
        ;
        mockMvc.perform(deleteAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + mediaMetadataDto.getUuid()))
                .andExpect(status().isNotFound())
        ;

        mockMvc.perform(getAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + mediaMetadataDto.getUuid()))
                .andExpect(status().isNotFound())
        ;

//        assertEquals(previousSize, mediaService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.DELETE);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), mediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

    }

    @Test
    public void mediaImageDeleteSuccess() throws Exception {
        final String name = "Kurapika";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.NONE;
        final String contentFile = "{json:null}";
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpg", com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_JPG.getMimeType(), contentFile.getBytes());
        final MediaMetadataDto mediaMetadataDto = new MediaMetadataDto.Builder()
                .name(name)
                .mediaType(mediaType)
                .bundleTag(globalBundleMetadataDto.getUuid())
                .build();

        // Also check if mediatype in mediametadata is overriden by the mediatype in the file in the multipartform if its none
        assertNotEquals(mediaType.toString(), jsonFile.getContentType());

        MediaMetadataDto newMediaMetadataDto = fromJson(mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(mediaMetadataDto))
        )
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), MediaMetadataDto.class);


        final long previousRevision = revisionService.getLatest();

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + newMediaMetadataDto.getBundleTag() + "/" + newMediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        file = new File(props.getBaseResources() + newMediaMetadataDto.getUrl());
        assertTrue(file.isFile());

        mockMvc.perform(get(URI.create("/public" + newMediaMetadataDto.getUrl())))
                .andExpect(status().isOk())
                .andExpect(content().string(contentFile));

        mockMvc.perform(getAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + newMediaMetadataDto.getUuid()))
                .andExpect(status().isOk())
        ;

        mockMvc.perform(deleteAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + newMediaMetadataDto.getUuid()))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        mockMvc.perform(getAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + newMediaMetadataDto.getUuid()))
                .andExpect(status().isNotFound())
        ;

        file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + newMediaMetadataDto.getBundleTag() + "/" + newMediaMetadataDto.getUuid() + ".yaml");
        assertFalse(file.exists());

        file = new File(props.getBaseResources() + newMediaMetadataDto.getUrl());
        assertFalse(file.isFile());

        mockMvc.perform(get(URI.create("/public" + newMediaMetadataDto.getUrl())))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

//        assertEquals(previousSize, mediaService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.DELETE);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), newMediaMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

    }

    @Test
    public void mediaImageDeleteFailedBecauseIoException() throws Exception {
        final String name = "Kurapika";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.NONE;
        final String contentFile = "{json:null}";
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpg", com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_JPG.getMimeType(), contentFile.getBytes());
        final MediaMetadataDto mediaMetadataDto = new MediaMetadataDto.Builder()
                .name(name)
                .mediaType(mediaType)
                .bundleTag(globalBundleMetadataDto.getUuid())
                .build();

        // Also check if mediatype in mediametadata is overriden by the mediatype in the file in the multipartform if its none
        assertNotEquals(mediaType.toString(), jsonFile.getContentType());

        MediaMetadataDto newMediaMetadataDto = fromJson(mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(mediaMetadataDto))
        )
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), MediaMetadataDto.class);


        final long previousRevision = revisionService.getLatest();

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + newMediaMetadataDto.getBundleTag() + "/" + newMediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        file = new File(props.getBaseResources() + newMediaMetadataDto.getUrl());
        assertTrue(file.isFile());
        assertTrue(file.delete());

        mockMvc.perform(get(URI.create("/public" + newMediaMetadataDto.getUrl())))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));


        mockMvc.perform(getAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + newMediaMetadataDto.getUuid()))
                .andExpect(status().isOk())
        ;

        mockMvc.perform(deleteAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + newMediaMetadataDto.getUuid()))
                .andExpect(status().isInternalServerError())
        ;

        mockMvc.perform(getAuthenticated("/media/" + globalBundleMetadataDto.getUuid() + "/" + newMediaMetadataDto.getUuid()))
                .andExpect(status().isOk())
        ;

        file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + newMediaMetadataDto.getBundleTag() + "/" + newMediaMetadataDto.getUuid() + ".yaml");
        assertTrue(file.exists());

//        assertEquals(previousSize, mediaService.getAll().size());
        assertEquals(previousRevision, revisionService.getLatest());
    }

//    @Test
//    public void deleteBundle() throws Exception {
//        mediaService.create(new BundleMetadataDto.Builder().name("ToDelete").build());
//
//        final long previousSize = mediaService.getAll().size();
//        final long previousRevision = revisionService.getLatest();
//
//        assertThat(mediaService.getAll().size(), Matchers.greaterThanOrEqualTo(1));
//
//        BundleMetadataDto bundleMetadataDto = mediaService.getAll().getByTag(mediaService.getAll().size() - 1);
//
//        assertNotNull(mediaService.getByTag(bundleMetadataDto.getTag()));
//
//        mockMvc.perform(deleteAuthenticated("/bundle/" + bundleMetadataDto.getTag()))
//                .andExpect(status().isNoContent())
//        ;
//
//        assertNull(mediaService.getByTag(bundleMetadataDto.getTag()));
//        assertEquals(previousSize - 1, mediaService.getAll().size());
//        assertEquals(previousRevision + 1, revisionService.getLatest());
//
//        List<Revision> revisions = revisionService.getDiffs(previousRevision);
//        assertThat(revisions, IsCollectionWithSize.hasSize(1));
//
//        Revision revision = revisions.getByTag(0);
//        assertEquals(revision.getAction(), Revision.Action.DELETE);
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), bundleMetadataDto.getTag());
//        assertEquals(revision.getResult(), null);
//    }
//
//    @Test
//    @Ignore("Not yet implemented...")
//    public void bundleDeleteContainingMedia() throws Exception {
//        fail();
//    }
//
//    @Test
//    public void editBundleSuccessBasic() throws Exception {
//        final String newName = "Nouveau Nom";
//        final BundleMetadataDto bundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name("Name").build());
//        final String formName = bundleMetadataDto.getName();
//        final long previousRevision = revisionService.getLatest();
//        final long previousSize = mediaService.getAll().size();
//
//        assertNotEquals("Choose a newName different", newName, formName);
//        assertNull(bundleMetadataDto.getValidity());
//
//        bundleMetadataDto.setName(newName);
//
//        MvcResult result = mockMvc.perform(putAuthenticated("/bundle")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(toJson(bundleMetadataDto)))
//                .andExpect(status().isOk()).andReturn();
//
//        BundleMetadataDto newBundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
//        assertEquals(newName, newBundleMetadataDto.getName());
//        assertNull(newBundleMetadataDto.getValidity());
//        assertNotEquals(newBundleMetadataDto.getTag(), bundleMetadataDto.getTag());
//        assertNull(newBundleMetadataDto.getValidity());
//        assertNull(bundleMetadataDto.getValidity());
//
//        assertNull(mediaService.getByTag(bundleMetadataDto.getTag()));
//        assertNotNull(mediaService.getByTag(newBundleMetadataDto.getTag()));
//        assertEquals(previousSize, mediaService.getAll().size());
//        assertEquals(previousRevision + 1, revisionService.getLatest());
//
//        List<Revision> revisions = revisionService.getDiffs(previousRevision);
//        revisions.sort((revision1, revision2) -> revision1.getAction().compareTo(revision2.getAction()));
//        assertThat(revisions, Matchers.hasSize(2));
//        System.out.println(revisions);
//
//        Revision revision = revisions.getByTag(1);
//        assertEquals(Revision.Action.DELETE, revision.getAction());
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), bundleMetadataDto.getTag());
//        assertEquals(revision.getResult(), null);
//
//        revision = revisions.getByTag(0);
//        assertEquals(Revision.Action.ADD, revision.getAction());
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), newBundleMetadataDto.getTag());
//        assertEquals(revision.getResult(), null);
//    }
//
//    @Test
//    public void editBundleSuccessOnValidity() throws Exception {
//        final String formName = "AncienNom";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(formName).build());
//        final LocalDateTime startDateTime = LocalDateTime.now();
//        final long previousRevision = revisionService.getLatest();
//        final long previousSize = mediaService.getAll().size();
//
//        assertNull(formBundleMetadataDto.getValidity());
//
//        formBundleMetadataDto.setValidity(makeValidityDto(startDateTime, null));
//
//        MvcResult result = mockMvc.perform(putAuthenticated("/bundle")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(toJson(formBundleMetadataDto)))
//                .andExpect(status().isOk()).andReturn();
//
//        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
//        assertEquals(formName, bundleMetadataDto.getName());
//        assertNotNull(bundleMetadataDto.getValidity());
//        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getStart()));
//        assertNull(bundleMetadataDto.getValidity().getEnd());
//        assertNotEquals(bundleMetadataDto.getTag(), formBundleMetadataDto.getTag());
//        assertEquals(bundleMetadataDto.getValidity().getStart(), formBundleMetadataDto.getValidity().getStart());
//        assertEquals(bundleMetadataDto.getValidity().getEnd(), formBundleMetadataDto.getValidity().getEnd());
//        assertEquals(revisionService.getLatest(), (long) bundleMetadataDto.getRevision());
//
//        assertNull(mediaService.getByTag(formBundleMetadataDto.getTag()));
//        assertNotNull(mediaService.getByTag(bundleMetadataDto.getTag()));
//        assertEquals(previousSize, mediaService.getAll().size());
//        assertEquals(previousRevision + 1, revisionService.getLatest());
//
//        List<Revision> revisions = revisionService.getDiffs(previousRevision);
//        revisions.sort((revision1, revision2) -> revision1.getAction().compareTo(revision2.getAction()));
//        assertThat(revisions, Matchers.hasSize(2));
//        System.out.println(revisions);
//
//        Revision revision = revisions.getByTag(1);
//        assertEquals(Revision.Action.DELETE, revision.getAction());
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), formBundleMetadataDto.getTag());
//        assertEquals(revision.getResult(), null);
//
//        revision = revisions.getByTag(0);
//        assertEquals(Revision.Action.ADD, revision.getAction());
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), bundleMetadataDto.getTag());
//        assertEquals(revision.getResult(), null);
//    }
//
//    @Test
//    public void putWithoutUUID() throws Exception {
//        final String name = "Babar";
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder().name(name).build();
//        bundleMetadataDto.setTag(null);
//
//        mockMvc.perform(putAuthenticated(("/bundle"))
//                .content(toJson(bundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//    @Test
//    public void putWithWrongUuid() throws Exception {
//        final String name = "Babar";
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder().tag("random").name(name).build();
//        bundleMetadataDto.setTag(null);
//
//        mockMvc.perform(putAuthenticated(("/bundle"))
//                .content(toJson(bundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//    @Test
//    public void editBundleFailedWithoutName() throws Exception {
//        final String name = "Toupoutou";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(name).build());
//        formBundleMetadataDto.setName(null);
//
//        mockMvc.perform(putAuthenticated(("/bundle"))
//                .content(toJson(formBundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//    @Test
//    public void editBundleFailedStartAfterEnd() throws Exception {
//        final String name = "Bouikbouik";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(name).build());
//
//        formBundleMetadataDto.setValidity(makeValidityDto(LocalDateTime.now(), LocalDateTime.now().minusDays(9)));
//        mockMvc.perform(putAuthenticated(("/bundle"))
//                .content(toJson(formBundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
////                .andExpect(content().string(""))
//                .andReturn();
//    }
//
//    @Test
//    public void editBundleSuccesWithValidityWithoutStartWithoutEnd() throws Exception {
//        final String name = "Jojo";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(name).build());
//
//        formBundleMetadataDto.setValidity(makeValidityDto(null, null));
//        assertNotNull(formBundleMetadataDto.getValidity());
//
//        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
//                .content(toJson(formBundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
//        assertEquals(name, bundleMetadataDtoAfter.getName());
//        assertNull(bundleMetadataDtoAfter.getValidity());
//    }
//
//    @Test
//    public void editBundleSuccesWithValidityWithStartWithoutEnd() throws Exception {
//        final String name = "Jojo";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(name).build());
//        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(6);
//
//        formBundleMetadataDto.setValidity(makeValidityDto(startDateTime, null));
//
//
//        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
//                .content(toJson(formBundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
//        assertEquals(name, bundleMetadataDtoAfter.getName());
//        assertNotNull(bundleMetadataDtoAfter.getValidity());
//        assertNull(bundleMetadataDtoAfter.getValidity().getEnd());
//        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()));
//    }
//
//    @Test
//    public void editBundleSuccesWithValidityWithoutStartWithEnd() throws Exception {
//        final String name = "Bouikbouik";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(name).build());
//        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(9);
//
//        formBundleMetadataDto.setValidity(makeValidityDto(null, endDateTime));
//
//        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
//                .content(toJson(formBundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
//        assertEquals(name, bundleMetadataDtoAfter.getName());
//        assertNotNull(bundleMetadataDtoAfter.getValidity());
//        assertThat(toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()), allOf(LocalDateTimeMatchers.before(LocalDateTime.now().plus(Duration.ofSeconds(1))), LocalDateTimeMatchers.after(LocalDateTime.now().minus(Duration.ofSeconds(1)))));
//        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getEnd()));
//    }
//
//    @Test
//    public void editBundleFailedWithValidityWithoutStartWithEndAnteriorNow() throws Exception {
//        final String name = "Bouikbouik";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(name).build());
//        formBundleMetadataDto.setValidity(makeValidityDto(null, LocalDateTime.now().minusDays(9)));
//
//        mockMvc.perform(putAuthenticated(("/bundle"))
//                .content(toJson(formBundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
////                .andExpect(content().string(""))
//                .andReturn();
//    }
//
//    @Test
//    public void editBundleFailedWithWrongFormatStart() throws Exception {
//        final String name = "Bouikbouik";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(name).build());
//        formBundleMetadataDto.setValidity(makeValidityDto(null, null));
//        formBundleMetadataDto.getValidity().setStart("zesljkl");
//
//        mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(formBundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(""))
//                .andReturn();
//    }
//
//    @Test
//    public void editFailedWithWronfFormatEnd() throws Exception {
//        final String name = "Bouikbouik";
//        final BundleMetadataDto formBundleMetadataDto = mediaService.create(new BundleMetadataDto.Builder().name(name).build());
//        formBundleMetadataDto.setValidity(makeValidityDto(LocalDateTime.now(), null));
//        formBundleMetadataDto.getValidity().setEnd("zesljkl");
//
//        mockMvc.perform(postAuthenticated(("/bundle"))
//                .content(toJson(formBundleMetadataDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(""))
//                .andReturn();
//    }
//
//
//    @Test
//    public void editBundleCreateFailedIfUnknownProperty() throws Exception {
//        mockMvc.perform(putAuthenticated(("/bundle"))
//                .content("{tag:null, unknown:null}")
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(""))
//                .andReturn();
//    }

}

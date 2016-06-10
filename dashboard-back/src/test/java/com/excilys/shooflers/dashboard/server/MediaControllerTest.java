package com.excilys.shooflers.dashboard.server;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.ValidityDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.rest.MediaController;
import com.excilys.shooflers.dashboard.server.securityTest.RequireValidUserTest;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Mickael on 07/06/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class MediaControllerTest {

    // ============================================================
    //	Attributes
    // ============================================================

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private BundleService bundleService;

    @Autowired
    private DashboardProperties props;

    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * Client Rest
     */
    private MockMvc mockMvc;

    /**
     * Converter of Json &lt;-&gt; Java Object
     */
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private BundleMetadataDto globalBundleMetadataDto;

    // ============================================================
    //	Methods utils
    // ============================================================

    /**
     * @return Content of header to authenticate to the API REST with default admin credentials.
     * (withtout the key work Basic at the start)
     */
    private String getHeaderAuthenticationContent() {
        return getHeaderAuthenticationContent(props.getAdminLogin(), props.getAdminPassword());
    }

    /**
     * Content of header to authenticate to the API REST with default admin credentials.
     *
     * @param login    Login to login
     * @param password Password to use
     * @return Content of the header Authentication
     */
    private static String getHeaderAuthenticationContent(String login, String password) {
        return "Basic " + Base64.encodeBase64String((login + ":" + password).getBytes());
    }

    /**
     * Map an object to JSON
     *
     * @param o object to map
     * @return JSON result
     */
    private String toJson(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        //noinspection unchecked
        mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    /**
     * MAp a JSON string to an Java Object
     *
     * @param txt    String to parse
     * @param aClass Class to convert
     * @param <T>    Generic type
     * @return Object mapped
     */
    private <T> T fromJson(String txt, Class<T> aClass) throws IOException {
        MockHttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(txt.getBytes());
        //noinspection unchecked
        return (T) this.mappingJackson2HttpMessageConverter.read(aClass, mockHttpInputMessage);
    }


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        //noinspection OptionalGetWithoutIsPresent
        mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    private MockHttpServletRequestBuilder postAuthenticated(String path) {
        return post(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    private MockHttpServletRequestBuilder getAuthenticated(String path) {
        return get(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    private MockHttpServletRequestBuilder putAuthenticated(String path) {
        return put(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    private MockHttpServletRequestBuilder deleteAuthenticated(String path) {
        return delete(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    private MockMultipartHttpServletRequestBuilder fileUploadAuthenticated(String path) {
        return (MockMultipartHttpServletRequestBuilder) fileUpload(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    private void resetBundles() throws IOException {
        File bundleFolder = new File(props.getBasePath() + "/" + BundleDao.ENTITY_NAME);
        File bundleExampleFolder = new File(props.getBasePath() + "/" + BundleDao.ENTITY_NAME + "Example");

        FileUtils.deleteDirectory(bundleFolder);
        FileUtils.copyDirectory(bundleExampleFolder, bundleFolder);
    }

    private static ValidityDto makeValidityDto(LocalDateTime start, LocalDateTime end) {
        return new ValidityDto(
                start == null ? null : ValidityDtoMapperImpl.FORMATTER.format(start),
                end == null ? null : ValidityDtoMapperImpl.FORMATTER.format(end));
    }

    private static LocalDateTime toLocalDateTime(String txt) {
        return LocalDateTime.from(ValidityDtoMapperImpl.FORMATTER.parse(txt));
    }

    // ============================================================
    //	Callback for Tests
    // ============================================================

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        globalBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name("Bundle").build());
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

        mediaService.save(new MediaMetadataDto.Builder().name("pao").uuidBundle(bundleMetadataDto.getUuid()).build());
        mediaService.save(new MediaMetadataDto.Builder().name("pao").uuidBundle(bundleMetadataDto.getUuid()).build());
        assertEquals(2, mediaService.getByBundle(bundleMetadataDto.getUuid()).size());

        stringResult = mockMvc.perform(getAuthenticated("/media/" + bundleMetadataDto.getUuid()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        objectMapper = new ObjectMapper();
        list = objectMapper.readValue(stringResult, new TypeReference<List<MediaMetadataDto>>() {
        });

        // assertEquals(mediaService.getByBundle(bundleMetadataDto.getUuid()), list);
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
        final MediaMetadataDto mediaMetadataDto = mediaService.save(new MediaMetadataDto.Builder().name(name).uuidBundle(globalBundleMetadataDto.getUuid()).build());

        MvcResult result = mockMvc.perform(getAuthenticated(("/media/" + globalBundleMetadataDto.getUuid() + "/" + mediaMetadataDto.getUuid())))
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto newMediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);
        assertEquals(name, newMediaMetadataDto.getName());
        assertEquals(globalBundleMetadataDto.getUuid(), newMediaMetadataDto.getUuidBundle());
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
                .param("media", toJson(new MediaMetadataDto.Builder().name(name).mediaType(com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG).build())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_COMPULSORY_FIELD))
                .andReturn();
    }

    @Test
    public void mediaImageCreateFailedWithUnvalidUuidBundle() throws Exception {
        final String name = "UnNom";

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder().name(name).mediaType(com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG).uuidBundle("bunduru").build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_BUNDLE_NOT_FOUND))

                .andReturn();
    }

    @Test
    public void mediaCreateFailedWithUnvalidMediaTypeWithoutFile() throws Exception {
        final String name = "UnNom";
        final MediaMetadataDto mediaMetadataDto = new MediaMetadataDto.Builder().name(name).uuidBundle(globalBundleMetadataDto.getUuid()).build();
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
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.png", com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_JPG.getMimeType(), "{json:null}".getBytes());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .uuidBundle(bundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_EXTENSION_NAME))
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(mediaType, com.excilys.shooflers.dashboard.server.model.type.MediaType.getMediaType(mediaMetadataDto.getMediaType()));
        assertNull(mediaMetadataDto.getValidity());
        assertNull(mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        fail("Need to fix a default Duration");
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getUuidBundle() + "/" + mediaMetadataDto.getUuid() + ".yaml");
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
                .uuidBundle(globalBundleMetadataDto.getUuid())
                .mediaType(com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG)
                .build();
        final MockMultipartFile fakeImageFile = new MockMultipartFile("file", "texte.jpg", com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_JPG.getMimeType(), contentFile.getBytes());

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
        final MediaMetadataDto mediaMetadataDto = new MediaMetadataDto.Builder().name(name).uuidBundle(globalBundleMetadataDto.getUuid()).mediaType(com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_PNG).build();
        final MockMultipartFile fakeImageFile = new MockMultipartFile("file", "texte.jpg", com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_JPG.getMimeType(), "{json:null}".getBytes());
        mediaMetadataDto.setMediaType("mediatype");

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(fakeImageFile)
                .param("media", toJson(mediaMetadataDto))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto newMediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + newMediaMetadataDto.getUuidBundle() + "/" + newMediaMetadataDto.getUuid() + ".yaml");
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
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB_SITE;

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .uuidBundle(globalBundleMetadataDto.getUuid())
                        .url(url)
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(mediaType, com.excilys.shooflers.dashboard.server.model.type.MediaType.getMediaType(mediaMetadataDto.getMediaType()));
        assertNull(mediaMetadataDto.getValidity());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
//        fail("Need to fix a default Duration");
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getUuidBundle() + "/" + mediaMetadataDto.getUuid() + ".yaml");
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
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB_SITE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpg", com.excilys.shooflers.dashboard.server.model.type.MediaType.IMAGE_JPG.getMimeType(), "{json:null}".getBytes());
        final String url = "http://www.google.fr";

        // Also check if mediatype in mediametadata is overriden by the mediatype in the file in the multipartform
        assertNotEquals(mediaType.toString(), jsonFile.getContentType());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .url(url)
                        .uuidBundle(globalBundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(mediaType.getMimeType(), mediaMetadataDto.getMediaType());
        assertNull(mediaMetadataDto.getValidity());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(0, mediaMetadataDto.getDuration());
        assertNotNull(mediaMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), mediaMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getUuidBundle() + "/" + mediaMetadataDto.getUuid() + ".yaml");
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
                        .uuidBundle(globalBundleMetadataDto.getUuid())
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

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getUuidBundle() + "/" + mediaMetadataDto.getUuid() + ".yaml");
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
                        .uuidBundle(globalBundleMetadataDto.getUuid())
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
                        .uuidBundle(bundleMetadataDto.getUuid())
                        .build()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MediaController.MESSAGE_MEDIA_TYPE_NOT_FOUND))
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
                        .uuidBundle(globalBundleMetadataDto.getUuid())
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
                        .uuidBundle(globalBundleMetadataDto.getUuid())
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
                        .uuidBundle(globalBundleMetadataDto.getUuid())
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

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getUuidBundle() + "/" + mediaMetadataDto.getUuid() + ".yaml");
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
                        .uuidBundle(globalBundleMetadataDto.getUuid())
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

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getUuidBundle() + "/" + mediaMetadataDto.getUuid() + ".yaml");
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
                        .uuidBundle(globalBundleMetadataDto.getUuid())
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

        File file = new File(props.getBasePath() + "/" + MediaDao.ENTITY_NAME + "/" + mediaMetadataDto.getUuidBundle() + "/" + mediaMetadataDto.getUuid() + ".yaml");
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
//    @Test
//    public void deleteUnknown() throws Exception {
//        final long previousSize = mediaService.getAll().size();
//        final long previousRevision = revisionService.getLatest();
//
//        mockMvc.perform(deleteAuthenticated("/bundle/wronguuid"))
//                .andExpect(status().isNotFound())
//        ;
//
//        assertEquals(previousSize, mediaService.getAll().size());
//        assertEquals(previousRevision, revisionService.getLatest());
//    }
//
//    @Test
//    public void deleteBundle() throws Exception {
//        mediaService.create(new BundleMetadataDto.Builder().name("ToDelete").build());
//
//        final long previousSize = mediaService.getAll().size();
//        final long previousRevision = revisionService.getLatest();
//
//        assertThat(mediaService.getAll().size(), Matchers.greaterThanOrEqualTo(1));
//
//        BundleMetadataDto bundleMetadataDto = mediaService.getAll().get(mediaService.getAll().size() - 1);
//
//        assertNotNull(mediaService.get(bundleMetadataDto.getUuid()));
//
//        mockMvc.perform(deleteAuthenticated("/bundle/" + bundleMetadataDto.getUuid()))
//                .andExpect(status().isNoContent())
//        ;
//
//        assertNull(mediaService.get(bundleMetadataDto.getUuid()));
//        assertEquals(previousSize - 1, mediaService.getAll().size());
//        assertEquals(previousRevision + 1, revisionService.getLatest());
//
//        List<Revision> revisions = revisionService.getDiffs(previousRevision);
//        assertThat(revisions, IsCollectionWithSize.hasSize(1));
//
//        Revision revision = revisions.get(0);
//        assertEquals(revision.getAction(), Revision.Action.DELETE);
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
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
//        assertNotEquals(newBundleMetadataDto.getUuid(), bundleMetadataDto.getUuid());
//        assertNull(newBundleMetadataDto.getValidity());
//        assertNull(bundleMetadataDto.getValidity());
//
//        assertNull(mediaService.get(bundleMetadataDto.getUuid()));
//        assertNotNull(mediaService.get(newBundleMetadataDto.getUuid()));
//        assertEquals(previousSize, mediaService.getAll().size());
//        assertEquals(previousRevision + 1, revisionService.getLatest());
//
//        List<Revision> revisions = revisionService.getDiffs(previousRevision);
//        revisions.sort((revision1, revision2) -> revision1.getAction().compareTo(revision2.getAction()));
//        assertThat(revisions, Matchers.hasSize(2));
//        System.out.println(revisions);
//
//        Revision revision = revisions.get(1);
//        assertEquals(Revision.Action.DELETE, revision.getAction());
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
//        assertEquals(revision.getResult(), null);
//
//        revision = revisions.get(0);
//        assertEquals(Revision.Action.ADD, revision.getAction());
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), newBundleMetadataDto.getUuid());
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
//        assertNotEquals(bundleMetadataDto.getUuid(), formBundleMetadataDto.getUuid());
//        assertEquals(bundleMetadataDto.getValidity().getStart(), formBundleMetadataDto.getValidity().getStart());
//        assertEquals(bundleMetadataDto.getValidity().getEnd(), formBundleMetadataDto.getValidity().getEnd());
//        assertEquals(revisionService.getLatest(), (long) bundleMetadataDto.getRevision());
//
//        assertNull(mediaService.get(formBundleMetadataDto.getUuid()));
//        assertNotNull(mediaService.get(bundleMetadataDto.getUuid()));
//        assertEquals(previousSize, mediaService.getAll().size());
//        assertEquals(previousRevision + 1, revisionService.getLatest());
//
//        List<Revision> revisions = revisionService.getDiffs(previousRevision);
//        revisions.sort((revision1, revision2) -> revision1.getAction().compareTo(revision2.getAction()));
//        assertThat(revisions, Matchers.hasSize(2));
//        System.out.println(revisions);
//
//        Revision revision = revisions.get(1);
//        assertEquals(Revision.Action.DELETE, revision.getAction());
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), formBundleMetadataDto.getUuid());
//        assertEquals(revision.getResult(), null);
//
//        revision = revisions.get(0);
//        assertEquals(Revision.Action.ADD, revision.getAction());
//        assertEquals(((long) revision.getRevision()), previousRevision + 1);
//        assertEquals(revision.getType(), Revision.Type.BUNDLE);
//        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
//        assertEquals(revision.getResult(), null);
//    }
//
//    @Test
//    public void putWithoutUUID() throws Exception {
//        final String name = "Babar";
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder().name(name).build();
//        bundleMetadataDto.setUuid(null);
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
//        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder().uuid("random").name(name).build();
//        bundleMetadataDto.setUuid(null);
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
//                .content("{uuid:null, unknown:null}")
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(""))
//                .andReturn();
//    }

}

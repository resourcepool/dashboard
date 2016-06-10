package com.excilys.shooflers.dashboard.server;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.ValidityDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.securityTest.RequireValidUserTest;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mickael
 *         <p>
 *         TODO check the message returned by an 400 error
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class BundleControllerTest {

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private BundleService bundleService;

    // ============================================================
    //	Attributes
    // ============================================================

    /**
     * Client Rest
     */
    private MockMvc mockMvc;

    /**
     * Converter of Json &lt;-&gt; Java Object
     */
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private DashboardProperties props;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
     * @param txt String to parse
     * @param aClass Class to convert
     * @param <T> Generic type
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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // ============================================================
    //	Tests
    // ============================================================

    @Test
    @Ignore("Change not yet reported")
    public void bundleAll() throws Exception {
        mockMvc.perform(getAuthenticated("/bundle"))
                .andExpect(status().isOk())
                .andReturn();

//        fromJson(result.getResponse().getContentAsString(), );
    }

    /**
     * The main goal is to check that an empty list doesn't generate an error
     */
    @Test
    @Ignore("Change not yet reported")
    public void bundleAllEmpty() throws Exception {
        File bundleFolder = new File(props.getBasePath() + "/" + BundleDao.ENTITY_NAME);

        if (bundleFolder.exists()) {
            FileUtils.deleteDirectory(bundleFolder);
            assertFalse(bundleFolder.exists());
        }
        assertTrue(bundleFolder.mkdir());

        mockMvc.perform(getAuthenticated("/bundle"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void bundleNotFoundNegative() throws Exception {
        mockMvc.perform(getAuthenticated("/bundle/dsfsdf"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void bundleNotFoundReally() throws Exception {
        mockMvc.perform(getAuthenticated("/bundle/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void bundleFound() throws Exception {
        final String name = "bundleMetadataDto";
        final BundleMetadataDto bundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());

        MvcResult result = mockMvc.perform(getAuthenticated(("/bundle/" + bundleMetadataDto.getUuid())))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadata bundleMetadata = fromJson(result.getResponse().getContentAsString(), BundleMetadata.class);
        assertEquals(name, bundleMetadata.getName());
        assertNull(bundleMetadata.getValidity());
    }

    @Test
    public void bundleCreateEmptyBody1() throws Exception {
        mockMvc.perform(postAuthenticated(("/bundle")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void bundleCreateBasicSuccess() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "UnNom";

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().name(name).build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);

        assertEquals(name, bundleMetadataDto.getName());
        assertNull(bundleMetadataDto.getValidity());
        assertNotNull(bundleMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), (long) bundleMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + BundleDao.ENTITY_NAME + "/" + bundleMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.ADD);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void bundleCreateCompleteSuccess() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "UnNom";
        final LocalDateTime startDateTime = LocalDateTime.now().minusMonths(3).minusDays(10);
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(10);

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().name(name).validity(makeValidityDto(startDateTime, endDateTime)).build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);

        assertEquals(name, bundleMetadataDto.getName());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getStart()));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getEnd()));
        assertNotNull(bundleMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), (long) bundleMetadataDto.getRevision());

        File file = new File(props.getBasePath() + "/" + BundleDao.ENTITY_NAME + "/" + bundleMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        assertEquals(previousRevision + 1, revisionService.getLatest());
    }

    @Test
    public void bundleCreateFailedWithoutName() throws Exception {
        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void bundleCreateSuccessButCantSetUuid() throws Exception {
        final String name = "Bouikbouik";
        final String chosenUuid = UUID.randomUUID().toString();

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().name(name).uuid(chosenUuid).build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);

        assertEquals(name, bundleMetadataDto.getName());
        assertNull(bundleMetadataDto.getValidity());
        assertNotEquals(chosenUuid, bundleMetadataDto.getUuid());
        assertEquals(revisionService.getLatest(), (long) bundleMetadataDto.getRevision());
    }

    @Test
    public void bundleCreateFailedStartAfterEnd() throws Exception {
        final String name = "Bouikbouik";

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder()
                        .name(name)
                        .validity(makeValidityDto(LocalDateTime.now(), LocalDateTime.now().minusDays(9)))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void bundleCreateSuccesWithValidityWithoutStartWithoutEnd() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, null))
                .build();
        assertNotNull(bundleMetadataDto.getValidity());

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNull(bundleMetadataDtoAfter.getValidity());
    }

    @Test
    public void bundleCreateSuccesWithValidityWithStartWithoutEnd() throws Exception {
        final String name = "Bouikbouik";
        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(6);

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(startDateTime, null))
                .build();
        assertNotNull(bundleMetadataDto.getValidity());

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertNull(bundleMetadataDtoAfter.getValidity().getEnd());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()));
    }

    @Test
    public void bundleCreateSuccesWithValidityWithoutStartWithEnd() throws Exception {
        final String name = "Bouikbouik";
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(9);

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, endDateTime))
                .build();
        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertThat(toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()), allOf(LocalDateTimeMatchers.before(LocalDateTime.now().plus(Duration.ofSeconds(1))), LocalDateTimeMatchers.after(LocalDateTime.now().minus(Duration.ofSeconds(1)))));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getEnd()));
    }

    @Test
    public void bundleCreateFailedWithValidityWithoutStartWithEndAnteriorNow() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, LocalDateTime.now().minusDays(9)))
                .build();
        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void bundleCreateFailedWithWrongFormatStart() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, null))
                .build();
        bundleMetadataDto.getValidity().setStart("zesljkl");
        assertNotNull(bundleMetadataDto.getValidity());

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void bundleCreateFailedWithWronfFormatEnd() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(LocalDateTime.now(), null))
                .build();
        bundleMetadataDto.getValidity().setEnd("zesljkl");
        assertNotNull(bundleMetadataDto.getValidity());

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    public void bundleCreateFailedIfUnknownProperty() throws Exception {
        mockMvc.perform(postAuthenticated(("/bundle"))
                .content("{unknown:null}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    public void deleteUnknown() throws Exception {
        final long previousSize = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();

        mockMvc.perform(deleteAuthenticated("/bundle/wronguuid"))
                .andExpect(status().isNotFound())
        ;

        assertEquals(previousSize, bundleService.getAll().size());
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void deleteBundle() throws Exception {
        bundleService.save(new BundleMetadataDto.Builder().name("ToDelete").build());

        final long previousSize = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();

        assertThat(bundleService.getAll().size(), Matchers.greaterThanOrEqualTo(1));

        BundleMetadataDto bundleMetadataDto = bundleService.getAll().get(bundleService.getAll().size() - 1);

        assertNotNull(bundleService.get(bundleMetadataDto.getUuid()));

        mockMvc.perform(deleteAuthenticated("/bundle/" + bundleMetadataDto.getUuid()))
                .andExpect(status().isNoContent())
        ;

        assertNull(bundleService.get(bundleMetadataDto.getUuid()));
        assertEquals(previousSize - 1, bundleService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.DELETE);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    @Ignore("Not yet implemented...")
    public void bundleDeleteContainingMedia() throws Exception {
        fail();
    }

    @Test
    public void editBundleSuccessBasic() throws Exception {
        final String newName = "Nouveau Nom";
        final BundleMetadataDto bundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name("Name").build());
        final String formName = bundleMetadataDto.getName();
        final long previousRevision = revisionService.getLatest();
        final long previousSize = bundleService.getAll().size();

        assertNotEquals("Choose a newName different", newName, formName);
        assertNull(bundleMetadataDto.getValidity());

        bundleMetadataDto.setName(newName);

        MvcResult result = mockMvc.perform(putAuthenticated("/bundle")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJson(bundleMetadataDto)))
                .andExpect(status().isOk()).andReturn();

        BundleMetadataDto newBundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(newName, newBundleMetadataDto.getName());
        assertNull(newBundleMetadataDto.getValidity());
        assertNotEquals(newBundleMetadataDto.getUuid(), bundleMetadataDto.getUuid());
        assertNull(newBundleMetadataDto.getValidity());
        assertNull(bundleMetadataDto.getValidity());

        assertNull(bundleService.get(bundleMetadataDto.getUuid()));
        assertNotNull(bundleService.get(newBundleMetadataDto.getUuid()));
        assertEquals(previousSize, bundleService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        revisions.sort((revision1, revision2) -> revision1.getAction().compareTo(revision2.getAction()));
        assertThat(revisions, Matchers.hasSize(2));
        System.out.println(revisions);

        Revision revision = revisions.get(1);
        assertEquals(Revision.Action.DELETE, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

        revision = revisions.get(0);
        assertEquals(Revision.Action.ADD, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), newBundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void editBundleSuccessOnValidity() throws Exception {
        final String formName = "AncienNom";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(formName).build());
        final LocalDateTime startDateTime = LocalDateTime.now();
        final long previousRevision = revisionService.getLatest();
        final long previousSize = bundleService.getAll().size();

        assertNull(formBundleMetadataDto.getValidity());

        formBundleMetadataDto.setValidity(makeValidityDto(startDateTime, null));

        MvcResult result = mockMvc.perform(putAuthenticated("/bundle")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJson(formBundleMetadataDto)))
                .andExpect(status().isOk()).andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(formName, bundleMetadataDto.getName());
        assertNotNull(bundleMetadataDto.getValidity());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getStart()));
        assertNull(bundleMetadataDto.getValidity().getEnd());
        assertNotEquals(bundleMetadataDto.getUuid(), formBundleMetadataDto.getUuid());
        assertEquals(bundleMetadataDto.getValidity().getStart(), formBundleMetadataDto.getValidity().getStart());
        assertEquals(bundleMetadataDto.getValidity().getEnd(), formBundleMetadataDto.getValidity().getEnd());
        assertEquals(revisionService.getLatest(), (long) bundleMetadataDto.getRevision());

        assertNull(bundleService.get(formBundleMetadataDto.getUuid()));
        assertNotNull(bundleService.get(bundleMetadataDto.getUuid()));
        assertEquals(previousSize, bundleService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        revisions.sort((revision1, revision2) -> revision1.getAction().compareTo(revision2.getAction()));
        assertThat(revisions, Matchers.hasSize(2));
        System.out.println(revisions);

        Revision revision = revisions.get(1);
        assertEquals(Revision.Action.DELETE, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), formBundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

        revision = revisions.get(0);
        assertEquals(Revision.Action.ADD, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void putWithoutUUID() throws Exception {
        final String name = "Babar";
        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder().name(name).build();
        bundleMetadataDto.setUuid(null);

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void putWithWrongUuid() throws Exception {
        final String name = "Babar";
        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder().uuid("random").name(name).build();
        bundleMetadataDto.setUuid(null);

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void editBundleFailedWithoutName() throws Exception {
        final String name = "Toupoutou";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());
        formBundleMetadataDto.setName(null);

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void editBundleFailedStartAfterEnd() throws Exception {
        final String name = "Bouikbouik";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());

        formBundleMetadataDto.setValidity(makeValidityDto(LocalDateTime.now(), LocalDateTime.now().minusDays(9)));
        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
//                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void editBundleSuccesWithValidityWithoutStartWithoutEnd() throws Exception {
        final String name = "Jojo";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());

        formBundleMetadataDto.setValidity(makeValidityDto(null, null));
        assertNotNull(formBundleMetadataDto.getValidity());

        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNull(bundleMetadataDtoAfter.getValidity());
    }

    @Test
    public void editBundleSuccesWithValidityWithStartWithoutEnd() throws Exception {
        final String name = "Jojo";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());
        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(6);

        formBundleMetadataDto.setValidity(makeValidityDto(startDateTime, null));


        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertNull(bundleMetadataDtoAfter.getValidity().getEnd());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()));
    }

    @Test
    public void editBundleSuccesWithValidityWithoutStartWithEnd() throws Exception {
        final String name = "Bouikbouik";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(9);

        formBundleMetadataDto.setValidity(makeValidityDto(null, endDateTime));

        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertThat(toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()), allOf(LocalDateTimeMatchers.before(LocalDateTime.now().plus(Duration.ofSeconds(1))), LocalDateTimeMatchers.after(LocalDateTime.now().minus(Duration.ofSeconds(1)))));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getEnd()));
    }

    @Test
    public void editBundleFailedWithValidityWithoutStartWithEndAnteriorNow() throws Exception {
        final String name = "Bouikbouik";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());
        formBundleMetadataDto.setValidity(makeValidityDto(null, LocalDateTime.now().minusDays(9)));

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
//                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void editBundleFailedWithWrongFormatStart() throws Exception {
        final String name = "Bunduru";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());
        formBundleMetadataDto.setValidity(makeValidityDto(null, null));
        formBundleMetadataDto.getValidity().setStart("zesljkl");

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void editFailedWithWronfFormatEnd() throws Exception {
        final String name = "Bouikbouik";
        final BundleMetadataDto formBundleMetadataDto = bundleService.save(new BundleMetadataDto.Builder().name(name).build());
        formBundleMetadataDto.setValidity(makeValidityDto(LocalDateTime.now(), null));
        formBundleMetadataDto.getValidity().setEnd("zesljkl");

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }


    @Test
    public void editBundleCreateFailedIfUnknownProperty() throws Exception {
        mockMvc.perform(putAuthenticated(("/bundle"))
                .content("{uuid:null, unknown:null}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

}

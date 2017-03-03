package com.excilys.shooflers.dashboard.server;

import com.excilys.shooflers.dashboard.server.dao.DaoInitializer;
import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.ValidityDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.securityTest.RequireValidUserTest;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author Mickael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({DashboardApplication.class, TestConfiguration.class})
@WebAppConfiguration
@SuppressWarnings("WeakerAccess")
public abstract class AbstractControllerTest {

    //============================================================
    // Attributes
    //============================================================
    @Autowired
    protected RevisionService revisionService;

    @Autowired
    protected DashboardProperties props;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected FileSystem fileSystem;

    @Autowired
    private DaoInitializer daoInitializer;

    /**
     * Client Rest
     */
    protected MockMvc mockMvc;

    /**
     * Converter of Json &lt;-&gt; Java Object
     */
    protected HttpMessageConverter mappingJackson2HttpMessageConverter;

    //============================================================
    // Methods Utils
    //============================================================

    /**
     * @return Content of header to authenticate to the API REST with default admin credentials.
     * (withtout the key work Basic at the start)
     */
    protected String getHeaderAuthenticationContent() {
        return getHeaderAuthenticationContent(props.getAdminLogin(), props.getAdminPassword());
    }

    /**
     * Content of header to authenticate to the API REST with default admin credentials.
     *
     * @param login    Login to login
     * @param password Password to use
     * @return Content of the header Authentication
     */
    protected static String getHeaderAuthenticationContent(String login, String password) {
        return "Basic " + Base64.encodeBase64String((login + ":" + password).getBytes());
    }


    /**
     * @return Content of ApiKey header
     */
    protected String getHeaderApiKeyContent() {
        return props.getApiKey();
    }

    /**
     * Map an object to JSON
     *
     * @param o object to map
     * @return JSON result
     */
    protected String toJson(Object o) throws IOException {
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
    protected <T> T fromJson(String txt, Class<T> aClass) throws IOException {
        MockHttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(txt.getBytes());
        //noinspection unchecked
        return (T) this.mappingJackson2HttpMessageConverter.read(aClass, mockHttpInputMessage);
    }


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        //noinspection OptionalGetWithoutIsPresent
        mappingJackson2HttpMessageConverter = Arrays.stream(converters).filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    /**
     * Make a POST with authentication for this webservice
     *
     * @param path Path to Post
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder postAuthenticated(String path) {
        return post(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    /**
     * Make a POST with authentication for this webservice
     *
     * @param path Path to Post
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder postApiKey(String path) {
        return post(URI.create(path)).header(RequireValidUserTest.HEADER_API_KEY, getHeaderApiKeyContent());
    }

    /**
     * Make a GET with authentication for this webservice
     *
     * @param path Path to Post
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder getAuthenticated(String path) {
        return get(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    /**
     * Make a PUT with authentication for this webservice
     *
     * @param path Path to Post
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder putAuthenticated(String path) {
        return put(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    /**
     * Make a DELETE with authentication for this webservice
     *
     * @param path Path to Post
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder deleteAuthenticated(String path) {
        return delete(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    /**
     * Make a MULTIPART Form POST with authentication for this webservice
     *
     * @param path Path to Post
     * @return MockHttpServletRequestBuilder
     */
    protected MockMultipartHttpServletRequestBuilder fileUploadAuthenticated(String path) {
        return (MockMultipartHttpServletRequestBuilder) fileUpload(URI.create(path)).header(RequireValidUserTest.HEADER_AUTHORIZATION, getHeaderAuthenticationContent());
    }

    /**
     * Produces a validityDto quickly
     *
     * @param start start
     * @param end   end
     * @return ValidityDto
     */
    protected static ValidityDto makeValidityDto(LocalDateTime start, LocalDateTime end) {
        return new ValidityDto(
                start == null ? null : ValidityDtoMapperImpl.FORMATTER.format(start),
                end == null ? null : ValidityDtoMapperImpl.FORMATTER.format(end));
    }

    /**
     * Parse a string to a LocaleDateTime according the format of this Server
     *
     * @param txt Text to parse
     * @return LocalDateTime
     */
    protected static LocalDateTime toLocalDateTime(String txt) {
        return LocalDateTime.from(ValidityDtoMapperImpl.FORMATTER.parse(txt));
    }

    // ============================================================
    //	Callback for Tests
    // ============================================================
    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        daoInitializer.init();
    }

    @After
    public final void tearDown() throws Exception {
        Files.walkFileTree(fileSystem.getPath(props.getBasePath()), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

}

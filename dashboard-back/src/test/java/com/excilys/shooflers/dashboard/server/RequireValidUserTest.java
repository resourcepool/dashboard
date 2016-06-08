package com.excilys.shooflers.dashboard.server;

import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test the Security Feature.
 *
 * @author Mickael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class RequireValidUserTest {

    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * Client Rest
     */
    private MockMvc mockMvc;

    @Autowired
    private DashboardProperties props;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
    private String getHeaderAuthenticationContent(String login, String password) {
        return Base64.encodeBase64String((login + ":" + password).getBytes());
    }

    @Before
    public void setUp() throws Exception {
        // Set up the REST client
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void bundleNotAccessible() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void mediaNotAccessible() throws Exception {
        mockMvc.perform(get(URI.create("/media")))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void noNeedAuthWithResourceDoesntExists() throws Exception {
        mockMvc.perform(get(URI.create("/soidfusdoifus")))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void badValueAuthentification() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, "Basic dsfispodifqsdpoif"))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void badValueAuthentification2() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, "Basic"))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void wrongAuthentification() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, "Basic " + getHeaderAuthenticationContent("user", "user")))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void goodAuthentication() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, "Basic " + getHeaderAuthenticationContent()))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void badValueToken() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, "Token " + "perieprfdkjdg"))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void badValueToken2() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, "Token"))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void wrongToken() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, "Token " + getHeaderAuthenticationContent()))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void retrieveTokenAfterLogin() throws Exception {
        MvcResult result = mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, "Basic " + getHeaderAuthenticationContent()))
                .andExpect(status().isOk()).andReturn();

        String headerAuthorization = result.getResponse().getHeader(HEADER_AUTHORIZATION);

        System.out.println(headerAuthorization);

        assertThat(headerAuthorization, Matchers.startsWith("Token "));
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, headerAuthorization))
                .andExpect(status().isOk());
    }
}
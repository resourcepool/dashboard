package com.excilys.shooflers.dashboard.server;

import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.security.interceptor.CorsInterceptor;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test Resources without annotations.
 *
 * @author Mickael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class NoRequireTest {

    //============================================================
    // Consts
    //============================================================
    static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String URL_TO_TEST = "/test1/public";

    //============================================================
    // Attributes
    //============================================================
    /**
     * Client Rest
     */
    private MockMvc mockMvc;

    @Autowired
    private DashboardProperties props;

    @Autowired
    private WebApplicationContext webApplicationContext;

    //============================================================
    // Methods utils for tests
    //============================================================

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
        return "Basic " + Base64.encodeBase64String((login + ":" + password).getBytes());
    }

    //============================================================
    // Callbacks
    //============================================================
    @Before
    public void setUp() throws Exception {
        // Set up the REST client
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    //============================================================
    // Tests
    //============================================================
    @Test
    public void noNeedAuthenticationOnNonAnnotated() throws Exception {
        mockMvc.perform(get(URI.create("/test1/public")))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void noNeedAuthenticationOn404() throws Exception {
        mockMvc.perform(get(URI.create("/unknown")))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void authenticationAlwaysWorks() throws Exception {
        mockMvc.perform(get(URI.create(URL_TO_TEST)).header(HEADER_AUTHORIZATION, getHeaderAuthenticationContent()))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @Ignore("Not implemented yet...")
    public void tokenAlwaysWorks() throws Exception {
        mockMvc.perform(get(URI.create(URL_TO_TEST)).header(HEADER_AUTHORIZATION, "Token "))
                .andExpect(status().isOk())
        ;
        fail();
    }

    @Test
    public void apiKeyAlwaysWorks() throws Exception {
        mockMvc.perform(get(URI.create(URL_TO_TEST)).header(CorsInterceptor.HEADER_API_KEY, props.getApiKey()))
                .andExpect(status().isOk())
        ;
    }
}

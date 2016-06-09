package com.excilys.shooflers.dashboard.server.securityTest;

import com.excilys.shooflers.dashboard.server.DashboardApplication;
import com.excilys.shooflers.dashboard.server.endpointsResources.AnnotationSecurityOnlyOnMethodsTestController;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.security.interceptor.CorsInterceptor;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test Resources annoted by @RequireValidUser and @RequireValidApiKey.
 *
 * @author Mickael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class RequireValidUserOrApiKeyOnCombinationAnnotationsTest {

    //============================================================
    // Consts
    //============================================================
    static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String URL_TO_TEST = "erzer";

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
    public void baseNotAccessible() throws Exception {
        mockMvc.perform(get(URI.create("/test2/requireUser")))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void baseNoodAuthentication() throws Exception {
        mockMvc.perform(get(URI.create("/test2/requireUser")).header(HEADER_AUTHORIZATION, getHeaderAuthenticationContent()))
                .andExpect(status().isOk())
                .andExpect(content().string(AnnotationSecurityOnlyOnMethodsTestController.MESSAGE_OK))
        ;
    }

    @Test
    public void baseNoodApiKey() throws Exception {
        mockMvc.perform(get(URI.create("/test2/requireUser")).header(CorsInterceptor.HEADER_API_KEY, props.getApiKey()))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void combinationNotAccessible() throws Exception {
        mockMvc.perform(get(URI.create("/test2/requireUserOrApiKey")))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void goodAuthentication() throws Exception {
        mockMvc.perform(get(URI.create("/test2/requireUserOrApiKey")).header(HEADER_AUTHORIZATION, getHeaderAuthenticationContent()))
                .andExpect(status().isOk())
                .andExpect(content().string(AnnotationSecurityOnlyOnMethodsTestController.MESSAGE_OK))
        ;
    }

    @Test
    public void goodApiKey() throws Exception {
        mockMvc.perform(get(URI.create("/test2/requireUserOrApiKey")).header(CorsInterceptor.HEADER_API_KEY, props.getApiKey()))
                .andExpect(status().isOk())
        ;
    }
}

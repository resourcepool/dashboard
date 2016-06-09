package com.excilys.shooflers.dashboard.server;

import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.security.interceptor.CorsInterceptor;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test the Security Feature.
 *
 * @author Mickael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class RequireValidApikeyTest {

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
        return "Basic " + Base64.encodeBase64String((login + ":" + password).getBytes());
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
    public void loginAlsoWorks() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(HEADER_AUTHORIZATION, getHeaderAuthenticationContent("admin", "admin")))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void goodApikey() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(CorsInterceptor.HEADER_API_KEY, props.getApiKey()))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void wrongApikey() throws Exception {
        mockMvc.perform(get(URI.create("/bundle")).header(CorsInterceptor.HEADER_API_KEY, "phoenix wrong"))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void doesntWorkOnOnlyRequireUser() throws Exception {
        mockMvc.perform(post(URI.create("/bundle"))
                .content(("{}"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(CorsInterceptor.HEADER_API_KEY, props.getApiKey()))
                .andExpect(status().isUnauthorized())
        ;
    }
}

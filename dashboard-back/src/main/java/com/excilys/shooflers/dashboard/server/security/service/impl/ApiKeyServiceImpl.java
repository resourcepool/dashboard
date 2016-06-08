package com.excilys.shooflers.dashboard.server.security.service.impl;

import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.security.exception.IllegalApiKeyException;
import com.excilys.shooflers.dashboard.server.security.exception.NoHeaderProvidedException;
import com.excilys.shooflers.dashboard.server.security.service.ApiKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import static com.excilys.shooflers.dashboard.server.security.interceptor.CorsInterceptor.HEADER_API_KEY;
import static com.excilys.shooflers.dashboard.server.security.interceptor.CorsInterceptor.PARAM_API_KEY;

/**
 * @author Loïc Ortola on 08/06/2016.
 */
@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyServiceImpl.class);


    @Autowired
    private DashboardProperties props;

    private String apiKey;

    @PostConstruct
    public void init() {
        // Get ApiKey from properties
        apiKey = props.getApiKey();

    }

    @Override
    public void validateApiKey() {
        // Check Token
        // Retrieve Servlet Response
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestApiKey = getApiKey(request);

        if (requestApiKey == null) {
            throw new NoHeaderProvidedException();
        }
        LOGGER.debug("Checking apiKey : {}", requestApiKey);

        // Throws if not valid
        assertValidApiKey(requestApiKey);

    }


    private void assertValidApiKey(String requestApiKey) {
        // Throws if the apiKey does not exist in the database
        if (requestApiKey == null || !requestApiKey.equals(apiKey)) {
            // Wrong ApiKey
            LOGGER.warn("Wrong Api-Key provided: {}", requestApiKey);
            throw new IllegalApiKeyException(requestApiKey);
        }
    }

    /**
     * Get the api application id from request.
     *
     * @param request {@link javax.servlet.http.HttpServletRequest}
     * @return the api appId value
     */
    private String getApiKey(HttpServletRequest request) {
        String header = request.getHeader(HEADER_API_KEY);
        return header == null ? request.getParameter(PARAM_API_KEY) : header;
    }
}

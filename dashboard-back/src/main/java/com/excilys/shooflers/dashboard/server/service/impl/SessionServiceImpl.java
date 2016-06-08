package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.security.service.ApiKeyService;
import com.excilys.shooflers.dashboard.server.service.SessionService;
import com.excilys.shooflers.dashboard.server.security.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Service
public class SessionServiceImpl implements SessionService {

    public static final String REALM = "Dashboard";

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private UserAuthService userAuthService;


    @Override
    public void validateUser() {
        userAuthService.validateUser();
    }

    @Override
    public void validateApiKey() {
        apiKeyService.validateApiKey();
    }

}

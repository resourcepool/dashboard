package com.excilys.shooflers.dashboard.server.service;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface SessionService {

    /**
     * Assert if valid credentials were provided.
     * Credentials are either provided with basic auth headers at login time, or via a session-token
     */
    void validateUser();

    /**
     * Assert if valid ApiKey was provided.
     * Api-Key is either provided with an X-Api-Key header or via a api-key parameter
     */
    void validateApiKey();

}

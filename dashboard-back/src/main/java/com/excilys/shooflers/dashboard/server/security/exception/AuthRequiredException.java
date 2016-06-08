package com.excilys.shooflers.dashboard.server.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Authentication is required. Please provide credentials using Basic Auth")
public class AuthRequiredException extends RuntimeException {

    public AuthRequiredException() {
        super("Authentication is required. Please provide credentials using Basic Auth");
    }
}

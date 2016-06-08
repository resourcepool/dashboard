package com.excilys.shooflers.dashboard.server.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Illegal Api-Key provided. Please provide valid Api-Key")
public class IllegalApiKeyException extends RuntimeException {

    public IllegalApiKeyException() {
        super("Illegal Api-Key provided");
    }

    public IllegalApiKeyException(String apiKey) {
        super("Illegal Api-Key provided: " + apiKey);
    }

}

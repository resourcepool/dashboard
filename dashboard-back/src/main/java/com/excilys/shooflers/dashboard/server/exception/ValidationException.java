package com.excilys.shooflers.dashboard.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to thrown when a validation had not success.
 *
 * @author Mickael
 */
@SuppressWarnings("unused")
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error while validating your content. Please check your data structure to continue.")
public class ValidationException extends RuntimeException {
    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    protected ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

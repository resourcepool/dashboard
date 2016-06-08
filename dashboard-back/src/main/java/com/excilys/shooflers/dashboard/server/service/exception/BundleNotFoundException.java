package com.excilys.shooflers.dashboard.server.service.exception;


public class BundleNotFoundException extends RuntimeException {

    private String message;

    public BundleNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

package com.excilys.shooflers.dashboard.server.service.exception;


public class JsonMalformedException extends RuntimeException {

    private String message;

    public JsonMalformedException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

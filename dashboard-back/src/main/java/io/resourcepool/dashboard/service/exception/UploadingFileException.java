package io.resourcepool.dashboard.service.exception;


public class UploadingFileException extends RuntimeException {

    private String message;

    public UploadingFileException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

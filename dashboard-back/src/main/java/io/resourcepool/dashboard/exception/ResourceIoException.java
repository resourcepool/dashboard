package io.resourcepool.dashboard.exception;

/**
 * Exception when an IO exception occured for resources
 *
 * @author Mickael
 */
@SuppressWarnings("unused")
public class ResourceIoException extends RuntimeException {
    public ResourceIoException() {
        super();
    }

    public ResourceIoException(String message) {
        super(message);
    }

    public ResourceIoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceIoException(Throwable cause) {
        super(cause);
    }

    protected ResourceIoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

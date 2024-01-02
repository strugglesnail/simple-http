package com.struggle.http.exception;

public class NoSuchUrlAttributeException extends UrlException {

    public NoSuchUrlAttributeException() {
    }

    public NoSuchUrlAttributeException(String message) {
        super(message);
    }

    public NoSuchUrlAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchUrlAttributeException(Throwable cause) {
        super(cause);
    }
}

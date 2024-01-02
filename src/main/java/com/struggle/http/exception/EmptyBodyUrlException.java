package com.struggle.http.exception;

public class EmptyBodyUrlException extends UrlException {

    public EmptyBodyUrlException() {
    }

    public EmptyBodyUrlException(String message) {
        super(message);
    }

    public EmptyBodyUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyBodyUrlException(Throwable cause) {
        super(cause);
    }
}

package com.struggle.http.exception;

public class ResponseBodyFormatUrlException extends UrlException {

    public ResponseBodyFormatUrlException() {
    }

    public ResponseBodyFormatUrlException(String message) {
        super(message);
    }

    public ResponseBodyFormatUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseBodyFormatUrlException(Throwable cause) {
        super(cause);
    }
}

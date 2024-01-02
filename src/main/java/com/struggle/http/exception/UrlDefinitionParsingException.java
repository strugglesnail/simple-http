package com.struggle.http.exception;

public class UrlDefinitionParsingException extends UrlException {

    public UrlDefinitionParsingException() {
    }

    public UrlDefinitionParsingException(String message) {
        super(message);
    }

    public UrlDefinitionParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlDefinitionParsingException(Throwable cause) {
        super(cause);
    }
}

package com.struggle.http.exception;

public class NoSuchUrlDefinitionException extends UrlException {

    private final String name;

    public NoSuchUrlDefinitionException(String name) {
        super("No url named '" + name + "' available");
        this.name = name;
    }

    public NoSuchUrlDefinitionException(String name, String message) {
        super("No url named '" + name + "' available: " + message);
        this.name = name;
    }
}

package com.struggle.http.parse;

import com.struggle.http.exception.UrlException;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface UrlDefinitionReader {

    void loadUrlDefinitions(String[] urlLocations) throws UrlException;

    void loadUrlDefinitions(String urlLocations) throws UrlException;
}

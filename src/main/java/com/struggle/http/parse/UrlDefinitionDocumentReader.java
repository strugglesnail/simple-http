package com.struggle.http.parse;

import org.w3c.dom.Document;

import java.util.Map;

public interface UrlDefinitionDocumentReader {


    default void urlVariables(Map<String, Object> variables) {}

    void registerUrlDefinitions(Document document, UrlRegistry register) throws Exception;
}

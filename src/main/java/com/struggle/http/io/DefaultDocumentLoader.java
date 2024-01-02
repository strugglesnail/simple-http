package com.struggle.http.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DefaultDocumentLoader {

    private static final Log logger = LogFactory.getLog(DefaultDocumentLoader.class);

    public DefaultDocumentLoader() {
    }

    public Document loadDocument(InputSource inputSource, EntityResolver entityResolver, int validationMode, boolean namespaceAware) throws Exception {
        DocumentBuilderFactory factory = this.createDocumentBuilderFactory(validationMode, namespaceAware);
        if (logger.isTraceEnabled()) {
            logger.trace("Using JAXP provider [" + factory.getClass().getName() + "]");
        }
        DocumentBuilder builder = this.createDocumentBuilder(factory, entityResolver);
        return builder.parse(inputSource);
    }

    protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceAware);
        if (validationMode != 0) {
            factory.setValidating(true);
            if (validationMode == 3) {
                factory.setNamespaceAware(true);
                try {
                    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
                    factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
                } catch (IllegalArgumentException var6) {
                    ParserConfigurationException pcex = new ParserConfigurationException("Unable to validate using XSD: Your JAXP provider [" + factory + "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
                    pcex.initCause(var6);
                    throw pcex;
                }
            }
        }

        return factory;
    }

    protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory, EntityResolver entityResolver) throws ParserConfigurationException {
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        if (entityResolver != null) {
            docBuilder.setEntityResolver(entityResolver);
        }
        docBuilder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException ex) throws SAXException {
                    logger.warn("Ignored XML validation warning", ex);
                }

                @Override
                public void error(SAXParseException ex) throws SAXException {
                    throw ex;
                }

                @Override
                public void fatalError(SAXParseException ex) throws SAXException {
                    throw ex;
                }
            });
        return docBuilder;
    }
}

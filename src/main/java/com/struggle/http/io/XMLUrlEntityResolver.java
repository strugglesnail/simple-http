package com.struggle.http.io;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * 解析Url标签
 */
public class XMLUrlEntityResolver implements EntityResolver {

    private Logger logger = LoggerFactory.getLogger(XMLUrlEntityResolver.class);


    private static final String SYNC_URL_SYSTEM = "urls.xsd";


    private static final String SYNC_URL_DTD = "com/struggle/http/parse/urls.xsd";

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        try {
            if (systemId != null) {
                String lowerCaseSystemId = systemId.toLowerCase(Locale.ENGLISH);
                if (lowerCaseSystemId.contains(SYNC_URL_SYSTEM)) {
                    return getInputSource(SYNC_URL_DTD, publicId, systemId);
                }
            }
            return new InputSource();
        } catch (Exception e) {
            throw new SAXException(e.toString());
        }
    }

    private InputSource getInputSource(String path, String publicId, String systemId) {
        InputSource source = null;
        if (path != null) {
            try {
                InputStream in = Resources.getResourceAsStream(path);
                source = new InputSource(in);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
            } catch (IOException e) {
                // ignore, null is ok
                logger.info("path: " + path + "   读取不到");
            }
        }
        return source;
    }
}

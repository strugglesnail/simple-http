package com.struggle.http.parse;

import com.struggle.http.exception.UrlException;
import com.struggle.http.io.DefaultDocumentLoader;
import com.struggle.http.io.XMLUrlEntityResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * url对象定义读取器
 */
public class XmlUrlDefinitionReader implements UrlDefinitionReader {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private final UrlRegistry register;

    private EntityResolver entityResolver;

    private PathMatchingResourcePatternResolver resourceLoader;

    private DefaultDocumentLoader documentLoader;

    private DefaultUrlDefinitionDocumentReader documentReader;

    public XmlUrlDefinitionReader(UrlRegistry registry) {
        this(registry, null);
    }

    public XmlUrlDefinitionReader(UrlRegistry registry, Map<String, Object> variables) {
        this.register = registry;
        this.resourceLoader = new PathMatchingResourcePatternResolver();
        this.documentLoader = new DefaultDocumentLoader();
        documentReader = new DefaultUrlDefinitionDocumentReader();
        documentReader.urlVariables(variables);
    }

    @Override
    public void loadUrlDefinitions(String[] urlLocations) throws UrlException {
        try {
            Resource[] resources = resolveUrlLocations(urlLocations);
            for (Resource resource : resources) {
                parseXml(resource);
            }
        } catch (Exception e) {
            throw new UrlException("Xml parsed failed！", e);
        }
    }

    @Override
    public void loadUrlDefinitions(String urlLocations) throws UrlException {
        try {
            PathMatchingResourcePatternResolver resourceLoader = getResourceLoader();
            Resource[] resources = resourceLoader.getResources(urlLocations);
            for (Resource resource : resources) {
                parseXml(resource);
            }
        } catch (Exception e) {
            throw new UrlException("Xml parsed failed！", e);
        }
    }

    private void parseXml(Resource resource) throws Exception {
        InputStream inputStream = resource.getInputStream();
        InputSource inputSource = new InputSource(inputStream);
        Document document = documentLoader.loadDocument(inputSource, this.getEntityResolver(), 3, false);
        registerUrlDefinitions(document);
    }

    private void registerUrlDefinitions(Document doc) throws Exception {
        documentReader.registerUrlDefinitions(doc, register);
    }

    public EntityResolver getEntityResolver() {
        if (this.entityResolver == null) {
            ResourceLoader resourceLoader = this.getResourceLoader();
            if (resourceLoader != null) {
                this.entityResolver = new XMLUrlEntityResolver();
            }
        }
        return entityResolver;
    }

    public PathMatchingResourcePatternResolver getResourceLoader() {
        return resourceLoader;
    }

    public Resource[] resolveUrlLocations(String[] urlLocations) {
        return Stream.of(Optional.ofNullable(urlLocations).orElse(new String[0])).flatMap((location) -> {
            return Stream.of(this.getResources(location));
        }).toArray((x$0) -> {
            return new Resource[x$0];
        });
    }

    private Resource[] getResources(String location) {
        try {
            return this.getResourceLoader().getResources(location);
        } catch (IOException var3) {
            return new Resource[0];
        }
    }
}

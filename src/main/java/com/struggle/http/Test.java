package com.struggle.http;

import com.struggle.http.parse.*;
import com.struggle.http.session.DefaultUrlSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class Test {

    protected static final Log logger = LogFactory.getLog(Test.class);

    public static void main(String[] args) throws Exception {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/beans*.xml");
//
//        Object x = context.getBean("x");
//        System.out.println(x);

        // 读取xml文件
//        URL resource = Test.class.getClassLoader().getResource("classpath:beans.xml");
//        System.out.println(resource);

//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        Resource[] resources = resolver.getResources("classpath:/urls*.xml");
//        System.out.println(resources[0].getFilename());
//        Resource resource = resources[0];
//        InputSource inputSource = new InputSource(resource.getInputStream());
//        DocumentLoader documentLoader = new DefaultDocumentLoader();
//        ResourceEntityResolver entityResolver = new ResourceEntityResolver(resolver);
//        SimpleSaxErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
//        Document document = documentLoader.loadDocument(inputSource, entityResolver, errorHandler, 3, false);
//        System.out.println(document.getDocumentElement().getTagName());
//        System.out.println(document.getDocumentElement().getAttribute("type"));
//        System.out.println(document.getDocumentElement().getElementsByTagName("url").item(1).getLocalName());
        // 1. 解析
        UrlDefinitionRegistry registry = new SimpleUrlDefinitionRegistry();
        UrlMapperRegistry mapperRegistry = new UrlMapperRegistry();
        UrlRegistry registry1 = new UrlRegistry(registry, mapperRegistry);
        XmlUrlDefinitionReader reader = new XmlUrlDefinitionReader(registry1);
        reader.loadUrlDefinitions("classpath*:config/urls/**/*.xml");
        // 代理
        DefaultUrlSession session = new DefaultUrlSession(registry1);
        Map<String, Object> param = new HashMap<>();
        param.put("type", 2);
        Map<Object, Object> selectMap = session.selectMap("TEST.test", param);
        System.out.println(selectMap);
    }



}

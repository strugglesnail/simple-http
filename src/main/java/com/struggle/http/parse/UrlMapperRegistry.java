package com.struggle.http.parse;

import com.struggle.http.exception.NoSuchUrlDefinitionException;
import com.struggle.http.exception.UrlException;
import com.struggle.http.proxy.UrlsProxyFactory;
import com.struggle.http.session.UrlSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlMapperRegistry {

    private final Map<Class, UrlsProxyFactory> urlMap = new ConcurrentHashMap(64);


    public <T> void registerUrlMapper(Class<T> type, UrlsProxyFactory proxyFactory) throws UrlException {
        urlMap.put(type, proxyFactory);
    }

    public <T> T getUrlMapper(Class<T> type, UrlSession session) throws NoSuchUrlDefinitionException {
        UrlsProxyFactory proxyFactory = urlMap.get(type);
        return proxyFactory.newInstance(session);
    }

    public <T> boolean hasUrlMapper(Class<T> type) {
        return urlMap.containsKey(type);
    }
}

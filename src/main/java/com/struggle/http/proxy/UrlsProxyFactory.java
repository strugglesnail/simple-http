package com.struggle.http.proxy;

import com.struggle.http.parse.UrlDefinitionHolder;
import com.struggle.http.session.UrlSession;

import java.lang.reflect.Proxy;

public class UrlsProxyFactory {

    private final Class urlsInterface;
    private final UrlDefinitionHolder definitionHolder;

    public <T> UrlsProxyFactory(UrlDefinitionHolder definitionHolder) {
        this.definitionHolder = definitionHolder;
        this.urlsInterface = definitionHolder.getMapperClass();
    }

    protected <T> T newInstance(UrlsProxy urlsProxy) {
        return (T) Proxy.newProxyInstance(this.urlsInterface.getClassLoader(), new Class[]{this.urlsInterface}, urlsProxy);
    }

    public <T> T newInstance(UrlSession session) {
        UrlsProxy urlsProxy = new UrlsProxy(session, definitionHolder);
        return this.newInstance(urlsProxy);
    }
}

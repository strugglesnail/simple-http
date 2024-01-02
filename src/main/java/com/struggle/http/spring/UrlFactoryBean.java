package com.struggle.http.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

public class UrlFactoryBean<T> extends UrlSessionDaoSupport implements FactoryBean<T> {


    private Class<T> urlInterface;

    public UrlFactoryBean(Class<T> urlInterface) {
        this.urlInterface = urlInterface;
    }

    @Override
    protected void checkDaoConfig() {
        super.checkDaoConfig();
        Assert.notNull(this.urlInterface, "Property 'mapperInterface' is required");
//        UrlSession urlSession = getUrlSession();
//        try {
//            T mapper = urlSession.getMapper(urlInterface);
//        } catch (Exception var6) {
//            this.logger.error("Error get the url '" + this.urlInterface + "'", var6);
//            throw new IllegalArgumentException(var6);
//        } finally {
//            ErrorContext.instance().reset();
//        }
    }

    @Override
    public T getObject() throws Exception {
        T mapper = this.getUrlSession().getMapper(urlInterface);
        return mapper;
    }

    @Override
    public Class<?> getObjectType() {
        return urlInterface;
    }
}

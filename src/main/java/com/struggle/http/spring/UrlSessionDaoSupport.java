package com.struggle.http.spring;

import com.struggle.http.session.UrlSession;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

public abstract class UrlSessionDaoSupport extends DaoSupport {

    private UrlSession urlSession;

    public UrlSessionDaoSupport() {

    }

    public void setUrlSession(UrlSession urlSession) {
        this.urlSession = urlSession;
    }

    public UrlSession getUrlSession() {
        return urlSession;
    }

    @Override
    protected void checkDaoConfig() {
        Assert.notNull(this.urlSession, "Property 'urlSession are required");
    }
}

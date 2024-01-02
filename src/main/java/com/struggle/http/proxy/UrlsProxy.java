package com.struggle.http.proxy;

import com.struggle.http.parse.UrlDefinitionHolder;
import com.struggle.http.session.UrlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UrlsProxy  implements InvocationHandler {


    private final Class urlsInterface;
    private final UrlSession session;
    private final UrlDefinitionHolder definitionHolder;

    public UrlsProxy(UrlSession session, UrlDefinitionHolder definitionHolder) {
        this.session = session;
        this.definitionHolder = definitionHolder;
        this.urlsInterface = definitionHolder.getMapperClass();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 基类方法直接返回
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        UrlMethod urlMethod = new UrlMethod(urlsInterface, method);
        return urlMethod.execute(session, args);
    }
}

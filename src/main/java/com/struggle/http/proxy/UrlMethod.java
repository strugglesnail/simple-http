package com.struggle.http.proxy;

import cn.hutool.core.annotation.AnnotationUtil;
import com.struggle.http.io.TypeParameterResolver;
import com.struggle.http.annotation.UrlHeads;
import com.struggle.http.annotation.UrlParam;
import com.struggle.http.session.UrlSession;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;


public class UrlMethod {

    private MethodSignature method;

    public UrlMethod(Class<?> mapperInterface, Method method) {
        this.method = new MethodSignature(mapperInterface, method);
    }

    public Object execute(UrlSession session, Object[] args) {
        String namespaceId = this.method.buildNamespaceId();
        Object result = null;
        if (args == null) {
            if (this.method.returnsMany()) {
                result = session.selectList(namespaceId);
            } else if (this.method.returnsMap()) {
                result = session.selectMap(namespaceId);
            } else {
                result = session.selectOne(namespaceId);
            }
        } else {
            if (args.length >= 1) {
                // 1. 查找所有参数是否有@UrlParam/@UrlHeads注解Map
                Map paramMap = Arrays.stream(args).filter(arg -> arg instanceof Map).filter(arg -> AnnotationUtil.hasAnnotation(arg.getClass(), UrlParam.class)).findFirst().map(arg -> (Map) arg).orElse(null);
                Map headMap = Arrays.stream(args).filter(arg -> arg instanceof Map).filter(arg -> AnnotationUtil.hasAnnotation(arg.getClass(), UrlHeads.class)).findFirst().map(arg -> (Map) arg).orElse(null);
                // 2. 如果没找到，则进一步判断
                if (paramMap == null) {
                    if (args[0] instanceof Map) {
                        paramMap = (Map) args[0];
                    }
                }
                if (headMap == null) {
                    if (args.length >= 2 && args[1] instanceof Map) {
                        headMap = (Map) args[1];
                    }
                }
                if (this.method.returnsMany()) {
                    result = session.selectList(namespaceId, paramMap, headMap);
                } else if (this.method.returnsMap()) {
                    result = session.selectMap(namespaceId, paramMap, headMap);
                } else {
                    result = session.selectOne(namespaceId, paramMap, headMap);
                }
            }
        }
        return result;
    }

    public static class MethodSignature {
        private final boolean returnsMany;
        private final boolean returnsMap;
        private final Class<?> returnType;
        private final String namespaceId;

        public MethodSignature(Class<?> mapperInterface, Method method) {
            Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface);
            if (resolvedReturnType instanceof Class<?>) {
                this.returnType = (Class<?>) resolvedReturnType;
            } else if (resolvedReturnType instanceof ParameterizedType) {
                this.returnType = (Class<?>) ((ParameterizedType) resolvedReturnType).getRawType();
            } else {
                this.returnType = method.getReturnType();
            }
            this.returnsMany = List.class.isAssignableFrom(this.returnType) || this.returnType.isArray();

            this.returnsMap = Map.class.isAssignableFrom(this.returnType);

            String namespace = method.getDeclaringClass().getCanonicalName();
            String id = method.getName();
            namespaceId =  namespace + "." + id;
        }


        public Class<?> getReturnType() {
            return returnType;
        }

        public boolean returnsMany() {
            return returnsMany;
        }

        public boolean returnsMap() {
            return returnsMap;
        }

        // statement
        private String buildNamespaceId() {
            return namespaceId;
        }
    }

}

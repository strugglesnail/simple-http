package com.struggle.http.parse;

/**
 * url对象注册中心
 */
public class UrlRegistry {

    UrlDefinitionRegistry definitionRegistry;

    UrlMapperRegistry mapperRegistry;

    public UrlRegistry(UrlDefinitionRegistry definitionRegistry, UrlMapperRegistry mapperRegistry) {
        this.definitionRegistry = definitionRegistry;
        this.mapperRegistry = mapperRegistry;
    }

    public UrlDefinitionRegistry getDefinitionRegistry() {
        return definitionRegistry;
    }

    public UrlMapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }
}

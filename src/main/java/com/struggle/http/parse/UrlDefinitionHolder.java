package com.struggle.http.parse;

import java.util.Map;

/**
 * urls文件映射
 */
public class UrlDefinitionHolder {

    // urls命名空间: 映射接口
    private String namespace;
    private Class mapperClass;

    // url所属模块
    private String type;


    // urls描述
    private String description;

    // urls下所有的url元素映射
    private Map<String, UrlDefinition> urlDefinitionMap;


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(Class mapperClass) {
        this.mapperClass = mapperClass;
    }

    public Map<String, UrlDefinition> getUrlDefinitionMap() {
        return urlDefinitionMap;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrlDefinitionMap(Map<String, UrlDefinition> urlDefinitionMap) {
        this.urlDefinitionMap = urlDefinitionMap;
    }
}

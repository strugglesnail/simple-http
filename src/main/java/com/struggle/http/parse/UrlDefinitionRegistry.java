package com.struggle.http.parse;

import com.struggle.http.exception.NoSuchUrlDefinitionException;
import com.struggle.http.exception.UrlException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * Url元素对象注册器
 */
public interface UrlDefinitionRegistry {

    // 通过模块名称注册UrlDefinitionHolder
    void registerUrlDefinitionByType(String type, UrlDefinitionHolder definition) throws UrlException;

    // 通过命名空间(映射mapper)注册UrlDefinitionHolder
    void registerUrlDefinitionByNamespace(String namespace, UrlDefinitionHolder definition) throws UrlException;

    // 根据模块名称获取UrlDefinitionHolder
    UrlDefinitionHolder getUrlDefinitionHolder(String type) throws NoSuchBeanDefinitionException;

    // 根据模块名称或者命名空间获取UrlDefinition
    UrlDefinition getUrlDefinition(String typeOrNamespaceAndId) throws NoSuchUrlDefinitionException;
}

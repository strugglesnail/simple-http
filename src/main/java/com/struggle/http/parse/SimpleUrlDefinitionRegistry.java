package com.struggle.http.parse;

import com.struggle.http.exception.NoSuchUrlDefinitionException;
import com.struggle.http.exception.UrlException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleUrlDefinitionRegistry implements UrlDefinitionRegistry {

    // 模块名称
    private final Map<String, UrlDefinitionHolder> urlDefinitionTypeMap = new ConcurrentHashMap(64);
    // 命名空间
    private final Map<String, UrlDefinitionHolder> urlDefinitionNamespaceMap = new ConcurrentHashMap(64);



    @Override
    public void registerUrlDefinitionByType(String type, UrlDefinitionHolder definition) throws UrlException {
        urlDefinitionTypeMap.put(type, definition);
    }
    @Override
    public void registerUrlDefinitionByNamespace(String namespace, UrlDefinitionHolder definition) throws UrlException {
        urlDefinitionNamespaceMap.put(namespace, definition);
    }

    @Override
    public UrlDefinitionHolder getUrlDefinitionHolder(String typeOrNamespace) throws NoSuchUrlDefinitionException {
        UrlDefinitionHolder bd = this.urlDefinitionTypeMap.get(typeOrNamespace);
        if (bd == null) {
            bd = urlDefinitionNamespaceMap.get(typeOrNamespace);
        }
        if (bd == null) {
            throw new NoSuchUrlDefinitionException(typeOrNamespace);
        } else {
            return bd;
        }
    }

    @Override
    public UrlDefinition getUrlDefinition(String typeOrNamespaceAndId) throws NoSuchUrlDefinitionException {
        int lastIndex = typeOrNamespaceAndId.lastIndexOf(".");
        String typeOrNamespace = typeOrNamespaceAndId.substring(0, lastIndex);
        UrlDefinitionHolder udh = getUrlDefinitionHolder(typeOrNamespace);
        Map<String, UrlDefinition> definitionMap = udh.getUrlDefinitionMap();
        String id = typeOrNamespaceAndId.substring(lastIndex+1);
        UrlDefinition definition = definitionMap.get(id);
        if (definition == null) {
            throw new NoSuchUrlDefinitionException(id, "Please check if your mapping method matches the element id！");
        }
        return definition;
    }
}

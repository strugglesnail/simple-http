package com.struggle.http.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用于配置同步连接的信息
 */
@ConfigurationProperties(prefix = "sync")
public class SyncProperties {

    private String[] urlLocations;

    Map<String, Object> config;


    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public String[] getUrlLocations() {
        return urlLocations;
    }

    public void setUrlLocations(String[] urlLocations) {
        this.urlLocations = urlLocations;
    }

    public Map<String, Object> transformMapKeys() {
        return transformMapKeys(config, ".");
    }

    public Map<String, Object> transformMapKeys(Map<String, Object> sourceMap, String separator) {
        Map<String, Object> transformedMap = new HashMap<>();
        if (sourceMap == null) {
            return transformedMap;
        }
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                Map<String, Object> nestedTransformedMap = transformMapKeys((Map<String, Object>) value, separator);
                for (Map.Entry<String, Object> nestedEntry : nestedTransformedMap.entrySet()) {
                    String transformedKey = key + separator + nestedEntry.getKey();
                    transformedMap.put(transformedKey, nestedEntry.getValue());
                }
            } else {
                transformedMap.put(key, value);
            }
        }
        return transformedMap;
    }


}

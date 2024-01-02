package com.struggle.http.session;

import cn.hutool.core.collection.CollectionUtil;
import com.struggle.http.exception.UrlException;
import com.struggle.http.executor.HttpExecutor;
import com.struggle.http.parse.UrlDefinition;
import com.struggle.http.parse.UrlRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认session
 */
public class DefaultUrlSession implements UrlSession {

    // Url定义对象注册中心
    private UrlRegistry registry;

    public DefaultUrlSession(UrlRegistry registry) {
        this.registry = registry;
    }

    @Override
    public <T> T selectOne(String statement) {
        return selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Map<String, Object> urlParam) {
        return selectOne(statement, null, null);
    }

    @Override
    public <T> T selectOne(String statement, Map<String, Object> urlParam, Map<String, Object> urlHeads) {
//        Map<String, Object> headMap = getMap(heads);
        Object resultData = getResultData(statement, urlParam, urlHeads);
        return (T) resultData;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement) {
        // http执行结果
        return selectMap(statement, null, null);
    }
    @Override
    public <K, V> Map<K, V> selectMap(String statement, Map<String, Object> urlParam) {
        // http执行结果
        return selectMap(statement, urlParam, null);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Map<String, Object> urlParam, Map<String, Object> urlHeads) {
        // http执行结果
//        Map<String, Object> headMap = getMap(heads);
        Object resultData = getResultData(statement, urlParam, urlHeads);
        return (Map<K, V>) resultData;
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return selectList(statement, null, null);
    }

    @Override
    public <E> List<E> selectList(String statement, Map<String, Object> urlParam, Map<String, Object> urlHeads) {
//        Map<String, Object> headMap = getMap(heads);
        Object result = getResultData(statement, urlParam, urlHeads);
        if (result == null) {
            return null;
        }
        if (!(result instanceof List)) {
            throw new UrlException("result data convert error！");
        }
        return (List<E>) result;
    }

//    private Map<String, Object> getMap(Map<String, Object> urlHeads) {
//        Map<String, Object> headMap = new HashMap<>();
//        if (CollectionUtils.isEmpty(heads)){
//            return null;
//        }
//        for (Head head : heads) {
//            headMap.put(head.getKey(), head.getValue());
//        }
//        return headMap;
//    }

    private Object getResultData(String statement, Map<String, Object> urlParamMap, Map<String, Object> headMap) {
        UrlDefinition definition = registry.getDefinitionRegistry().getUrlDefinition(statement);

        // 参数追加
        if (CollectionUtil.isNotEmpty(urlParamMap)) {

            Map<String, Object> params = CollectionUtil.isEmpty(definition.getParams()) ? new HashMap<>() : definition.getParams() ;
            params.putAll(urlParamMap);
            definition.setParams(params);
        }

        if (CollectionUtil.isNotEmpty(headMap)) {
            // 头部参数追加
            Map<String, Object> heads = definition.getHeads();
            heads.putAll(headMap);
            definition.setHeads(heads);
        }
        HttpExecutor executor = definition.getExecutor();
        Object result = executor.execute(definition);
//        Object o = ExecutorUtil.httpExecute(definition);
        return result;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        if (registry.getMapperRegistry().hasUrlMapper(type)) {
            return registry.getMapperRegistry().getUrlMapper(type, this);
        }
        throw new UrlException();
    }
}

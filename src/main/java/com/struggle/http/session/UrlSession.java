package com.struggle.http.session;

import java.util.List;
import java.util.Map;

public interface UrlSession {

    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Map<String, Object> urlParam);

    <T> T selectOne(String statement, Map<String, Object> urlParam, Map<String, Object> urlHeads);

    <K, V> Map<K, V> selectMap(String statement);

    <K, V> Map<K, V> selectMap(String statement, Map<String, Object> urlParam);

    <K, V> Map<K, V> selectMap(String statement, Map<String, Object> urlParam, Map<String, Object> urlHeads);

    <E> List<E> selectList(String statement);

    <E> List<E> selectList(String statement, Map<String, Object> urlParam, Map<String, Object> urlHeads);

    <T> T getMapper(Class<T> type);
}

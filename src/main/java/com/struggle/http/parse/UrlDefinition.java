package com.struggle.http.parse;

import com.struggle.http.executor.HttpExecutor;

import java.util.List;
import java.util.Map;

/**
 * URL定义
 */
public interface UrlDefinition {


    // URL名称
    String getId();

    // 请求方式
    String getRequestMethod();

    // 请求重试次数
    String getRetry();

    // 响应类型
    String getResultType();

    // URL描述
    String getDescription();

    // 请求URL地址
    String getRequestUrl();

    // 请求参数
    Map<String, Object> getParams();

    // 请求头参数
    Map<String, Object> getHeads();

    // 数据响应结果层级结构
    List<String> getResponseBody();

    // 请求执行器
    HttpExecutor getExecutor();


    // URL名称
    void setId(String id);

    // 请求方式
    void setRequestMethod(String requestMethod);

    // 请求重试次数
    void setRetry(String retry);

    // 响应类型
    void setResultType(String resultType);

    // URL描述
    void setDescription(String desc);

    // 请求URL地址
    void setRequestUrl(String requestUrl);

    // 请求参数
    void setParams(Map<String, Object> params);

    // 请求头参数
    void setHeads(Map<String, Object> heads);

    // URL依赖关联
//    void setRefs(List<UrlDefinition> refs);

    // 响应数据结果层级结构
    void setResponseBody(List<String> responseBodies);

    // 请求执行器
    void setExecutor(HttpExecutor executor);
}

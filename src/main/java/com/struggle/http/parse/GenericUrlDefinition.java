package com.struggle.http.parse;


import com.struggle.http.executor.HttpExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 默认实现URL定义
 */
public class GenericUrlDefinition implements UrlDefinition {

    // 标识：映射urlMapper方法
    private String id;

    // 请求描述
    private String desc;

    // 请求路径
    private String requestUrl;
    // 请求头
    private Map<String, Object> heads;

    // 请求参数
    private Map<String, Object> params;

    // 请求方式：GET/POST
    private String requestMethod;

    // 请求重试次数
    private String retry;

    // 响应类型
    private String resultType;

    // 响应数据的数据结构
    private List<String> responseBody;

    // 请求执行器
    private HttpExecutor executor;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public String getRequestUrl() {
        return requestUrl;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public Map<String, Object> getHeads() {
        return heads == null ? new HashMap<>() : heads;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRequestMethod() {
        return requestMethod;
    }

    @Override
    public String getRetry() {
        return retry;
    }

    @Override
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public void setRetry(String retry) {
        this.retry = retry;
    }

    @Override
    public String getResultType() {
        return resultType;
    }

    @Override
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public void setDescription(String desc) {
        this.desc = desc;
    }

    @Override
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public void setHeads(Map<String, Object> heads) {
        this.heads = heads;
    }

    @Override
    public List<String> getResponseBody() {
        return responseBody;
    }

    @Override
    public void setResponseBody(List<String> responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public HttpExecutor getExecutor() {
        return executor;
    }

    @Override
    public void setExecutor(HttpExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String toString() {
        return "GenericUrlDefinition{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", heads=" + heads +
                ", params=" + params +
                ", requestMethod='" + requestMethod + '\'' +
                ", retry='" + retry + '\'' +
                ", resultType='" + resultType + '\'' +
                ", responseBody=" + responseBody +
                ", executor=" + executor.getClass().getSimpleName() +
                '}';
    }
}

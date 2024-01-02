package com.struggle.http.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.struggle.http.exception.HttpException;
import com.struggle.http.exception.UrlException;
import com.struggle.http.io.Resources;
import com.struggle.http.ognl.OgnlUtil;
import com.struggle.http.parse.UrlDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ExecutorUtil {

    private static Logger logger = LoggerFactory.getLogger(ExecutorUtil.class);


    public static Object httpExecute(UrlDefinition definition) {
        String url = definition.getRequestUrl();
        Map<String, Object> heads = definition.getHeads();
        Map<String, Object> params = definition.getParams();
        String requestMethod = definition.getRequestMethod();

        String json = JSONUtil.toJsonPrettyStr(params);
        String requestBody = XmlUtils.isXmlRequest(heads) ? XmlUtils.jsonToXmlForAttr(json) : json;
        String responseBody;
        if (StringUtils.isEmpty(requestMethod)) {
            throw new HttpException("没有指定请求类型");
        }

        switch (definition.getRequestMethod()) {
            case "GET":
                HttpRequest get = HttpUtil.createGet(url);
                heads.forEach((h, v) -> get.header(h, (String) v));
                responseBody = get
                        .body(requestBody)
                        .form(params)
                        .execute()
                        .body();
                break;
            case "POST":
                HttpRequest post = HttpUtil.createPost(url);
                heads.forEach((h, v) -> post.header(h, (String) v));
                setBodyType(heads, post, params, requestBody);
                responseBody = post
                        .execute()
                        .body();
                break;
            default:
                throw new IllegalStateException("Unexpected requestMethod: " + requestMethod);
        }
        Object result = null;
        if (StringUtils.isNotBlank(responseBody)) {
            String resultType = definition.getResultType();
            if (StringUtils.isNotBlank(resultType)) {
                Class<?> resultTypeClass;
                try {
                    resultTypeClass = Resources.classForName(resultType);
                } catch (ClassNotFoundException e) {
                    throw new UrlException("ClassNotFoundException: " + resultType, e);
                }
                Object o = JSON.parseObject(responseBody, resultTypeClass);
                return o;
            }

            if (XmlUtils.isJsonObject(responseBody)) { // 响应结果是对象
                Map<String, Object> resultMap = JSON.parseObject(responseBody, Map.class);
                result = resultMap;
            } else  if (XmlUtils.isJsonArray(responseBody)) { // 响应结果是列表
                result = JSON.parseObject(responseBody, List.class);
            } else  if (XmlUtils.isXmlRequest(heads)) { // 响应结果是XML
                result = XmlUtils.xmlToJsonReMap(responseBody);
            } else {
                throw new UrlException("Http response body format error！" + responseBody);
            }
            if (result instanceof Map) {
                List<String> responseData = definition.getResponseBody();
                if (CollectionUtil.isNotEmpty(responseData)) {
                    String bodyResult = responseData.get(0);
                    Object newResult = OgnlUtil.getValue(bodyResult, result);
                    if (newResult == null) {
                        logger.error("Unexpected result data: {}", responseBody);
                        return null;
                    }
                    result = newResult;
                }
            }
        }
        return result;
    }

    private static HttpRequest setBodyType(Map<String, Object> heads, HttpRequest post, Map<String, Object> form, String body) {
        if (CollectionUtil.isNotEmpty(heads) && heads.containsKey("Content-Type") && ((String) heads.get("Content-Type")).startsWith("multipart/form-data")) {
            post.form(form);
        } else {
            post.body(body);
        }
        return post;
    }
}

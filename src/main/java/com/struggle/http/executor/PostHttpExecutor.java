package com.struggle.http.executor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.struggle.http.parse.UrlDefinition;
import com.struggle.http.util.XmlUtils;
import com.google.common.collect.ImmutableMap;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * POST请求执行器
 */
public class PostHttpExecutor extends AbstractHttpExecutor {


    @Override
    public String requestMethod() {
        return "POST";
    }

    @Override
    public String httpResponse(UrlDefinition definition) {
        String url = definition.getRequestUrl();
        Map<String, Object> heads = definition.getHeads();
        Map<String, Object> params = definition.getParams();
        String json = JSONUtil.toJsonPrettyStr(params);
        String requestBody = XmlUtils.isXmlRequest(heads) ? XmlUtils.jsonToXmlForAttr(json) : json;
        HttpRequest post = HttpUtil.createPost(url);
        heads.forEach((h, v) -> post.header(h, (String) v));
        setBodyType(heads, post, params, requestBody);
        return post.execute().body();
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

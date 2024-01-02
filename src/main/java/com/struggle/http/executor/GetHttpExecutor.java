package com.struggle.http.executor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.struggle.http.parse.UrlDefinition;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * GET请求执行器
 */
public class GetHttpExecutor extends AbstractHttpExecutor {


    @Override
    public String requestMethod() {
        return "GET";
    }

    @Override
    public String httpResponse(UrlDefinition definition) {
        String url = definition.getRequestUrl();
        Map<String, Object> heads = definition.getHeads();
        Map<String, Object> params = definition.getParams();
        url = HttpUtil.urlWithForm(url, params, Charset.defaultCharset(), true);
        HttpRequest get = HttpUtil.createGet(url);
        heads.forEach((h, v) -> get.header(h, (String) v));
        return get.execute().body();
    }

}

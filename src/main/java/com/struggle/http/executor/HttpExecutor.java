package com.struggle.http.executor;

import com.struggle.http.parse.UrlDefinition;

public interface HttpExecutor {

    // 请求类型
    String requestMethod();

    // 执行http请求
    Object execute(UrlDefinition definition);
}

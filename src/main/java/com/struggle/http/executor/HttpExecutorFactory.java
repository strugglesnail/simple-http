package com.struggle.http.executor;

/**
 * 默认的请求执行器工厂
 */
public class HttpExecutorFactory {


    public static HttpExecutor newExecutor(String requestMethod) {
        HttpExecutor executor;
        switch (requestMethod) {
            case "GET":
                executor = new GetHttpExecutor();
                break;
            case "POST":
                executor = new PostHttpExecutor();
                break;
            default:
                executor = null;
        }
        return executor;
    }
}

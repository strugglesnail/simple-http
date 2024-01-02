package com.struggle.http.exception;


/**
 * http请求异常
 * 
 * @author struggle
 */
public class HttpException extends RuntimeException {

    public HttpException() {
    }

    public HttpException(String message) {
        super(message);
    }
}

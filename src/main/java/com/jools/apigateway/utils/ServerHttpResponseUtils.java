package com.jools.apigateway.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/6 10:30
 * @description: TODO
 */
public class ServerHttpResponseUtils {

    //错误请求 - 400
    public static void badResponseAns(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
    }

    //鉴权未通过请求 - 401
    public static void noAuthAns(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    //返回后台错误 - 500
    public static void internelServerError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

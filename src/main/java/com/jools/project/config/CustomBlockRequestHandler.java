package com.jools.project.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/18 17:38
 * @description: TODO
 */
@Slf4j
@Component
public class CustomBlockRequestHandler implements BlockRequestHandler {
    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        Exception exception = (Exception) throwable;
        log.info("调用了自定义 BlockRequestHandler 处理异常: {}", exception.getClass().getName());

        // 获取请求相关信息
        ServerHttpRequest request = serverWebExchange.getRequest();
        String url = request.getURI().getPath();
        String methodName = request.getMethod().name();
        String hostStr = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
        String message;


        log.error("触发流控规则: 请求 url:{} , Method-Type:{}, 来源host:{}", url, methodName, hostStr);

        //根据不同的流控规则返回信息
        if (exception instanceof DegradeException) {
            log.info("处理 DegradeException，返回 503");
            message = "服务降级，请稍后再试";
            return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE) // 返回 503
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(message));
        } else if (exception instanceof FlowException) {
            log.info("处理 FlowException，返回 429");
            message = "请求过于频繁，请稍后再试";
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS) // 返回 429
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(message));
        } else {
            log.info("处理其他 Sentinel 异常");
            // 其他 Sentinel 异常处理
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue("系统繁忙"));
        }
    }

    // 注册自定义 BlockRequestHandler
    @PostConstruct
    public void init() {
        log.info("注册自定义 BlockRequestHandler");
        GatewayCallbackManager.setBlockHandler(new CustomBlockRequestHandler());
    }
}

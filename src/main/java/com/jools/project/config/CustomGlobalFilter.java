package com.jools.project.config;

import com.jools.project.utils.ServerHttpResponseUtils;
import com.jools.project.utils.ValidatorUtils;
import com.jools.joolsclientsdk.uitls.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/4 23:01
 * @description: TODO
 */

@Configuration
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    //白名单，只有来源于以下ip的请求可以通过
    private static final List<String> WHITE_LIST = Arrays.asList("127.0.0.1");

    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }

    /**
     * 全局过滤器 - 成功拦截所有进入该网关的请求
     * 完成以下功能:
     * 1 - 用户发送请求到 API 网关
     * 2 - 请求日志
     * 3 - (黑白名单）
     * 4 - 用户鉴权 (判断 ak,sk 是否合法)
     * 5 - 请求的模拟接口是否存在
     * 6 - 请求转发，调用模拟接口
     * 7 - 相应日志
     * 8 - 调用成功，接口调用次数 + 1
     * 9 - 调用失败，返回一个规范的错误码
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        log.info("custom global filter");
        //请求日志
        ServerHttpRequest request = exchange.getRequest();
        String id = request.getId();
        String pathValue = request.getPath().value();
        String methodName = request.getMethod().name();
        String hostStr = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        log.info("请求id: {}", id);
        log.info("请求来源: {}", hostStr);
        log.info("请求URI: {}", pathValue);
        log.info("请求方法: {}", methodName);
        log.info("请求携带参数: {}", queryParams);

        //返回响应
        ServerHttpResponse response = exchange.getResponse();

        //3 - (黑白名单）
        if (!WHITE_LIST.contains(hostStr)) { //响应 403
            ServerHttpResponseUtils.badResponseAns(response);
            return response.setComplete();
        }

        //4 - 用户鉴权 (判断 ak,sk 是否合法)
        //基于请求头获取参数
        HttpHeaders headers = request.getHeaders();

        //TODO: accessKey 可以先到数据库去校验 - 后期可以使用 OpenFeign 调用后台模拟接口平台完成校验
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");

        //校验权限，这里简化，直接判断与测试 accessKey 是否一致
        if (accessKey == null || !accessKey.equals(SignUtil.TEST_ACCESS_KEY)) { //如果未通过，返回鉴权失败异常
            ServerHttpResponseUtils.noAuthAns(response);
            return response.setComplete();
        }

        //校验随机数
        if (nonce == null || Long.parseLong(nonce) > 10000) {
            ServerHttpResponseUtils.noAuthAns(response);
            return response.setComplete();
        }

        //校验时间戳如果时间差距大于 3 分钟报错
        if (!ValidatorUtils.isWithinThreeMinutes(timestamp)) {
            ServerHttpResponseUtils.noAuthAns(response);
            return response.setComplete();
        }

        //判断签名
        //如何拼接这个 sign? 就按照客户端拼接的方式来进行。
        //TODO: secretKey 可以后期通过查询数据库获取 - 后期可以使用 OpenFeign 调用后台模拟接口平台完成校验
        if (body == null || !sign.equals(SignUtil.getSign(body, SignUtil.TEST_SECRET_KEY))) {
            ServerHttpResponseUtils.noAuthAns(response);
            return response.setComplete();
        }

        //5 - 请求的模拟接口是否存在
        //TODO: 数据库中查询模拟接口是否存在，已经各种请求参数校验  - 后期可以使用 OpenFeign等技术 调用后台模拟接口平台完成校验

        //6 - 如果鉴权并且参数校验通过，请求转发，调用模拟接口
        try {
            Mono<Void> result = chain.filter(exchange);
        } catch (Exception e) {
            ServerHttpResponseUtils.internelServerError(response);
            return response.setComplete();
        }

        return handleResponse(exchange, chain);
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = (HttpStatus) originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {

                                        /*
                                         调用完成之后的后续操作:
                                         8 - 调用成功，接口调用次数 + 1
                                         TODO - 使用 AOP 机制完成接口调用次数更新！
                                         相关方法已经在后台 UserInterfaceService 的 invokeInterfaceCount 实现

                                         9 - TODO 调用失败，返回一个规范的错误码
                                         if (!response.getStatusCode().equals(HttpStatus.OK)) {
                                            ServerHttpResponseUtils.internelServerError(response);
                                            return response.setComplete();
                                         }
                                         */
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);

                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        //rspArgs.add(requestUrl);
                                        String data = new String(content, StandardCharsets.UTF_8);//data
                                        sb2.append(data);

                                        //7. 打印响应日志
                                        log.info("响应的结果为: {}", data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关响应异常!" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

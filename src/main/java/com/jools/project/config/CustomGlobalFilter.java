package com.jools.project.config;

import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.buffer.Unpooled;
import com.jools.joolscommon.model.entity.InterfacesInfo;
import com.jools.joolscommon.model.entity.User;
import com.jools.joolscommon.service.InnerInterfacesInfoService;
import com.jools.joolscommon.service.InnerUserInterfaceInfoService;
import com.jools.joolscommon.service.InnerUserService;
import com.jools.project.utils.ServerHttpResponseUtils;
import com.jools.project.utils.ValidatorUtils;
import com.jools.joolsclientsdk.uitls.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
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

    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    @DubboReference
    private InnerInterfacesInfoService innerInterfacesInfoService;

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

        //基于请求头获取参数
        HttpHeaders headers = request.getHeaders();

        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");

        //4 - 用户鉴权 (判断 ak,sk 是否合法)
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            throw new RuntimeException("无效AccessKey:" + accessKey);
        }
        if (null == invokeUser) {
            ServerHttpResponseUtils.noAuthAns(response);
            return response.setComplete();
        }

        //校验权限，这里简化，直接判断与测试 accessKey 是否一致
        if (accessKey == null || !accessKey.equals(invokeUser.getAccessKey())) { //如果未通过，返回鉴权失败异常
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
        if (body == null || !sign.equals(SignUtil.getSign(body, SignUtil.TEST_SECRET_KEY))) {
            ServerHttpResponseUtils.noAuthAns(response);
            return response.setComplete();
        }

        //5 - 请求的模拟接口是否存在
        InterfacesInfo interfaceInfo = null;
        String hostAddr = InterfacesHostAddr.VIRTUAL_INTERFACES_PLATFORM.getAddr();
        try {
            interfaceInfo = innerInterfacesInfoService.getInterfaceInfo(hostAddr + pathValue, methodName);
        } catch (Exception e) {
            throw new RuntimeException("InterfaceInfo 不存在, url:" + pathValue + " method:" + methodName);
        }
        if (interfaceInfo == null) {
            ServerHttpResponseUtils.badResponseAns(response);
            return response.setComplete();
        }

        //获取接口唯一标识符
        Long interfaceInfoId = interfaceInfo.getId();
        Long userId = invokeUser.getId();

        //扩充: 校验接口剩余调用次数是否大于 0
        boolean canInvoke = false;
        try {
            canInvoke = innerUserInterfaceInfoService.canInvoke(interfaceInfoId, userId);
        } catch (Exception e) {
            ServerHttpResponseUtils.internelServerError(response);
            return response.setComplete();
        }
        if (!canInvoke) {   //如果剩余调用次数小于 0 拒绝
            ServerHttpResponseUtils.noAuthAns(response);
            return response.setComplete();
        }

        //6 - 如果鉴权并且参数校验通过，请求转发，调用模拟接口
        try {
            Mono<Void> result = chain.filter(exchange);
        } catch (Exception e) {
            ServerHttpResponseUtils.internelServerError(response);
            return response.setComplete();
        }

        //传入 接口 Id 和用户 Id
        return handleResponse(exchange, chain, interfaceInfoId, userId);
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = (HttpStatus) originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    private boolean isProcessed = false; // 标志位，防止重复处理

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));

                        if (isProcessed) {
                            log.warn("该响应已经被处理过，跳过重复处理");
                            return super.writeWith(body); // 如果已经处理过，直接调用父类方法
                        }

                        // 标记为已处理
                        isProcessed = true;


                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body).cache();  // 确保只订阅一次;
                            //
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        /*
                                            调用完成之后的后续操作:
                                            8 - 调用成功，接口调用次数 + 1
                                         */
                                        boolean invoked = false;
                                        try {
                                            invoked = innerUserInterfaceInfoService.invokeInterfaceCount(interfaceId, userId);
                                        } catch (Exception e) {
                                            throw new RuntimeException("invokeCount error", e);
                                        }
                                        if (!invoked)
                                            throw new RuntimeException("接口:" + interfaceId + "调用错误, userId:" + userId);
                                        /*
                                         9 - TODO 调用失败，返回一个规范的错误码
                                         if (!response.getStatusCode().equals(HttpStatus.OK)) {
                                            ServerHttpResponseUtils.internelServerError(response);
                                            return response.setComplete();
                                         }
                                         */
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer); // 释放内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);

                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        //rspArgs.add(requestUrl);
                                        String data = new String(content, StandardCharsets.UTF_8);//data
                                        sb2.append(data);

                                        //7. 打印响应日志
                                        log.info("Gateway Module - CustomGlobalFilter - 响应的结果为: {}", data);
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

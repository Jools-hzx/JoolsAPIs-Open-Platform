package com.jools.project.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/18 11:39
 * @description: 自定义网关限流处理1
 */
@Slf4j
@Configuration
public class GatewayFlowRuleConfiguration {

    @Bean
    public SentinelGatewayFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void initBlockHandlers() {
        // 使用低级别的 setBlockRequestHandler 自定义限流返回信息
        GatewayCallbackManager.setBlockHandler((exchange, t) -> {

            // 自定义限流提示信息
            String message = getBlockMessage((BlockException) t);
            ServerHttpRequest request = exchange.getRequest();
            String url = request.getURI().getPath();
            String methodName = request.getMethod().name();
            String hostStr = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
            log.error("请求 url:{} , Method-Type:{}, 来源host:{} 过于频繁，拒绝信息:{}", url, methodName, hostStr, message);

            // 返回一个 Mono<ServerResponse> 响应，包含限流信息
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(BodyInserters.fromValue(message));
        });
    }

    // 返回友好的限流提示信息
    private String getBlockMessage(BlockException t) {
        if (t instanceof ParamFlowException) {
            // 参数流控异常
            return "请求过多，触发了参数限流，请稍后再试。";
        } else {
            // 其他流控异常
            //比如普通限流 (QPS) 限流:
            //基于每个接口的整体请求数 来进行限制的。即无论请求的具体内容如何，只要请求到达某个接口，都会被统计在内。
            return "请求过多，服务器限流，请稍后再试。";
        }
    }

    //基于 /api/name/user 配置的限流规则
    @PostConstruct
    public void initGatewayRule01() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        //路由 ID 来配置 GatewayFlowRule，而不是基于 API 路径；见 application.yml 内的配置
        rules.add(new GatewayFlowRule("getName_path")
                .setCount(3) // 限制每秒QPS为1
                .setIntervalSec(1)); // 每秒
        GatewayRuleManager.loadRules(rules);
    }
}

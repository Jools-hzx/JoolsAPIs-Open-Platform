package com.jools.project.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/18 11:39
 * @description: 自定义网关限流处理1
 */
@Slf4j
@Configuration
public class GatewayRuleConfiguration {

    @Bean
    public SentinelGatewayFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    //    //DONE: 封装进 CustomBlockRequestHandler 内
//    @PostConstruct
//    public void initBlockHandlers() {
//        // 使用低级别的 setBlockRequestHandler 自定义限流返回信息
//        GatewayCallbackManager.setBlockHandler(new CustomBlockRequestHandler());
//    }

//    // 返回友好的限流提示信息
//    private String getBlockMessage(Exception t) {
//        if (t instanceof ParamFlowException) {
//            // 参数流控异常
//            return "请求过多，触发了参数限流，请稍后再试。";
//        } else if (t instanceof DegradeException) {
//            // 服务降级异常
//            return "服务暂不可用";
//        } else {
//            //其他
//            return "请求异常";
//        }
//    }

    //基于 /api/name/user 配置的限流规则
    @PostConstruct
    public void initGatewayRule01() {
        Set<GatewayFlowRule> rules = new HashSet<>();
//
//        //路由 ID 来配置 GatewayFlowRule，而不是基于 API 路径；见 application.yml 内的配置
//        rules.add(new GatewayFlowRule("getName_path")
//                .setCount(3) // 限制每秒QPS为1
//                .setIntervalSec(1)); // 每秒
//        GatewayRuleManager.loadRules(rules);
    }
}

package com.jools.joolsclientsdk;

import com.jools.joolsclientsdk.client.JoolsHttpClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/23 22:10
 * @description: TODO
 */


// 配置能够读取到 application.yml 的配置，读取到配置之后，
// 注解用于自动扫描组件，使得 Spring 能够自动注册相应的 Bean
@ComponentScan
// Lombok 注解，自动生成了类的 getter、setter 方法
@Data
// 能够读取 application.yml 的配置，读取到配置之后，把这个读到的配置设置到我们这里的属性中
// 配置配置前缀为 "jools.client"
@ConfigurationProperties("jools.client")
// 通过 @Configuration注解，将该类标记为一个配置类
@Configuration
public class JoolsClientConfig {

    private String accessKey;

    private String secretKey;


    @Bean
    public JoolsHttpClient joolsHttpClient() {
        //使用 ak 和 sk 创建一个 JoolsHttpClient 实例
        return new JoolsHttpClient(accessKey, secretKey);
    }
}

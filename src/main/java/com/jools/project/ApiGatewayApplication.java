package com.jools.project;

import com.jools.project.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDubbo    //用于开启 Dubbo 的注解驱动功能，使 Dubbo 的配置生效。
@Service
public class ApiGatewayApplication {

    @DubboReference     //用于注入 Dubbo 服务的客户端代理，以便调用远程服务。
    private DemoService demoService;

    public static void main(String[] args) {
        /*
        1. ConfigurableApplicationContext：用于获取 Spring 应用上下文，从而获取 Bean 实例。
        2. doSayHello 和 doSayHi 方法：调用 DemoService 的方法，验证 Dubbo 服务调用是否正常工作。
         */
        ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayApplication.class, args);
        ApiGatewayApplication application = context.getBean(ApiGatewayApplication.class);
        String result = application.doSayHello("world");
        String result2 = application.doSayHi("world");
        System.out.println("result: " + result);
        System.out.println("result: " + result2);
    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }

    public String doSayHi(String name) {
        return demoService.sayHi(name);
    }
}

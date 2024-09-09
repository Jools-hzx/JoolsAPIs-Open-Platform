# 自定义 SDK 基于 SpringBoot Starter

开发者引入之后，可以直接在 application.yml 中写配置，自动创建客户端。
调用客户端接口方法可以远程调用模拟接口平台


## 导入依赖 (install 到了自己本地 Maven 仓库，未发布到远端Maven仓库)
```xml
<dependency>
    <groupId>com.jools</groupId>
    <artifactId>jools-client-sdk</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 配置 SDK
application.yml 内
```yml
jools:
  client:
    access-key: xxxx
    secret-key: xxxx
```


## 自带的 Client
```java
@ComponentScan
@Data   // Lombok 注解，自动生成了类的 getter、setter 方法
@ConfigurationProperties("jools.client") // 能够读取 application.yml 的配置，读取到配置之后，把这个读到的配置设置到我们这里的属性中，配置前缀为 "jools.client"
@Configuration    // 通过 @Configuration注解，将该类标记为一个配置类
public class JoolsClientConfig {

    private String accessKey;

    private String secretKey;


    @Bean
    public JoolsHttpClient joolsHttpClient() {
        //使用 ak 和 sk 创建一个 JoolsHttpClient 实例
        return new JoolsHttpClient(accessKey, secretKey);
    }
}
```
resources/META-INF/spring.factories
```properties
# spring boot starter
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.jools.joolsclientsdk.JoolsClientConfig
```


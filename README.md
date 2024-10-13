# JoolsAPIs-Open-Platform [JoolsAPIs开放调用平台] 
A simple API open platform that allows developers to easily query and call various interfaces. It also makes it convenient for administrators to manage the interfaces.

![Static Badge](https://img.shields.io/badge/Jools_OpenAPIs_Platform-version_1.0-blue)
![Static Badge](https://img.shields.io/badge/Spring_framework-SpringBoot_2.7.x-green?logo=spring)
![Static Badge](https://img.shields.io/badge/Dubbo_3.0.9-8A2BE2?logo=appveyor)
![Static Badge](https://img.shields.io/badge/Nacos_2.4-grey?logo=appveyor)
![Static Badge](https://img.shields.io/badge/Sentinel-1.8.0-green?logo=spring)
![Static Badge](https://img.shields.io/badge/Seata-1.8.0-darkblue?logo=spring)
![Static Badge](https://img.shields.io/badge/MySQL_8.0.34-grey?logo=appveyor)
![Static Badge](https://img.shields.io/badge/JDK_version-Java_1.8.x-blue?logo=java)
![Static Badge](https://img.shields.io/badge/Node-v_16.13-blue?logo=javascript)
![Static Badge](https://img.shields.io/badge/npm-v8.1-red?logo=javascript)



## 各个分支项目内容
```txt
1. main 分支: admin APIs 管理后台

2. api-gateway-main 分支: 网关子项目，统一处理调用接口请求的相关操作(统计调用次数、权限校验、跨域、计费、路由、接口保护、日志、缓存、流量染色等)

3. hzx-interfaces-main 分支: 模拟存储各个接口的平台

4. api-client-sdk-main 分支: 自定义 SpringBoot starter 客户端 SDK，工具类，让其他项目和用户更加方便地调用接口的源码包

5. open-api-fontend-main 分支:  前端项目，基于 Ant Design Pro 项目模板二次开发(用户权限校验，管理员控制面板功能，远程调用接口平台功能)

6. api-jools-commons-main 分支: 公共模块，共同使用的 model、interface 服务
```



## 项目介绍
后端基于Spring Boot + Dubbo + Gateway，前端基于 Ant Design Pro 的 API 接口开放调用平台。

### 管理员权限:
1. 接入并发布接口
2. 可视化各接口调用情况

### 用户权限:
1. 开通接口调用权限
2. 浏览接口及在线调试
3. 通过客户端 SDK 轻松调用接口

管理员可以接入并发布接口，可视化各接口调用情况:用户可以开通接口调用权限、浏览接口及在线调试，并通过客户端
SDK 轻松调用接口。

### 完成工作:
1. 将项目后端划分成为多模块使用 Maven 进行多模块依赖管理和打包。包括: 后台系统、模拟接口、公共模块、客户端 SDK、API 网关。借助MyBatis X 插件快速生成基础代码，减少重复工作
2. 提升接口调用的安全性和可溯源性，设计 API 签名认证算法，为用户分配唯一 ak/sk 鉴权。
3. 开发基于 Spring Boot Starter 的客户端 SDK，实现一行代码调用接口，简化开发者操作。介绍开发者调用成本过高的问题
4. 使用 Spring Cloud Gateway 作为 API 网关，集中处理路由转发、签名校验、访问控制等，提高安全性与维护效率。
5. 抽象模型层与业务层为公共模块，使用 Dubbo RPC框架实现子系统间高性能调用，选用Nacos作为服务注册中心，减少代码重复。
6. 基于 Seata 框架实现分布式事务管理，结合 Nacos 服务注册与配置集中管理，确保各微服务间的事务一致性及自动化回滚，提升数据可靠性和一致性。
7. 前端基于 Ant Design Pro 脚手架和自带的 umi-request 请求库提高开发效率。使用 EChart 可视化库实现了接口调用的分析图表。


---

## main 分支 - 管理员接口后台管理平台
### 基于 SpringBoot 项目快速开发初始模板二次开发

模板来源
> 作者：[程序员鱼皮](https://github.com/liyupi)
> 仅分享于 [编程导航知识星球](https://yupi.icu)

## 理员接口后台管理平台 - 模板特点
### 主流框架 & 特性

- Spring Boot 2.7.x（贼新）
- Spring MVC
- MyBatis + MyBatis Plus 数据访问（开启分页）
- Spring Boot 调试工具和项目处理器
- Spring AOP 切面编程
- Spring Scheduler 定时任务
- Spring 事务注解

### 数据存储
支持:
- MySQL 数据库
- Redis 内存数据库
- Elasticsearch 搜索引擎
- 腾讯云 COS 对象存储

### 工具类

- Easy Excel 表格处理
- Hutool 工具库
- Apache Commons Lang3 工具类
- Lombok 注解

### 业务特性

- 业务代码生成器（支持自动生成 Service、Controller、数据模型代码）
- Spring Session Redis 分布式登录
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 长整数丢失精度解决
- 多环境配置


## 业务功能

- 提供示例 SQL（用户、帖子、帖子点赞、帖子收藏表）
- 用户登录、注册、注销、更新、检索、权限管理
- 帖子创建、删除、编辑、更新、数据库检索、ES 灵活检索
- 帖子点赞、取消点赞
- 帖子收藏、取消收藏、检索已收藏帖子
- 帖子全量同步 ES、增量同步 ES 定时任务
- 支持微信开放平台登录
- 支持微信公众号订阅、收发消息、设置菜单
- 支持分业务的文件上传

### 单元测试

- JUnit5 单元测试
- 示例单元测试类

### 架构设计

- 合理分层


## 快速上手

> 所有需要修改的地方都标记了 `todo`，便于大家找到修改的位置~

### MySQL 数据库

1）修改 `application.yml` 的数据库配置为你自己的：

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/my_db
    username: root
    password: 123456
```

2）执行 `sql/create_table.sql` 中的数据库语句，自动创建库表

3）启动项目，访问 `http://localhost:8101/api/doc.html` 即可打开接口文档，不需要写前端就能在线调试接口了~

![](doc/swagger.png)

### Redis 分布式登录

1）修改 `application.yml` 的 Redis 配置为你自己的：

```yml
spring:
  redis:
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
    password: 123456
```

2）修改 `application.yml` 中的 session 存储方式：

```yml
spring:
  session:
    store-type: redis
```

3）移除 `MainApplication` 类开头 `@SpringBootApplication` 注解内的 exclude 参数：

修改前：

```java
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
```

修改后：


```java
@SpringBootApplication
```

### Elasticsearch 搜索引擎

1）修改 `application.yml` 的 Elasticsearch 配置为你自己的：

```yml
spring:
  elasticsearch:
    uris: http://localhost:9200
    username: root
    password: 123456
```

2）复制 `sql/post_es_mapping.json` 文件中的内容，通过调用 Elasticsearch 的接口或者 Kibana Dev Tools 来创建索引（相当于数据库建表）

```
PUT post_v1
{
 参数见 sql/post_es_mapping.json 文件
}
```

这步不会操作的话需要补充下 Elasticsearch 的知识，或者自行百度一下~

3）开启同步任务，将数据库的帖子同步到 Elasticsearch

找到 job 目录下的 `FullSyncPostToEs` 和 `IncSyncPostToEs` 文件，取消掉 `@Component` 注解的注释，再次执行程序即可触发同步：

```java
// todo 取消注释开启任务
//@Component
```

### 业务代码生成器

支持自动生成 Service、Controller、数据模型代码，配合 MyBatisX 插件，可以快速开发增删改查等实用基础功能。

找到 `generate.CodeGenerator` 类，修改生成参数和生成路径，并且支持注释掉不需要的生成逻辑，然后运行即可。

```
// 指定生成参数
String packageName = "com.jools.project";
String dataName = "用户评论";
String dataKey = "userComment";
String upperDataKey = "UserComment";
```

生成代码后，可以移动到实际项目中，并且按照 `// todo` 注释的提示来针对自己的业务需求进行修改。
>>>>>>> 30edafc (Build the basic backend: Implement InterfacesInfo Model Basic CRUD; Allow user & post CRUD and etc)

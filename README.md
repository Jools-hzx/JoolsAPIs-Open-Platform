# 抽象公共模块项目

## 服务抽取
1. 数据库中查是否已分配给用户秘钥（根据 accessKey 拿到用户信息，返回用户信息，为空表示不存在）
2. 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数，返回接口信息，为空表示不存在）
3. 接口调用次数 + 1 invokeCount（accessKey、secretKey（标识用户），请求接口路径）

包含各个项目之间公用的重复类和服务接口
![image](https://github.com/user-attachments/assets/9c6b74ec-359e-402d-94b7-ca0a1f1c9f16)

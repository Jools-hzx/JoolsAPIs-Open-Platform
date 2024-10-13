CREATE DATABASE nacos_config CHARACTER SET utf8 COLLATE utf8_general_ci;


-- 支持分布式事务
INSERT INTO `distributed_lock`(lock_key, lock_value, expire)
VALUES ('AsyncCommitting', ' ', 0);
INSERT INTO `distributed_lock`(lock_key, lock_value, expire)
VALUES ('RetryCommitting', ' ', 0);
INSERT INTO `distributed_lock`(lock_key, lock_value, expire)
VALUES ('RetryRollbacking', ' ', 0);
INSERT INTO `distributed_lock`(lock_key, lock_value, expire)
VALUES ('TxTimeoutCheck', ' ', 0);


-- 创建表
create table branch_table
(
    branch_id         bigint        not null primary key,
    xid               varchar(128)  not null,
    transaction_id    bigint        null,
    resource_group_id varchar(32)   null,
    resource_id       varchar(256)  null,
    branch_type       varchar(8)    null,
    status            tinyint       null,
    client_id         varchar(64)   null,
    application_data  varchar(2000) null,
    gmt_create        datetime(6)   null,
    gmt_modified      datetime(6)   null
);
create index idx_xid on branch_table (xid);
create table distributed_lock
(
    lock_key   char(20)    not null primary key,
    lock_value varchar(20) not null,
    expire     bigint      null
);
create table global_table
(
    xid                       varchar(128)  not null primary key,
    transaction_id            bigint        null,
    status                    tinyint       not null,
    application_id            varchar(32)   null,
    transaction_service_group varchar(32)   null,
    transaction_name          varchar(128)  null,
    timeout                   int           null,
    begin_time                bigint        null,
    application_data          varchar(2000) null,
    gmt_create                datetime      null,
    gmt_modified              datetime      null
);
create index idx_status_gmt_modified on global_table (status, gmt_modified);
create index idx_transaction_id on global_table (transaction_id);
create table interfaces_info
(
    id             bigint auto_increment comment '用户Id(主键)' primary key,
    name           varchar(256) default '""'              not null comment '接口名称',
    description    varchar(256) default '""'              not null comment '接口描述',
    url            varchar(512) default '/test/api'       not null comment '接口url',
    requestHeader  text                                   null comment '请求头',
    responseHeader text                                   null comment '响应头',
    status         tinyint      default 1                 not null comment '接口状态( 0 - 关闭，1 - 开启)',
    method         varchar(256) default 'default_method'  not null comment '请求类型（GET/POST）',
    userId         bigint       default 0                 not null comment '创建人 Id',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint      default 0                 not null comment '是否删除 (0 - 未被删除，1 - 已经删除)',
    requestParams  varchar(512) default ''                not null comment '请求参数'
) comment '接口信息表';
create table lock_table
(
    row_key        varchar(128)      not null primary key,
    xid            varchar(128)      null,
    transaction_id bigint            null,
    branch_id      bigint            not null,
    resource_id    varchar(256)      null,
    table_name     varchar(32)       null,
    pk             varchar(36)       null,
    status         tinyint default 0 not null comment '0:locked ,1:rollbacking',
    gmt_create     datetime          null,
    gmt_modified   datetime          null
);
create index idx_branch_id on lock_table (branch_id);
create index idx_status on lock_table (status);
create index idx_xid on lock_table (xid);
create table post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '帖子' collate = utf8mb4_unicode_ci;
create index idx_userId on post (userId);
create table post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '帖子收藏';
create index idx_postId on post_favour (postId);
create index idx_userId on post_favour (userId);
create table post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '帖子点赞';
create index idx_postId on post_thumb (postId);
create index idx_userId on post_thumb (userId);
create table undo_log
(
    id            bigint auto_increment comment '主键ID' primary key,
    branch_id     bigint                              not null comment '分支事务ID',
    xid           varchar(100)                        not null comment '全局事务ID',
    context       varchar(128)                        not null comment '上下文信息，包含序列化方式',
    rollback_info longblob                            not null comment '回滚日志',
    log_status    int                                 not null comment '日志状态，0: 正常状态, 1: 已全局回滚完成',
    log_created   timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    log_modified  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    constraint ux_undo_log unique (xid, branch_id)
) comment 'AT 事务模式下的回滚日志' charset = utf8mb3;
create table user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    accessKey    varchar(255)                           null,
    secretKey    varchar(255)                           null
) comment '用户' collate = utf8mb4_unicode_ci;
create index idx_unionId on user (unionId);
create table user_interface_info
(
    id              bigint auto_increment comment '主键' primary key,
    userId          bigint                             not null comment '调用用户 id',
    interfaceInfoId bigint                             not null comment '接口 id',
    totalNum        int      default 0                 not null comment '总调用次数',
    leftNum         int      default 0                 not null comment '剩余调用次数',
    status          int      default 0                 not null comment '0-正常，1-禁用',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';



-- 插入测试数据
-- 修改 `user 表`
ALTER TABLE `user`
    ADD COLUMN `accessKey` VARCHAR(255),
    ADD COLUMN `secretKey` VARCHAR(255);

-- 插入测试数据 user 表
INSERT INTO `user` (`id`, `userAccount`, `userPassword`, `unionId`, `mpOpenId`, `userName`, `userAvatar`, `userProfile`,
                    `userRole`, `createTime`, `updateTime`, `isDelete`, `accessKey`, `secretKey`)
VALUES (1821023112521080833, 'hzx2001', '865f1103ddfe2b50759b83a34110310a', NULL, NULL, 'Jools He',
        'https://yupi.icu/logo.png\r\n', NULL, 'admin', '2024-08-07 11:18:37', '2024-08-20 21:45:14', 0, 'jools',
        '123456');
INSERT INTO `user` (`id`, `userAccount`, `userPassword`, `unionId`, `mpOpenId`, `userName`, `userAvatar`, `userProfile`,
                    `userRole`, `createTime`, `updateTime`, `isDelete`, `accessKey`, `secretKey`)
VALUES (1828036828521308161, 'Jools He', '2ada93bd9243c305356f35d578f1ad82', NULL, NULL, NULL, NULL, NULL, 'admin',
        '2024-08-26 19:48:37', '2024-09-16 00:53:39', 0, 'jools666', '123456');

-- 插入数据 user_interface_info
INSERT INTO `user_interface_info` (`id`, `userId`, `interfaceInfoId`, `totalNum`, `leftNum`, `status`, `createTime`,
                                   `updateTime`, `isDelete`)
VALUES (1, 1, 1, 103, 7, 0, '2022-10-03 19:19:59', '2024-09-09 19:15:48', 0);
INSERT INTO `user_interface_info` (`id`, `userId`, `interfaceInfoId`, `totalNum`, `leftNum`, `status`, `createTime`,
                                   `updateTime`, `isDelete`)
VALUES (2, 2, 1, 103, 7, 0, '2024-09-02 12:00:55', '2024-09-02 14:18:06', 0);
INSERT INTO `user_interface_info` (`id`, `userId`, `interfaceInfoId`, `totalNum`, `leftNum`, `status`, `createTime`,
                                   `updateTime`, `isDelete`)
VALUES (3, 3, 1, 103, 7, 0, '2024-09-02 12:00:58', '2024-09-02 14:18:06', 0);
INSERT INTO `user_interface_info` (`id`, `userId`, `interfaceInfoId`, `totalNum`, `leftNum`, `status`, `createTime`,
                                   `updateTime`, `isDelete`)
VALUES (4, 1821023112521080833, 1, 6, 100, 0, '2024-09-16 11:26:07', '2024-09-17 18:40:20', 0),
       (5, 1828036828521308161, 1, 374, 91, 0, '2024-09-16 12:09:01', '2024-10-04 19:27:04', 0),
       (6, 1828036828521308161, 2, 22, 10, 0, '2024-09-16 15:31:59', '2024-09-16 15:31:59', 0),
       (7, 1828036828521308161, 3, 333, 10, 0, '2024-09-16 15:31:59', '2024-09-18 11:56:41', 0),
       (8, 1828036828521308161, 4, 144, 10, 0, '2024-09-16 15:31:59', '2024-09-18 11:56:41', 0),
       (9, 1821023112521080833, 5, 55, 10, 0, '2024-09-16 15:31:59', '2024-09-16 15:31:59', 0),
       (10, 1821023112521080833, 5, 99, 10, 0, '2024-09-16 15:31:59', '2024-09-18 14:56:14', 0);


-- 插入数据 interfaces_info
INSERT INTO `interfaces_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                               `method`, `userId`, `createTime`, `updateTime`, `isDelete`, `requestParams`)
VALUES (1, '请求用户名称', '基于 POST 获取用户请求', 'http://localhost:10000/api/name/user',
        '{\n  \"Content-Type\": \"application/json\"\n}', '{\n  \"Content-Type\": \"application/json\"\n}', 1, 'POST',
        1821023112521080833, '2024-08-26 20:37:31', '2024-08-26 21:37:41', 0,
        '[\n	{\"name\": \"username\", \"type\": \"string\"}\n]\n');
INSERT INTO `interfaces_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                               `method`, `userId`, `createTime`, `updateTime`, `isDelete`, `requestParams`)
VALUES (2, '潘展鹏', '叶晓啸', 'www.courtney-kassulke.net', '戴鸿煊', '袁荣轩', 0, '谢立诚', 434, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, '');
INSERT INTO `interfaces_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                               `method`, `userId`, `createTime`, `updateTime`, `isDelete`, `requestParams`)
VALUES (3, '覃天宇', '冯昊强', 'www.johnie-harris.name', '武博文', '戴思聪', 0, '毛昊焱', 2334, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, '');
INSERT INTO `interfaces_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                               `method`, `userId`, `createTime`, `updateTime`, `isDelete`, `requestParams`)
VALUES (4, '谭黎昕', '傅哲瀚', 'www.josette-adams.org', '覃振家', '吕风华', 0, '孔鹭洋', 57553, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (5, '魏嘉熙', '沈思聪', 'www.wyatt-nader.org', '韩嘉懿', '熊思源', 0, '姚立轩', 0, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (6, '龚修洁', '宋锦程', 'www.jerlene-grimes.io', '廖哲瀚', '张建辉', 1, '林天宇', 34618, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (7, '蒋鑫磊', '谭明辉', 'www.micki-dicki.name', '唐雪松', '沈鹏飞', 0, '罗烨华', 27, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (8, '钱雪松', '吕鹏', 'www.andy-russel.org', '范烨伟', '邵黎昕', 0, '苏笑愚', 5460331959, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (9, '严晟睿', '唐晟睿', 'www.nadine-bradtke.name', '郑峻熙', '冯琪', 0, '秦烨华', 90477376,
        '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
       (10, '段浩轩', '潘文博', 'www.terrence-konopelski.co', '韩雨泽', '袁志强', 0, '陈博文', 9453614492,
        '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
       (11, '黎健雄', '龙泽洋', 'www.rodney-douglas.io', '冯晟睿', '韩明哲', 0, '蒋弘文', 70703, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (12, '邵绍齐', '范振家', 'www.wilbur-reinger.name', '邹绍辉', '叶哲瀚', 0, '姚子轩', 24180,
        '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
       (13, '顾博文', '张瑾瑜', 'www.kittie-kautzer.name', '龙修杰', '万弘文', 0, '潘弘文', 334268466,
        '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
       (14, '唐明辉', '郝耀杰', 'www.ed-barton.org', '蒋晓啸', '段钰轩', 0, '程明', 6, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (15, '陶泽洋', '龙语堂', 'www.shane-braun.io', '杜驰', '徐笑愚', 0, '熊展鹏', 8, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (16, '孔炎彬', '龚昊天', 'www.rolf-wiegand.net', '赵明辉', '覃昊天', 0, '白天宇', 7453201, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (17, '陈梓晨', '高志泽', 'www.norah-goldner.org', '何鹤轩', '郝鹏', 0, '李煜城', 791, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (18, '白思', '汪懿轩', 'www.hugo-bradtke.co', '于立轩', '毛楷瑞', 0, '罗俊驰', 3219, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (19, '李琪', '谭健雄', 'www.gerry-dicki.biz', '龙果', '吴晟睿', 0, '马昊焱', 61151986, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (20, '吴思', '吴志泽', 'www.kareem-feest.io', '汪黎昕', '赵瑾瑜', 0, '邱致远', 6, '2024-08-07 20:01:40',
        '2024-08-07 20:01:40', 0, ''),
       (21, '阿里巴巴', 'safafd', 'localhost:8080/sdfsfd', 'adfs', 'sadfa', 1, 'POST', 1821023112521080833,
        '2024-08-08 21:06:44', '2024-08-08 21:44:47', 1, ''),
       (22, 'HAHAHA - Jools He', 'AHHAHA', 'localhost:8080/haahah', 'hahaha', 'hahah', 1, 'POST', 1821023112521080833,
        '2024-08-20 16:55:21', '2024-08-20 16:55:21', 0, ''),
       (24, '刘笑愚', '吴琪', 'www.wesley-trantow.com', '万鸿煊', '王越彬', 1, '黄雪松', 26826, '2024-08-07 20:01:40',
        '2024-08-26 20:38:49', 0, '');


















/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE `interfaces_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户Id(主键)',
  `name` varchar(256) NOT NULL DEFAULT '""' COMMENT '接口名称',
  `description` varchar(256) NOT NULL DEFAULT '""' COMMENT '接口描述',
  `url` varchar(512) NOT NULL DEFAULT '/test/api' COMMENT '接口url',
  `requestHeader` text COMMENT '请求头',
  `responseHeader` text COMMENT '响应头',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '接口状态( 0 - 关闭，1 - 开启)',
  `method` varchar(256) NOT NULL DEFAULT 'default_method' COMMENT '请求类型（GET/POST）',
  `userId` bigint NOT NULL DEFAULT '0' COMMENT '创建人 Id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 (0 - 未被删除，1 - 已经删除)',
  `requestParams` varchar(512) NOT NULL DEFAULT '' COMMENT '请求参数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口信息表';

INSERT INTO `interfaces_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `createTime`, `updateTime`, `isDelete`, `requestParams`) VALUES
(1, '请求用户名称', '基于 POST 获取用户请求', 'http://localhost:10000/api/name/user', '{\n  \"Content-Type\": \"application/json\"\n}', '{\n  \"Content-Type\": \"application/json\"\n}', 1, 'POST', 1821023112521080833, '2024-08-26 20:37:31', '2024-08-26 21:37:41', 0, '[\n	{\"name\": \"username\", \"type\": \"string\"}\n]\n');
INSERT INTO `interfaces_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `createTime`, `updateTime`, `isDelete`, `requestParams`) VALUES
(2, '潘展鹏', '叶晓啸', 'www.courtney-kassulke.net', '戴鸿煊', '袁荣轩', 0, '谢立诚', 434, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, '');
INSERT INTO `interfaces_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `createTime`, `updateTime`, `isDelete`, `requestParams`) VALUES
(3, '覃天宇', '冯昊强', 'www.johnie-harris.name', '武博文', '戴思聪', 0, '毛昊焱', 2334, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, '');
INSERT INTO `interfaces_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `createTime`, `updateTime`, `isDelete`, `requestParams`) VALUES
(4, '谭黎昕', '傅哲瀚', 'www.josette-adams.org', '覃振家', '吕风华', 0, '孔鹭洋', 57553, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(5, '魏嘉熙', '沈思聪', 'www.wyatt-nader.org', '韩嘉懿', '熊思源', 0, '姚立轩', 0, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(6, '龚修洁', '宋锦程', 'www.jerlene-grimes.io', '廖哲瀚', '张建辉', 1, '林天宇', 34618, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(7, '蒋鑫磊', '谭明辉', 'www.micki-dicki.name', '唐雪松', '沈鹏飞', 0, '罗烨华', 27, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(8, '钱雪松', '吕鹏', 'www.andy-russel.org', '范烨伟', '邵黎昕', 0, '苏笑愚', 5460331959, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(9, '严晟睿', '唐晟睿', 'www.nadine-bradtke.name', '郑峻熙', '冯琪', 0, '秦烨华', 90477376, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(10, '段浩轩', '潘文博', 'www.terrence-konopelski.co', '韩雨泽', '袁志强', 0, '陈博文', 9453614492, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(11, '黎健雄', '龙泽洋', 'www.rodney-douglas.io', '冯晟睿', '韩明哲', 0, '蒋弘文', 70703, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(12, '邵绍齐', '范振家', 'www.wilbur-reinger.name', '邹绍辉', '叶哲瀚', 0, '姚子轩', 24180, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(13, '顾博文', '张瑾瑜', 'www.kittie-kautzer.name', '龙修杰', '万弘文', 0, '潘弘文', 334268466, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(14, '唐明辉', '郝耀杰', 'www.ed-barton.org', '蒋晓啸', '段钰轩', 0, '程明', 6, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(15, '陶泽洋', '龙语堂', 'www.shane-braun.io', '杜驰', '徐笑愚', 0, '熊展鹏', 8, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(16, '孔炎彬', '龚昊天', 'www.rolf-wiegand.net', '赵明辉', '覃昊天', 0, '白天宇', 7453201, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(17, '陈梓晨', '高志泽', 'www.norah-goldner.org', '何鹤轩', '郝鹏', 0, '李煜城', 791, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(18, '白思', '汪懿轩', 'www.hugo-bradtke.co', '于立轩', '毛楷瑞', 0, '罗俊驰', 3219, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(19, '李琪', '谭健雄', 'www.gerry-dicki.biz', '龙果', '吴晟睿', 0, '马昊焱', 61151986, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(20, '吴思', '吴志泽', 'www.kareem-feest.io', '汪黎昕', '赵瑾瑜', 0, '邱致远', 6, '2024-08-07 20:01:40', '2024-08-07 20:01:40', 0, ''),
(21, '阿里巴巴', 'safafd', 'localhost:8080/sdfsfd', 'adfs', 'sadfa', 1, 'POST', 1821023112521080833, '2024-08-08 21:06:44', '2024-08-08 21:44:47', 1, ''),
(22, 'HAHAHA - Jools He', 'AHHAHA', 'localhost:8080/haahah', 'hahaha', 'hahah', 1, 'POST', 1821023112521080833, '2024-08-20 16:55:21', '2024-08-20 16:55:21', 0, ''),
(24, '刘笑愚', '吴琪', 'www.wesley-trantow.com', '万鸿煊', '王越彬', 1, '黄雪松', 26826, '2024-08-07 20:01:40', '2024-08-26 20:38:49', 0, '');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.18 : Database - sob_blog_system
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sob_blog_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `sob_blog_system`;

/*Table structure for table `tb_article` */

DROP TABLE IF EXISTS `tb_article`;

CREATE TABLE `tb_article` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `user_id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `category_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分类',
  `content` mediumtext COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章内容',
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（1表示富文本，2表示markdown）',
  `review` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核状态（1表示通过，2表示未通过，3表示被打回）',
  `cover` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '封面',
  `status` varchar(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '2' COMMENT '发布状态（1表示发布，2表示草稿，3表示置顶）',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '文章状态（0表示删除，1表示正常）',
  `position` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '2' COMMENT '1表示发布到社区，2表示发布到本地博客，',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '摘要',
  `labels` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '阅读数量',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_user_article_on_user_id` (`user_id`),
  KEY `fk_category_article_on_category_id` (`category_id`),
  CONSTRAINT `fk_user_article_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_article` */

insert  into `tb_article`(`id`,`title`,`user_id`,`category_id`,`content`,`type`,`review`,`cover`,`status`,`state`,`position`,`summary`,`labels`,`view_count`,`create_time`,`update_time`) values 
('1372837239741841408','Tomorrow介绍【必阅】','1372837239645372416','Tomorrow','# 博客介绍\n1. Tomorrow诞生于2021年3月。\n2. Tomorrow主打为个人博客，同时提供一个供各位道友交流的社区。\n# 能为您带来什么\n1. 为各位道友提供一个记录生活的平台，你可以发表你的技术论文，也可以记录您的生活趣事，或者当作笔记本使用。\n2. 提高道友的文笔水平，在长期的文章积累中，愿道友也能有《[鹧鸪天](https://baike.baidu.com/item/%E9%B9%A7%E9%B8%AA%E5%A4%A9/1340238?fr=aladdin)》“忆昔彤庭望日华，匆匆枯笔梦生花。”的妙笔。\n3. 为道友寻找到志同道合的江湖挚友。\n# 功能介绍\n### 文章管理\n- 文章发表\n- 文章的管理\n- 回收站\n### 运营管理\n- 文章分类管理\n- 轮播图管理\n- 评论管理\n- 相册管理\n### 博客设置\n- 自定义博客信息\n##### 更多功能可前往博客后台体验。\n# 注意事项\n1. 请文明使用该网站，不可对他人文章进行肆意的贬低，等不道德行为，如辱骂等！\n2. 文章的发布，不可发布带有色，赌，谣，恐，等...性质的文章。\n3. 不可恶意攻击该网站。\n4. 为了各位道友信息的安全，切勿将账号泄露给他人。\n5. 望各位道友共同努力，一起提供一个文章且充满生机的江湖。\n##### 一经发现以上行为，将禁止该账户对网站的使用。\n# 使用技巧\n- 博客风格可根据图片的风格统一，不建议使用不同风格的文章封面。建议风格统一。\n- 博客风格，小清新，科技，喜庆，卡通。等...\n- 轮播图建议使用矩形图片，为防止图片未充满屏幕，或压缩严重，导致图片美感流失。\n# 小掌柜的祝语\n- 非常感谢道友的入驻，希望道友能在这个非凡的江湖能有所收获，我们一起远航。','2','2','http://zsh-pawn.oss-cn-shenzhen.aliyuncs.com/pawn-bok/57793b0a-5c7e-42ea-aaa1-b3292eea792b.jpg','1','1','2','Tomorrow介绍！','博客-简介',2,'2021-03-19 09:07:59','2021-03-19 09:07:59');

/*Table structure for table `tb_categories` */

DROP TABLE IF EXISTS `tb_categories`;

CREATE TABLE `tb_categories` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `pinyin` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼音',
  `description` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `status` varchar(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：0表示不使用，1表示正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_categories` */

/*Table structure for table `tb_comment` */

DROP TABLE IF EXISTS `tb_comment`;

CREATE TABLE `tb_comment` (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `parent_content` text COLLATE utf8mb4_general_ci COMMENT '父内容',
  `release_id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章发布人id',
  `article_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章ID',
  `comment_type` varchar(2) COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论类型,1表示评论，2表示评论的回复',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '评论内容',
  `user_id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论用户的ID',
  `user_avatar` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论用户的头像',
  `user_name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论用户的名称',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态（0表示删除，1表示正常）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_user_comment_on_user_id` (`user_id`),
  KEY `fk_article_comment_on_article_id` (`article_id`),
  CONSTRAINT `fk_user_comment_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_comment` */

/*Table structure for table `tb_daily_view_count` */

DROP TABLE IF EXISTS `tb_daily_view_count`;

CREATE TABLE `tb_daily_view_count` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '每天浏览量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_daily_view_count` */

/*Table structure for table `tb_friends` */

DROP TABLE IF EXISTS `tb_friends`;

CREATE TABLE `tb_friends` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接名称',
  `logo` varchar(1024) COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接logo',
  `url` varchar(1024) COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `state` varchar(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接状态:0表示不可用，1表示正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_friends` */

/*Table structure for table `tb_images` */

DROP TABLE IF EXISTS `tb_images`;

CREATE TABLE `tb_images` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ID',
  `url` varchar(1024) COLLATE utf8mb4_general_ci NOT NULL COMMENT '路径',
  `original` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片类型，loop,avatar',
  `state` varchar(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示删除，1表正常）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_user_images_on_user_id` (`user_id`),
  CONSTRAINT `fk_user_images_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_images` */

/*Table structure for table `tb_labels` */

DROP TABLE IF EXISTS `tb_labels`;

CREATE TABLE `tb_labels` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `name` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `user_id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '添加标签用户的id',
  `count` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_labels` */

/*Table structure for table `tb_looper` */

DROP TABLE IF EXISTS `tb_looper`;

CREATE TABLE `tb_looper` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `title` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图标题',
  `introduction` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图简介',
  `user_id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `state` varchar(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：0表示不可用，1表示正常',
  `target_url` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目标URL',
  `image_url` varchar(2014) COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_looper` */

insert  into `tb_looper`(`id`,`title`,`introduction`,`user_id`,`order`,`state`,`target_url`,`image_url`,`create_time`,`update_time`) values 
('1372837239716675584','Tomorrow','但行好事 - 莫问前程','1372837239645372416',1,'1','https://img.tukuppt.com//ad_preview/00/11/82/5c994ffec3bbf.jpg!/fw/780','http://zsh-pawn.oss-cn-shenzhen.aliyuncs.com/pawn-bok/024b6b39-d3de-471d-84a5-b1d75ebb1b61.jpg','2021-03-19 09:07:59','2021-03-19 09:07:59');

/*Table structure for table `tb_news` */

DROP TABLE IF EXISTS `tb_news`;

CREATE TABLE `tb_news` (
  `id` varchar(20) NOT NULL COMMENT '消息id',
  `content` text COMMENT '消息内容',
  `news_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送人的昵称',
  `news_aver` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送人的头像',
  `news_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送人的id',
  `receiver_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收人的id',
  `announcement` varchar(2) NOT NULL COMMENT '消息状态（1，表示公告，2，表示消息）',
  `view` varchar(2) NOT NULL COMMENT '消息状态（1，表示未查看，2，表示已查看）',
  `state` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '消息状态（0表示删除，1表示正常）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_news` */

/*Table structure for table `tb_photo_wall` */

DROP TABLE IF EXISTS `tb_photo_wall`;

CREATE TABLE `tb_photo_wall` (
  `id` varchar(20) NOT NULL COMMENT '照片id',
  `introduction` varchar(100) NOT NULL COMMENT '照片描述',
  `user_id` varchar(20) NOT NULL COMMENT '用户id',
  `state` varchar(2) NOT NULL DEFAULT '1' COMMENT '状态：0表示不可用，1表示正常',
  `image_url` varchar(2014) NOT NULL COMMENT '照片地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_photo_wall` */

/*Table structure for table `tb_refresh_token` */

DROP TABLE IF EXISTS `tb_refresh_token`;

CREATE TABLE `tb_refresh_token` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `refresh_token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '刷新的token',
  `user_id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `token_key` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token的key',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_refresh_token` */

/*Table structure for table `tb_settings` */

DROP TABLE IF EXISTS `tb_settings`;

CREATE TABLE `tb_settings` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `key` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '键',
  `value` varchar(512) COLLATE utf8mb4_general_ci NOT NULL COMMENT '值',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_settings` */

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `roles` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像地址',
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱地址',
  `sign` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '简介',
  `occupation` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '职位',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态：0表示删除，1表示正常',
  `reg_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册ip',
  `login_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录Ip',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `tb_user` */

insert  into `tb_user`(`id`,`user_name`,`password`,`roles`,`avatar`,`email`,`sign`,`occupation`,`state`,`reg_ip`,`login_ip`,`create_time`,`update_time`) values 
('1372837239645372416','admin','$2a$10$/hK7yniSu47VD1Hce1fzreLQJDI4KUKLCmmJ0OID5e14UpOSku76W','role_admin','http://zsh-pawn.oss-cn-shenzhen.aliyuncs.com/pawn-bok/5b5e82e0-98f9-4e6d-9800-da8f29070a96.png','2978824265@qq.com','但行好事，莫问前程。','江湖游侠','1','0:0:0:0:0:0:0:1','0:0:0:0:0:0:0:1','2021-03-19 09:07:59','2021-03-19 09:07:59');

/*Table structure for table `tb_web_info` */

DROP TABLE IF EXISTS `tb_web_info`;

CREATE TABLE `tb_web_info` (
  `id` varchar(20) NOT NULL COMMENT 'id',
  `user_id` varchar(20) NOT NULL COMMENT '用户id',
  `keyword` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '网站关键字',
  `value` varchar(30) NOT NULL COMMENT '网站标题',
  `introduction` text NOT NULL COMMENT '网站介绍',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_web_info` */

insert  into `tb_web_info`(`id`,`user_id`,`keyword`,`value`,`introduction`,`create_time`,`update_time`) values 
('1372837239691509760','1372837239645372416','key','admin的店铺','admin的网站','2021-03-19 09:07:59','2021-03-19 09:07:59');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

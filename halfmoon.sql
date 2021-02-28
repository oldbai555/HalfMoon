/*
Navicat MySQL Data Transfer

Source Server         : docker_mysql
Source Server Version : 50732
Source Host           : 192.168.30.133:3306
Source Database       : halfmoon

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-02-28 16:42:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `title` varchar(256) NOT NULL COMMENT '标题',
  `user_id` varchar(255) NOT NULL COMMENT '用户ID',
  `user_avatar` varchar(1024) DEFAULT NULL COMMENT '用户头像',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `category_id` varchar(255) NOT NULL COMMENT '分类ID',
  `content` mediumtext NOT NULL COMMENT '文章内容',
  `type` varchar(1) NOT NULL COMMENT '类型（0表示富文本，1表示markdown）',
  `state` varchar(1) NOT NULL COMMENT '状态（0表示已发布，1表示草稿，2表示删除）',
  `summary` text NOT NULL COMMENT '摘要',
  `labels` varchar(128) NOT NULL COMMENT '标签',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '阅读数量',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) DEFAULT '0' COMMENT '(0表示不删除,1表示删除)',
  `cover` varchar(1024) DEFAULT NULL COMMENT '封面',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '分类名称',
  `pinyin` varchar(128) NOT NULL COMMENT '拼音',
  `description` text NOT NULL COMMENT '描述',
  `order` int(11) NOT NULL COMMENT '顺序',
  `status` varchar(1) NOT NULL COMMENT '状态：0表示不使用，1表示正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `parent_content` text COMMENT '父内容',
  `article_id` varchar(255) NOT NULL COMMENT '文章ID',
  `content` text NOT NULL COMMENT '评论内容',
  `user_id` varchar(255) NOT NULL COMMENT '评论用户的ID',
  `user_avatar` varchar(1024) DEFAULT NULL COMMENT '评论用户的头像',
  `user_name` varchar(255) DEFAULT NULL COMMENT '评论用户的名称',
  `state` varchar(1) NOT NULL COMMENT '状态（0表示删除，1表示正常）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_daily_view_count
-- ----------------------------
DROP TABLE IF EXISTS `tb_daily_view_count`;
CREATE TABLE `tb_daily_view_count` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '每天浏览量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_friend_link`;
CREATE TABLE `tb_friend_link` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '友情链接名称',
  `logo` varchar(1024) NOT NULL COMMENT '友情链接logo',
  `url` varchar(1024) NOT NULL COMMENT '友情链接',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `state` varchar(1) NOT NULL COMMENT '友情链接状态:0表示不可用，1表示正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_images
-- ----------------------------
DROP TABLE IF EXISTS `tb_images`;
CREATE TABLE `tb_images` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `user_id` varchar(255) NOT NULL COMMENT '用户ID',
  `name` varchar(255) NOT NULL COMMENT '图片名称',
  `url` varchar(1024) NOT NULL COMMENT '路径',
  `content_type` varchar(255) NOT NULL COMMENT '图片类型',
  `state` varchar(1) NOT NULL COMMENT '状态（0表示删除，1表正常）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_img_looper
-- ----------------------------
DROP TABLE IF EXISTS `tb_img_looper`;
CREATE TABLE `tb_img_looper` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `title` varchar(128) NOT NULL COMMENT '轮播图标题',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `state` varchar(10) NOT NULL COMMENT '状态：0表示不可用，1表示正常',
  `target_url` varchar(1024) DEFAULT NULL COMMENT '目标URL',
  `image_url` varchar(2014) NOT NULL COMMENT '图片地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_labels
-- ----------------------------
DROP TABLE IF EXISTS `tb_labels`;
CREATE TABLE `tb_labels` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `name` varchar(32) NOT NULL COMMENT '标签名称',
  `count` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `tb_refresh_token`;
CREATE TABLE `tb_refresh_token` (
  `id` varchar(255) NOT NULL,
  `refresh_token` text NOT NULL COMMENT 'token',
  `user_id` varchar(255) NOT NULL COMMENT '用户Id',
  `mobile_token_key` varchar(255) DEFAULT NULL COMMENT '移动端的tokenkey',
  `token_key` varchar(255) DEFAULT NULL COMMENT 'token_key ，存放在redis中需要的用到的key',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_settings
-- ----------------------------
DROP TABLE IF EXISTS `tb_settings`;
CREATE TABLE `tb_settings` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `key` varchar(32) NOT NULL COMMENT '键',
  `value` varchar(512) NOT NULL COMMENT '值',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `roles` varchar(100) NOT NULL COMMENT '角色',
  `avatar` varchar(1024) NOT NULL COMMENT '头像地址',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `sign` varchar(100) DEFAULT NULL COMMENT '签名',
  `state` varchar(1) NOT NULL DEFAULT '1' COMMENT '状态：0表示删除，1表示正常',
  `reg_ip` varchar(32) NOT NULL COMMENT '注册ip',
  `login_ip` varchar(32) NOT NULL COMMENT '登录Ip',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- View structure for tb_article_view
-- ----------------------------
DROP VIEW IF EXISTS `tb_article_view`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `tb_article_view` AS select `tb_article`.`id` AS `id`,`tb_article`.`title` AS `title`,`tb_article`.`user_id` AS `user_id`,`tb_article`.`user_avatar` AS `user_avatar`,`tb_article`.`user_name` AS `user_name`,`tb_article`.`category_id` AS `category_id`,`tb_article`.`type` AS `type`,`tb_article`.`state` AS `state`,`tb_article`.`summary` AS `summary`,`tb_article`.`labels` AS `labels`,`tb_article`.`view_count` AS `view_count`,`tb_article`.`create_time` AS `create_time`,`tb_article`.`update_time` AS `update_time`,`tb_article`.`is_delete` AS `is_delete`,`tb_article`.`cover` AS `cover` from `tb_article` ;

-- ----------------------------
-- View structure for tb_user_view
-- ----------------------------
DROP VIEW IF EXISTS `tb_user_view`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `tb_user_view` AS select `tb_user`.`id` AS `id`,`tb_user`.`user_name` AS `user_name`,`tb_user`.`roles` AS `roles`,`tb_user`.`avatar` AS `avatar`,`tb_user`.`email` AS `email`,`tb_user`.`sign` AS `sign`,`tb_user`.`state` AS `state`,`tb_user`.`reg_ip` AS `reg_ip`,`tb_user`.`login_ip` AS `login_ip`,`tb_user`.`create_time` AS `create_time`,`tb_user`.`update_time` AS `update_time`,`tb_user`.`is_delete` AS `is_delete` from `tb_user` ;

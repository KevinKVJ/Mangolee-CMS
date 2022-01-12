SET NAMES utf8mb4;
SET foreign_key_checks = 0;

/** 用户表 **/
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`     VARCHAR(30)  NOT NULL COMMENT '用户名',
    `password`     VARCHAR(255) NOT NULL COMMENT '加密密码',
    `email`        VARCHAR(50)  NULL DEFAULT NULL COMMENT '邮箱',
    `role`         VARCHAR(30)  NULL DEFAULT NULL COMMENT '角色权限',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户';

/** 权限表(静态表) **/
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`           BIGINT               NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role`         VARCHAR(30) UNIQUE   NOT NULL COMMENT '权限角色名称', /** 管理员:ADMIN 普通用户:GUEST **/
    `mount`        INT      DEFAULT '1' NOT NULL COMMENT '启用状态', /** 默认启用 **/
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='权限';

/** 等级表(静态表) **/
DROP TABLE IF EXISTS `level`;
CREATE TABLE `level`
(
    `level`    INT NOT NULL DEFAULT 0 COMMENT '用户等级',    /** 管理员等级默认为0 guest等级从1开始 **/
    `description`   VARCHAR(512) NOT NULL COMMENT '等级描述',
    PRIMARY KEY (`level`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='等级';

SET NAMES utf8mb4;

/** 用户表 **/
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR (30) NOT NULL COMMENT '用户名',
  `password` VARCHAR (255) NOT NULL COMMENT '加密密码',
  `email` VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
  `role` VARCHAR(30) NULL DEFAULT NULL COMMENT '角色权限',
  `deleted` INT DEFAULT '0' COMMENT '逻辑删除',
  `version` INT DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE
      CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户';

/** 权限表(静态表) **/
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role` VARCHAR (30) UNIQUE NOT NULL COMMENT '权限角色名称',    /** 管理员:ADMIN 普通用户:GUEST **/
    `mount` INT DEFAULT '1' NOT NULL COMMENT '启用状态',    /** 默认启用 **/
    `deleted` INT DEFAULT '0' COMMENT '逻辑删除',
    `version` INT DEFAULT '1' COMMENT '乐观锁',
    `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE
        CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限';

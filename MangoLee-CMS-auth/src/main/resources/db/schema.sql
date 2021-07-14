SET NAMES utf8mb4;

/** 用户表 **/
drop table if exists `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(30) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '明文密码',
  `email` VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
  `deleted` int DEFAULT '0' COMMENT '逻辑删除',
  `version` int DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE
      CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户';


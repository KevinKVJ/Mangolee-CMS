
BEGIN;

/** 插入初始账号 **/
DELETE FROM `user`;
INSERT INTO `user` (`id`, `username`, `password`, `email`)
VALUES (1, 'Jack', '$2a$10$EdPwOZZwTHwMwHvCrDaOqOgcmP7m9yQY3r.s2cbzEhsr94GXo19vu', 'jack@mangolee.com'),    /** password1 **/
       (2, 'Sam','$2a$10$xDHCanPj54QHcgBcqbYOau1g19.Dxle.Lo7QmzH.j4WwGjpi8ycRW', 'sam@mangolee.com'),  /** password2 **/
       (3, 'Lee', '$2a$10$GOULD9f7zG7yWoX6ojlPF.Gm.m.inWQ8jIUmUHg09bRSbT00AVTby', 'lee@mangolee.com'),  /** password3 **/
       (4, 'Nick', '$2a$10$NZ6kUNq1w3unOEaFxJGzGe.yMpKbS7YHTB3J7o3rTkBh.h6eHAzLK', 'nick@mangolee.com'); /** password4 **/

/** 插入初始权限 **/
DELETE FROM `permission`;
INSERT INTO `permission` (`id`, `role`, `mount`)
VALUES (1, 'ADMIN', 1),
       (2, 'ADMIN', 1),
       (3, 'GUEST', 1),
       (4, 'GUEST', 1);

/** 分配权限 **/
DELETE FROM `userpermission`;
INSERT INTO `userpermission` (`id`, `userid`, `permissionid`)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 3),
       (4, 4, 4);

COMMIT;
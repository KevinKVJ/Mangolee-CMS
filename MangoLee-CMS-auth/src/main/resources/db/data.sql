/** 插入初始账号 **/
BEGIN;

DELETE FROM `user`;
INSERT INTO `user` (`id`, `username`, `password`, `email`)
VALUES (1, 'Jack', 'password1', 'jack@mangolee.com'),
       (2, 'Sam', 'password2', 'sam@mangolee.com'),
       (3, 'Lee', 'password3', 'lee@mangolee.com'),
       (4, 'Nick', 'password4', 'nick@mangolee.com');

COMMIT;
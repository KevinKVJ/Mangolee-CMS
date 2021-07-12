/** 插入初始账号 **/
BEGIN;

DELETE FROM `user`;
INSERT INTO `user` (`id`, `username`, `password`)
VALUES (1, 'Jack', 'password1'),
       (2, 'Sam', 'password2'),
       (3, 'Lee', 'password3'),
       (4, 'Nick', 'password4');

COMMIT;
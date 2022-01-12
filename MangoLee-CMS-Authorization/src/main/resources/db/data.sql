BEGIN;

/** 插入初始账号 **/
DELETE
FROM `user`;
INSERT INTO `user` (`id`, `username`, `password`, `email`, `role`, `level`)
VALUES (1, 'Jack','$2a$10$EdPwOZZwTHwMwHvCrDaOqOgcmP7m9yQY3r.s2cbzEhsr94GXo19vu','jack@mangolee.com', 'ADMIN',0), /**password1 **/
       (2, 'Sam','$2a$10$xDHCanPj54QHcgBcqbYOau1g19.Dxle.Lo7QmzH.j4WwGjpi8ycRW','sam@mangolee.com', 'ADMIN',0), /**password2 **/
       (3, 'Lee','$2a$10$GOULD9f7zG7yWoX6ojlPF.Gm.m.inWQ8jIUmUHg09bRSbT00AVTby','lee@mangolee.com', 'GUEST',1), /**password3 **/
       (4, 'Nick','$2a$10$NZ6kUNq1w3unOEaFxJGzGe.yMpKbS7YHTB3J7o3rTkBh.h6eHAzLK','nick@mangolee.com', 'GUEST',1), /**password4 **/
       (5, 'Tester','$2a$10$GxFKTtil49gwtTF8VvBdTe4aVYIN90XghsQwfzJDAtwNl4Z4e3pS2','test@mangolee.com', 'TEST',1); /**password5 **/

/** 插入初始权限 **/
DELETE
FROM `permission`;
INSERT INTO `permission` (`id`, `role`)
VALUES (1, 'ADMIN'),
       (2, 'GUEST'),
       (3, 'TEST');

COMMIT;

/** 插入初始等级 **/
DELETE
FROM `level`
INSERT INTO `level` (`level`, `description`)
VALUES
(0, 'ADMIN DEFAULT LEVEL'),
(1, 'GUEST DEFAULT LEVEL');
COMMIT;


/** 插入初始账号 **/
BEGIN;

DELETE FROM `user`;
INSERT INTO `user` (`id`, `username`, `password`, `email`)
VALUES (1, 'Jack', '$2a$10$EdPwOZZwTHwMwHvCrDaOqOgcmP7m9yQY3r.s2cbzEhsr94GXo19vu', 'jack@mangolee.com'),    /** password1 **/
       (2, 'Sam','$2a$10$xDHCanPj54QHcgBcqbYOau1g19.Dxle.Lo7QmzH.j4WwGjpi8ycRW', 'sam@mangolee.com'),  /** password2 **/
       (3, 'Lee', '$2a$10$GOULD9f7zG7yWoX6ojlPF.Gm.m.inWQ8jIUmUHg09bRSbT00AVTby', 'lee@mangolee.com'),  /** password3 **/
       (4, 'Nick', '$2a$10$NZ6kUNq1w3unOEaFxJGzGe.yMpKbS7YHTB3J7o3rTkBh.h6eHAzLK', 'nick@mangolee.com'); /** password4 **/

COMMIT;
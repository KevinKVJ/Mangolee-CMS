package org.mangolee;

import org.junit.jupiter.api.Test;
import org.mangolee.entity.User;
import org.mangolee.mapper.UserMapper;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class ApplicationTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void getAllUsers() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }
}

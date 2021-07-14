package org.mangolee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.mangolee.entity.User;
import org.mangolee.mapper.UserMapper;
import org.mangolee.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ApplicationTest {

    @Resource
    private UserService userService;

    //@Test
    void getAllUsersTest() {
        List<User> users = userService.list(null);
        users.forEach(System.out::println);
    }

    //@Test
    void updateUserPasswordTest() {
        String username = "Jack";
        String password = "password1";
        String newPassword = "password2";
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", password);
        // obtain
        User user = userService.getOne(wrapper);
        // update
        user.setPassword(newPassword);
        userService.update(user, wrapper);
        // print
        wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", newPassword);
        user = userService.getOne(wrapper);
        System.out.println(user);
    }

    //@Test
    void updateUserEmailTest() throws InterruptedException {
        String username = "Jack";
        String newEmail = "jackson@mangolee.com";
        User user = new User();
        user.setUsername(username);
        user.setEmail(newEmail);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        // update
        // wait for 5 seconds
        TimeUnit.SECONDS.sleep(5);
        userService.update(user, wrapper);
        // print
        wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("email", newEmail);
        user = userService.getOne(wrapper);
        System.out.println(user);
    }

    //@Test
    void logicalDeleteByUsernameTest() {
        String username = "Jack";
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        assertTrue(userService.remove(wrapper));
    }

    //@Test
    void logicalDeleteByIdTest() {
        Long id = 1L;
        assertTrue(userService.removeById(id));
    }

    //@Test
    void physicalDeleteByIdTest()  {
        Long id = 1L;
        userService.physicalDeleteById(id);
        assertNull(userService.getById(id));
    }

    //@Test
    void createUserTest() {
        User user = new User();
        user.setUsername("Lin");
        user.setPassword("passwordLin");
        user.setEmail("lin@mangolee.com");
        userService.save(user);
        user = userService.getOne(new QueryWrapper<User>().eq("username", "Lin"));
        System.out.println(user);
    }
}

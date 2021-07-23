package org.mangolee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.mangolee.config.SecurityConfig;
import org.mangolee.entity.User;
import org.mangolee.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        wrapper.eq("username", username);
        // obtain
        User user = userService.getOne(wrapper);
        assertTrue(new BCryptPasswordEncoder().matches(password, user.getPassword()));
        // update
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userService.update(user, wrapper);
        // print
        wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        user = userService.getOne(wrapper);
        assertTrue(new BCryptPasswordEncoder().matches(newPassword, user.getPassword()));
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

    // Test Redis
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //@Test
    void TestRedis(){
        redisTemplate.opsForValue().set("test","this is a test");
        System.out.println(redisTemplate.opsForValue().get("test"));
    }

    //@Test
    void f() {
        System.out.println(new BCryptPasswordEncoder().encode("password2"));
        System.out.println(new BCryptPasswordEncoder().encode("password3"));
        System.out.println(new BCryptPasswordEncoder().encode("password4"));
    }
}

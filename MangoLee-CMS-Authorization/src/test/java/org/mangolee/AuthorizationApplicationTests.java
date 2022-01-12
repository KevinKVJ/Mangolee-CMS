package org.mangolee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.mangolee.entity.User;
import org.mangolee.entity.UserInfo;
import org.mangolee.service.RedisService;
import org.mangolee.service.UserService;
import org.mangolee.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = AuthorizationApplication.class)
public class AuthorizationApplicationTests {

    @Resource
    private UserService  userService;
    // Test Redis
    @Resource
    private RedisService redisService;

    //@Test
    public void getAUser() {
        User user = userService.getById(1L);
        System.out.println(user);
        UserInfo userInfo = new UserInfo(user.getId(), null, user.getEmail(), user.getRole(),
                UUID.randomUUID().toString(), new Date(System.currentTimeMillis()));
        System.out.println(userInfo);
        String token = JwtUtils.createTokenFromUserInfo(userInfo, JwtUtils.SECRET_KEY, JwtUtils.SIGNATURE_ALGORITHM);
        System.out.println(token);
        userInfo = JwtUtils.getUserInfoFromToken(token, JwtUtils.SECRET_KEY, JwtUtils.SIGNATURE_ALGORITHM);
        System.out.println(userInfo);
    }

    @Test
    public void getAllUsersTest() {
        List<User> users = userService.list(null);
        users.forEach(System.out::println);
    }

    //@Test
    public void updateUserPasswordTest() {
        String             username    = "Jack";
        String             password    = "password1";
        String             newPassword = "password2";
        QueryWrapper<User> wrapper     = new QueryWrapper<>();
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
    public void updateUserEmailTest() throws InterruptedException {
        String username = "Jack";
        String newEmail = "jackson@mangolee.com";
        User   user     = new User();
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
    public void logicalDeleteByUsernameTest() {
        String             username = "Jack";
        QueryWrapper<User> wrapper  = new QueryWrapper<>();
        wrapper.eq("username", username);
        assertTrue(userService.remove(wrapper));
    }

    //@Test
    public void logicalDeleteByIdTest() {
        Long id = 1L;
        assertTrue(userService.removeById(id));
    }

    //@Test
    public void physicalDeleteByIdTest() {
        Long id = 1L;
        userService.physicalDeleteById(id);
        assertNull(userService.getById(id));
    }

    //@Test
    public void createUserTest() {
        User user = new User();
        user.setUsername("Lin");
        user.setPassword("passwordLin");
        user.setEmail("lin@mangolee.com");
        userService.save(user);
        user = userService.getOne(new QueryWrapper<User>().eq("username", "Lin"));
        System.out.println(user);
    }

    //@Test
    public void testRedis() {
        redisService.setValue("1234ss", "this is a test");
        System.out.println(redisService.getValue("1234ss"));
    }

}

package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.mangolee.entity.User;
import org.mangolee.entity.UserInfo;
import org.mangolee.exception.BaseException;
import org.mangolee.service.UserService;
import org.mangolee.utils.JwtUtils;
import org.mangolee.utils.Result;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@RestController
@Api(tags = "授权Controller")
public class AuthController {

    @Resource
    UserService userService;

    // 传入Username和Password作为参数, 调用下层Service接口
    public Result<String> login(@PathVariable("username") String username, @PathVariable("password") String password) {
        try {
            // 判断用户名是否为null或者空
            if (username == null || StringUtils.isEmpty(username)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 判断密码是否为null或者空
            if (password == null || StringUtils.isEmpty(password)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 根据用户生成User实体 并查询其在数据库是否存在
            User user = new User();
            user.setUsername(username);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            user = userService.getOne(queryWrapper);
            // 判断拥有该用户名的用户是否存在
            if (user == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 判断密码是否匹配
            if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 根据User生成对应的UserInfo
            UserInfo userInfo = new UserInfo(user.getId(), user.getUsername(), user.getEmail(),
                    user.getRole(), UUID.randomUUID().toString(), new Date(System.currentTimeMillis()));
            // 根据UserInfo生成对应的token
            String token = JwtUtils.createToken(userInfo);
            // 返回封装结果
            return Result.success(token);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    // Verify token
    public Result<UserInfo> verify(@PathVariable("token") String token) {
        // 检查token是否存在redis中

        // 如果不存在 报错

        // 如果存在 续期

        return null;
    }

    // Logout
    public Result<Void> logout(@PathVariable("token") String token) {
        // 删除redis中的token
        return null;
    }
}

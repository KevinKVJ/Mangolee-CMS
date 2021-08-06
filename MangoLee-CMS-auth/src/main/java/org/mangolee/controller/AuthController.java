package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.mangolee.entity.User;
import org.mangolee.entity.UserInfo;
import org.mangolee.exception.BaseException;
import org.mangolee.service.RedisService;
import org.mangolee.service.UserService;
import org.mangolee.service.impl.RedisServiceImpl;
import org.mangolee.utils.JwtUtils;
import org.mangolee.utils.Result;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/auth")
@Api(tags = "授权Controller")
public class AuthController {

    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    // 传入Username和Password作为参数, 调用下层Service接口
    @GetMapping("/login/{username}/{password}")
    public Result<String> login(@PathVariable("username") @NotNull String username,
                                @PathVariable("password") @NotNull String password) {
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
            String token = JwtUtils.createTokenFromUserInfo(userInfo, JwtUtils.SECRET_KEY
                    , JwtUtils.SIGNATURE_ALGORITHM);
            // 判断token是否为null
            if (token == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 将token保存到redis中去 并判断是否保存成功
            if (!redisService.setToken(token)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 返回封装结果
            return Result.success(token);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    // Verify token
    @PostMapping("/verify/{token}")
    public Result<UserInfo> verify(@PathVariable("token") @NotNull String token) {
        try {
            // 判断token是否为null
            if (token == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 检查token是否存在redis中及其类型
            Object object = redisService.get(token);
            if (!(object instanceof UserInfo)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 如果存在 续期一天 并检查是否成功
            if (!redisService.updateKeyTtl(token, RedisServiceImpl.DEFAULT_TTL)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 返回封装结果
            return Result.success((UserInfo) object);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    // Logout
    // 删除redis中的token
    @DeleteMapping("/del/{token}")
    public Result<Void> logout(@PathVariable("token") @NotNull String token) {
        try {
            if (token == null || !redisService.delete(token)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }
}

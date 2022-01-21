package org.mangolee.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Result;
import org.mangolee.entity.User;
import org.mangolee.entity.UserInfo;
import org.mangolee.service.RedisService;
import org.mangolee.service.UserService;
import org.mangolee.utils.JwtUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @ApiOperation("判断用户名和密码并在成功后生成token")
    @GetMapping("/login")
    public Result<String> login(
            @ApiParam(value = "用户名", required = true)
            @RequestParam("username") @NotNull String username,
            @ApiParam(value = "密码", required = true)
            @RequestParam("password") @NotNull String password) {

        // 判断用户名是否为null
        if (username == null) {
            return Result.error(400, "Username cannot be null");
        }
        // 判断用户名是否为null
        if (password == null) {
            return Result.error(400, "Password cannot be null");
        }
        // 根据用户生成User实体 并查询其在数据库是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username);
        User user = userService.getOne(queryWrapper);
        // 判断拥有该用户名的用户是否存在
        if (user == null) {
            return Result.error(400,"Username does not exist");
        }
        // 判断密码是否匹配
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return Result.error(400,"Password does not match");
        }
        // 根据User生成对应的UserInfo
        UserInfo userInfo = new UserInfo(user.getId(), user.getUsername(), user.getEmail(), user.getLevel(),
                user.getRole(), UUID.randomUUID().toString(), new Date());
        // 根据UserInfo生成对应的token
        String token = JwtUtils.createTokenFromUserInfo(userInfo, JwtUtils.SECRET_KEY
                , JwtUtils.SIGNATURE_ALGORITHM);
        // 判断token是否为null
        if (token == null) {
            return Result.error(500,"Token generation error");
        }
        // 将token保存到redis中去 并判断是否保存成功
        Result<Void> result = redisService.setValue(token, JSON.toJSONString(userInfo));
        //设置登录token保存时长
        redisService.updateKeyTtl(token, RedisService.DEFAULT_TTL);
        if (!Result.successful(result)) {
            return Result.error(500,"Redis save token error");
        }
        // 返回封装结果
        return Result.success(token);
    }

    @ApiOperation("根据token抽取UserInfo实例")
    @GetMapping("/verify")
    public Result<UserInfo> verify(
            @RequestParam String token) {
        if (token == null) {
            return Result.error(400, "Token is null");
        }
        return redisService.verify(token);
    }

    @ApiOperation("登出 即删除redis中的令牌")
    @DeleteMapping("/delete")
    public Result<Void> logout(
            @ApiParam(value = "令牌", required = true)
            @RequestParam("token") @NotNull String token) {
        if (token == null) {
            return Result.error(400, "Token is null");
        }
        return redisService.delete(token);
    }
}

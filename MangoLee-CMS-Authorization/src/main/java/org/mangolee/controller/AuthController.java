package org.mangolee.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Result;
import org.mangolee.entity.User;
import org.mangolee.entity.UserInfo;
import org.mangolee.exception.BaseException;
import org.mangolee.service.RedisService;
import org.mangolee.service.UserService;
import org.mangolee.utils.GlobalExceptionHandler;
import org.mangolee.utils.JwtUtils;
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

    @ApiOperation("判断用户名和密码并在成功后生成token")
    @GetMapping("/login/{username}/{password}")
    public Result<String> login(
            @ApiParam(value = "用户名", required = true)
            @PathVariable("username") @NotNull String username,
            @ApiParam(value = "密码", required = true)
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
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username);
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
            Result<Void> result = redisService.setValue(token, JSON.toJSONString(userInfo));
            //设置登录token保存时长
            redisService.updateKeyTtl(token,RedisService.DEFAULT_TTL);
            if (!Result.successful(result)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 返回封装结果
            return Result.success(token);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<String>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<String>().exceptionHandler(e);
        }
    }

    // Verify token
    @ApiOperation("根据token抽取UserInfo实例")
    @PostMapping("/verify/{token}")
    public Result<UserInfo> verify(
            @ApiParam(value = "令牌", required = true)
            @PathVariable("token") @NotNull String token) {
        // 判断token是否为null
        if (token == null) {
            throw new BaseException(Result.BAD_REQUEST);
        }
        // 检查token是否存在redis中及其类型
        Result<String> result = redisService.getValue(token);
        if (!Result.successful(result) || !(result.getData() instanceof String)) {
            throw new BaseException(Result.BAD_REQUEST);
        }
        // 如果存在 续期一天 并检查是否成功
        Result<Long> result1 = redisService.updateKeyTtl(token, RedisService.DEFAULT_TTL);
        if (!Result.successful(result1)) {
            throw new BaseException(Result.BAD_REQUEST);
        }

        //TODO 返回UserInfo封装结果
        return Result.success(JSON.parseObject(result.getData(), UserInfo.class));
    }

    // Logout
    @ApiOperation("删除redis中的令牌")
    @DeleteMapping("/delete/{token}")
    public Result<Void> logout(
            @ApiParam(value = "令牌", required = true)
            @PathVariable("token") @NotNull String token) {
        try {
            Result<Void> delete = redisService.delete(token);
            if (!Result.successful(delete)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return delete;
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Void>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Void>().exceptionHandler(e);
        }
    }
}

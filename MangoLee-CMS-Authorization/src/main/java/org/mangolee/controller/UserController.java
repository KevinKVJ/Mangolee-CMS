package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Permission;
import org.mangolee.entity.Result;
import org.mangolee.entity.User;
import org.mangolee.exception.BaseException;
import org.mangolee.service.PermissionService;
import org.mangolee.service.UserService;
import org.mangolee.utils.GlobalExceptionHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/user")
@Api(tags = "用户Controller")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @ApiOperation("根据主键ID获取用户")
    @GetMapping("/getbyid/{id}")
    public Result<User> getById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        return Result.success(userService.getById(id));
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/get")
    public Result<List<User>> getUsers() {
        return Result.success(userService.list());
    }

    @ApiOperation("根据用户名获取用户")
    @GetMapping("/get/{username}")
    public Result<User> getByUsername(@ApiParam(value = "用户名", required = true) @PathVariable(
            "username") @NotNull String username) {
        return Result.success(userService.getOne(new QueryWrapper<User>().eq("username", username)));
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/getall")
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.list());
    }

    @ApiOperation("根据邮箱获取用户")
    @GetMapping("/getbyemail/{email}")
    public Result<List<User>> getUsersByEmail(@ApiParam(value = "邮箱", required = true) @PathVariable(
            "email") @Email(message = "邮箱格式不正确") String email) {
        return Result.success(userService.list(new QueryWrapper<User>().eq("email", email)));
    }

    @ApiOperation("根据用户名修改用户权限")
    @PutMapping("/updaterole/{username}/{role}")
    public Result<User> updateRole(
            @ApiParam(value = "用户名", required = true)
            @PathVariable("username")
            @NotNull
                    String username,
            @ApiParam(value = "权限名称", required = true)
            @PathVariable("role")
            @NotNull
                    String role) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
            User               user    = userService.getOne(wrapper);
            // 若找不到用户则抛出异常
            if (user == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            QueryWrapper<Permission> wrapper1 = new QueryWrapper<Permission>().eq("role", role);
            // 若找不到权限则抛出异常
            Permission permission = permissionService.getOne(wrapper1);
            if (permission == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            user.setRole(role);
            if (!userService.update(user, wrapper)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(user);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<User>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<User>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据用户名和旧密码修改密码")
    @PutMapping("/updatepassword/{username}/{password}/{newpassword}")
    public Result<User> updatePassword(@ApiParam(value = "用户名", required = true) @PathVariable(
            "username") @NotNull String username,
                                       @ApiParam(value = "密码", required = true) @PathVariable("password") @NotNull String password,
                                       @ApiParam(value = "新密码", required = true) @PathVariable("newpassword") @NotNull String newPassword) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
            User               user    = userService.getOne(wrapper);
            // 若找不到用户则抛出异常
            if (user == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 若找到用户但密码不正确则抛出异常
            if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 更新用户
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userService.update(user, wrapper);
            wrapper = new QueryWrapper<User>().eq("username", username);
            user = userService.getOne(wrapper);
            return Result.success(user);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<User>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<User>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据用户名修改邮箱")
    @PutMapping("/updateemail/{username}/{email}")
    public Result<User> updateEmail(@ApiParam(value = "用户名", required = true) @PathVariable("username") @NotNull String username,
                                    @ApiParam(value = "新邮箱", required = true) @PathVariable("email") @Email(message = "邮箱格式不正确") String email) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
            User               user    = userService.getOne(wrapper);
            // 若找不到用户则抛出异常
            if (user == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            user.setEmail(email);
            if (!userService.update(user, wrapper)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(user);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<User>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<User>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据用户名进行逻辑删除")
    @DeleteMapping("/logicaldeletebyusername/{username}")
    public Result<Void> logicalDeleteByUsername(
            @ApiParam(value = "用户名", required = true)
            @PathVariable("username") @NotNull String username) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
            if (!userService.remove(wrapper)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Void>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Void>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据主键ID进行逻辑删除")
    @DeleteMapping("/logicaldeletebyid/{id}")
    public Result<Void> logicalDeleteById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        try {
            if (!userService.removeById(id)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Void>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Void>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据用户名密码邮箱权限创建新账号")
    @PostMapping("/create/{username}/{password}/{email}/{role}")
    public Result<User> createUser(@ApiParam(value = "用户名", required = true) @PathVariable("username") @NotNull String username,
                                   @ApiParam(value = "密码", required = true) @PathVariable("password") @NotNull String password,
                                   @ApiParam(value = "邮箱", required = true) @PathVariable("email") @Email(message =
                                           "邮箱格式不正确") String email,
                                   @ApiParam(value = "权限角色", required = true) @PathVariable("role") @NotNull String role

    ) {
        try {
            QueryWrapper<Permission> wrapper    = new QueryWrapper<Permission>().eq("role", role);
            Permission               permission = permissionService.getOne(wrapper);
            if (permission == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setEmail(email);
            user.setRole(role);
            if (!userService.save(user)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            QueryWrapper<User> wrapper1 = new QueryWrapper<User>().eq("username", username);
            user = userService.getOne(wrapper1);
            return Result.success(user);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<User>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<User>().exceptionHandler(e);
        }
    }

}

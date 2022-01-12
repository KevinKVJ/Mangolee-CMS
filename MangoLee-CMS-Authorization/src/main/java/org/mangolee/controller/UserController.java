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

    @ApiOperation("获取所有未被逻辑删除的用户")
    @GetMapping("/get")
    public Result<List<User>> getUsers() {
        return Result.success(userService.list());
    }

    @ApiOperation("根据用户名获取用户")
    @GetMapping("/get/{username}")
    public Result<User> getByUsername(@ApiParam(value = "用户名", required = true) @PathVariable(
            "username") @NotNull String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        return Result.success(userService.getOne(wrapper));
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/getall")
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @ApiOperation("根据邮箱获取用户")
    @GetMapping("/getbyemail/{email}")
    public Result<List<User>> getUsersByEmail(@ApiParam(value = "邮箱", required = true) @PathVariable(
            "email") @Email(message = "邮箱格式不正确") String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("email", email);
        return Result.success(userService.list(wrapper));
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
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User               user    = userService.getOne(wrapper);
        // 若找不到用户则抛出异常
        if (user == null) {
            return Result.error(400,"Can not find user");
        }
        QueryWrapper<Permission> wrapper1 = new QueryWrapper<Permission>().eq("role", role);
        // 若找不到权限则抛出异常
        Permission permission = permissionService.getOne(wrapper1);
        if (permission == null) {
            return Result.error(400,"Can not find permission for the user");
        }
        user.setRole(role);
        // 更新修改时间
        user.setGmtModified(null);
        if (!userService.update(user, wrapper)) {
            return Result.error(500,"Gmt update error");
        }
        return Result.success(user);
    }

    @ApiOperation("根据用户名和旧密码修改密码")
    @PutMapping("/updatepassword/{username}/{password}/{newpassword}")
    public Result<User> updatePassword(@ApiParam(value = "用户名", required = true) @PathVariable(
            "username") @NotNull String username,
                                       @ApiParam(value = "密码", required = true) @PathVariable("password") @NotNull String password,
                                       @ApiParam(value = "新密码", required = true) @PathVariable("newpassword") @NotNull String newPassword) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User               user    = userService.getOne(wrapper);
        // 若找不到用户则抛出异常
        if (user == null) {
            return Result.error(400,"Can not find user");
        }
        // 若找到用户但密码不正确则抛出异常
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return Result.error(400,"Password does not match");
        }
        // 更新用户
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        // 更新修改时间
        user.setGmtModified(null);
        userService.update(user, wrapper);
        wrapper = new QueryWrapper<User>().eq("username", username);
        user = userService.getOne(wrapper);
        return Result.success(user);
    }

    @ApiOperation("根据用户名修改邮箱")
    @PutMapping("/updateemail/{username}/{email}")
    public Result<User> updateEmail(@ApiParam(value = "用户名", required = true) @PathVariable("username") @NotNull String username,
                                    @ApiParam(value = "新邮箱", required = true) @PathVariable("email") @Email(message = "邮箱格式不正确") String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User               user    = userService.getOne(wrapper);
        // 若找不到用户则抛出异常
        if (user == null) {
            return Result.error(400,"Can not find user");
        }
        user.setEmail(email);
        // 更新修改时间
        user.setGmtModified(null);
        if (!userService.update(user, wrapper)) {
            return Result.error(500,"Email update error");
        }
        return Result.success(user);
    }

    @ApiOperation("根据用户名进行逻辑删除")
    @DeleteMapping("/logicaldeletebyusername/{username}")
    public Result<Void> logicalDeleteByUsername(
            @ApiParam(value = "用户名", required = true)
            @PathVariable("username") @NotNull String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        if (!userService.remove(wrapper)) {
            return Result.error(500,"Delete user error");
        }
        return Result.success();
    }

    @ApiOperation("根据主键ID进行逻辑删除")
    @DeleteMapping("/logicaldeletebyid/{id}")
    public Result<Void> logicalDeleteById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        if (!userService.removeById(id)) {
            return Result.error(500,"Delete user error");
        }
        return Result.success();
    }

    @ApiOperation("根据主键ID进行物理删除")
    @DeleteMapping("/physicaldeletebyid/{id}")
    public Result<Void> physicalDeleteById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        User user = userService.getUserByIdIgnoreLogicalDeletion(id);
        if (user == null) {
            return Result.error(400,"Get user error");
        }
        userService.physicalDeleteById(id);
        return Result.success();
    }

    @ApiOperation("根据用户名密码邮箱权限创建新账号")
    @PostMapping("/create/{username}/{password}/{email}/{role}")
    public Result<User> createUser(@ApiParam(value = "用户名", required = true) @PathVariable("username") @NotNull String username,
                                   @ApiParam(value = "密码", required = true) @PathVariable("password") @NotNull String password,
                                   @ApiParam(value = "邮箱", required = true) @PathVariable("email") @Email(message =
                                           "邮箱格式不正确") String email,
                                   @ApiParam(value = "权限角色", required = true) @PathVariable("role") @NotNull String role

    ) {
        QueryWrapper<Permission> wrapper    = new QueryWrapper<Permission>().eq("role", role);
        Permission               permission = permissionService.getOne(wrapper);
        if (permission == null) {
            return Result.error(400,"Get permission error");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEmail(email);
        user.setRole(role);
        if (!userService.save(user)) {
            return Result.error(500,"Save new user error");
        }
        QueryWrapper<User> wrapper1 = new QueryWrapper<User>().eq("username", username);
        user = userService.getOne(wrapper1);
        return Result.success(user);
    }

}

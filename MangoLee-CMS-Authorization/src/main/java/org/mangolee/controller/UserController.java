package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Permission;
import org.mangolee.entity.Result;
import org.mangolee.entity.User;
import org.mangolee.service.PermissionService;
import org.mangolee.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
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
    @GetMapping("/getbyid")
    public Result<User> getById(@ApiParam(value = "主键ID", required = true) @RequestParam("id") @NotNull Long id) {
        if (id == null) {
            return Result.error(400, "id is null");
        }
        return Result.success(userService.getById(id));
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/get")
    public Result<List<User>> get() {
        return Result.success(userService.list());
    }

    @ApiOperation("根据用户名获取用户")
    @GetMapping("/getbyusername")
    public Result<User> getByUsername(@ApiParam(value = "用户名", required = true) @RequestParam(
            "username") @NotNull String username) {
        if (username == null) {
            return Result.error(400, "username is null");
        }
        return Result.success(userService.getOne(new QueryWrapper<User>().eq("username", username)));
    }

    @ApiOperation("根据邮箱获取用户")
    @GetMapping("/getbyemail")
    public Result<List<User>> getUsersByEmail(@ApiParam(value = "邮箱", required = true) @RequestParam(
            "email") @Email(message = "邮箱格式不正确") String email) {
        if (email == null) {
            return Result.error(400, "email is null");
        }
        return Result.success(userService.list(new QueryWrapper<User>().eq("email", email)));
    }

    @ApiOperation("根据用户名修改用户权限")
    @PutMapping("/updaterole")
    public Result<User> updateRole(
            @ApiParam(value = "用户名", required = true)
            @RequestParam("username")
            @NotNull
                    String username,
            @ApiParam(value = "权限名称", required = true)
            @RequestParam("role")
            @NotNull
                    String role) {
        if (username == null) {
            return Result.error(400, "username is null");
        }
        if (role == null) {
            return Result.error(400, "role is null");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User               user    = userService.getOne(wrapper);
        // 若找不到用户则抛出异常
        if (user == null) {
            return Result.error(400,"Can not find user");
        }
        // 若找不到权限则抛出异常
        Permission permission = permissionService.getOne(new QueryWrapper<Permission>().eq("role", role));
        if (permission == null) {
            return Result.error(400,"Can not find permission with such role");
        }
        user.setRole(role);
        if (!userService.update(user, wrapper)) {
            return Result.error(400,"Failed to update the item");
        }
        return Result.success(user);
    }

    @ApiOperation("根据用户名和旧密码修改密码")
    @PutMapping("/updatepassword")
    public Result<User> updatePassword(
            @ApiParam(value = "用户名", required = true) @RequestParam("username") @NotNull String username,
            @ApiParam(value = "密码", required = true) @RequestParam("password") @NotNull String password,
            @ApiParam(value = "新密码", required = true) @RequestParam("newpassword") @NotNull String newPassword) {
        if (username == null) {
            return Result.error(400, "role is null");
        }
        if (password == null) {
            return Result.error(400, "password is null");
        }
        if (newPassword == null) {
            return Result.error(400, "new password is null");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User               user    = userService.getOne(wrapper);
        // 若找不到用户则抛出异常
        if (user == null) {
            return Result.error(400,"Can not find user with such username");
        }
        // 若找到用户但密码不正确则抛出异常
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return Result.error(400,"Password does not match");
        }
        // 更新用户
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        if (!userService.update(user, wrapper)) {
            return Result.error(400,"Failed to update the user");
        }
        return Result.success(userService.getOne(new QueryWrapper<User>().eq("username", username)));
    }

    @ApiOperation("根据用户名修改邮箱")
    @PutMapping("/updateemail")
    public Result<User> updateEmail(@ApiParam(value = "用户名", required = true) @RequestParam("username") @NotNull String username,
                                    @ApiParam(value = "新邮箱", required = true) @RequestParam("email") @Email(message = "邮箱格式不正确") String email) {
        if (username == null) {
            return Result.error(400, "Username is null");
        }
        if (email == null) {
            return Result.error(400, "email is null");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User               user    = userService.getOne(wrapper);
        if (user == null) {
            return Result.error(400, "Failed to find the user with such username");
        }
        user.setEmail(email);
        if (!userService.update(user, wrapper)) {
            return Result.error(400, "Failed to update the user");
        }
        return Result.success(user);
    }

    @ApiOperation("根据用户名进行删除")
    @DeleteMapping("/deletebyusername")
    public Result<Void> deleteByUsername(
            @ApiParam(value = "用户名", required = true)
            @RequestParam("username") @NotNull String username) {
        if (username == null) {
            return Result.error(400, "Username is null");
        }
        if (!userService.remove(new QueryWrapper<User>().eq("username", username))) {
            return Result.error(400, "Failed to delete the item");
        }
        return Result.success();
    }

    @ApiOperation("根据主键ID进行删除")
    @DeleteMapping("/deletebyid")
    public Result<Void> deleteById(@ApiParam(value = "主键ID", required = true) @RequestParam("id") @NotNull Long id) {
        if (id == null) {
            return Result.error(400, "id is null");
        }
        if (!userService.removeById(id)) {
            return Result.error(400, "Failed to delete the item");
        }
        return Result.success();
    }

    @ApiOperation("根据用户名密码邮箱权限创建新账号")
    @PostMapping("/create")
    public Result<User> createUser(
            @RequestBody HashMap<String,Object> hashMap) {
        if (!hashMap.containsKey("username") || hashMap.get("username") == null) {
            return Result.error(400, "username is null");
        }
        if (!hashMap.containsKey("password") || hashMap.get("password") == null) {
            return Result.error(400, "password is null");
        }
        if (!hashMap.containsKey("email") || hashMap.get("email") == null) {
            return Result.error(400, "email is null");
        }
        if (!hashMap.containsKey("role") || hashMap.get("role") == null) {
            return Result.error(400, "role is null");
        }
        Permission  permission = permissionService.getOne(new QueryWrapper<Permission>().eq("role", hashMap.get("role")));
        if (permission == null) {
            return Result.error(400, "Such role does not exist");
        }
        User user = new User();
        user.setUsername((String)hashMap.get("username"));
        user.setPassword(new BCryptPasswordEncoder().encode((String)hashMap.get("password")));
        user.setEmail((String)hashMap.get("email"));
        user.setRole((String)hashMap.get("role"));
        if ("ADMIN".equals(user.getRole())) {
            user.setLevel(0);
        } else {
            user.setLevel(1);
        }
        if (!userService.save(user)) {
            return Result.error(400, "Failed to insert the item");
        }
        return Result.success(userService.getOne(new QueryWrapper<User>().eq("username",
                user.getUsername())));
    }
}

package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.User;
import org.mangolee.exception.BaseException;
import org.mangolee.service.UserService;
import org.mangolee.entity.Result;
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

    @ApiOperation("根据主键ID获取用户")
    @GetMapping("/findbyid/{id}")
    public Result<User> getById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        try {
            return Result.success(userService.getById(id));
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("获取所有未被逻辑删除的用户")
    @GetMapping("/find/all")
    public Result<List<User> > getUsers() {
        try {
            return Result.success(userService.list());
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名获取用户")
    @GetMapping("/find/{username}")
    public Result<User> getByUsername(@ApiParam(value = "用户名", required = true) @PathVariable(
            "username") @NotNull String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        try {
            System.out.println(userService.getOne(wrapper));
            return Result.success(userService.getOne(wrapper));
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/find/allusers")
    public Result<List<User> > getAllUsers() {
        try {
            return Result.success(userService.getAllUsers());
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据邮箱获取用户")
    @GetMapping("/findbyemail/{email}")
    public Result<List<User> > getUsersByEmail(@ApiParam(value = "邮箱", required = true) @PathVariable(
            "email") @Email(message = "邮箱格式不正确") String email) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("email", email);
            List<User> users = userService.list(wrapper);
            if (users == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(users);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名和旧密码修改密码")
    @PutMapping("/updatepassword/{username}/{password}/{newpassword}")
    public Result<User> updatePassword(@ApiParam(value = "用户名", required = true) @PathVariable(
            "username") @NotNull String username,
                               @ApiParam(value = "密码", required = true) @PathVariable("password") @NotNull String password,
                               @ApiParam(value = "新密码", required = true) @PathVariable("newpassword") @NotNull String newPassword) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            User user = userService.getOne(wrapper);
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
            wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            user = userService.getOne(wrapper);
            // 若找不到更新后的用户则抛出异常
            if (user == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 更新后的用户密码不正确则抛出异常
            if (!new BCryptPasswordEncoder().matches(newPassword, user.getPassword())) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(user);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名修改邮箱")
    @PutMapping("/updateemail/{username}/{email}")
    public Result<User> updateEmail(@ApiParam(value = "用户名", required = true) @PathVariable("username") @NotNull String username,
                                    @ApiParam(value = "新邮箱", required = true) @PathVariable("email") @Email(message = "邮箱格式不正确") String email) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            User user = userService.getOne(wrapper);
            // 若找不到用户则抛出异常
            if (user == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            user.setEmail(email);
            userService.update(user, wrapper);
            wrapper = new QueryWrapper<>();
            wrapper.eq("username", username).eq("email", email);
            user = userService.getOne(wrapper);
            // 若找不到更新后的用户则抛出异常
            if (user == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(user);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名进行逻辑删除")
    @DeleteMapping("/logicaldeletebyusername/{username}")
    public Result<User> logicalDeleteByUsername(
            @ApiParam(value = "用户名", required = true)
            @PathVariable("username") @NotNull String username) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            if (!userService.remove(wrapper)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据主键ID进行逻辑删除")
    @DeleteMapping("/logicaldeletebyid/{id}")
    public Result<User> logicalDeleteById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        try {
            if (!userService.removeById(id)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据主键ID进行物理删除")
    @DeleteMapping("/physicaldeletebyid/{id}")
    public Result<User> physicalDeleteById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        try {
            userService.physicalDeleteById(id);
            if (userService.getUserByIdIgnoreLogicalDeletion(id) != null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名密码邮箱创建新账号")
    @PostMapping("/create/{username}/{password}/{email}")
    public Result<User> createUser(@ApiParam(value = "用户名", required = true) @PathVariable("username") @NotNull String username,
                                   @ApiParam(value = "密码", required = true) @PathVariable("password") @NotNull String password,
                                   @ApiParam(value = "邮箱", required = true) @PathVariable("email") @Email(message = "邮箱格式不正确") String email) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
            User user = userService.getOne(wrapper);
            // 检查是否存在此用户名 如果存在则抛出异常
            if (user != null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            user = new User();
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setEmail(email);
            if (!userService.save(user)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            user = userService.getOne(wrapper);
            // 若创建的用户找不到则抛出异常
            if (user == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(user);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

}

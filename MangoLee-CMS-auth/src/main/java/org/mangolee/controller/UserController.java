package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.User;
import org.mangolee.exception.BaseException;
import org.mangolee.service.UserService;
import org.mangolee.utils.Result;
import org.mangolee.utils.ResultEnum;
import org.mangolee.utils.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户Controller")
public class UserController {

    @Resource
    UserService userService;

    @ApiOperation("根据主键ID获取用户")
    @GetMapping("/findbyid/{id}")
    public Result<User> getById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") Long id) {
        try {
            return ResultUtils.success(userService.getById(id));
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("获取所有未被逻辑删除的用户")
    @GetMapping("/find/all")
    public Result<List<User> > getUsers() {
        try {
            return ResultUtils.success(userService.list());
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名和密码获取用户")
    @GetMapping("/find/{username}/{password}")
    public Result<List<User> > getByUsernameAndPassword(@ApiParam(value = "用户名", required = true) @PathVariable(
            "username") String username,
                                               @ApiParam(value = "密码", required = true) @PathVariable("password") String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", password);
        try {
            return ResultUtils.success(userService.list(wrapper));
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/find/allusers")
    public Result<List<User> > getAllUsers() {
        try {
            return ResultUtils.success(userService.getAllUsers());
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据邮箱获取用户")
    @GetMapping("/findbyemail/{email}")
    public Result<List<User> > getUsersByEmail(@ApiParam(value = "邮箱", required = true) @PathVariable(
            "email") String email) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("email", email);
            List<User> users = userService.list(wrapper);
            if (users == null) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            return ResultUtils.success(users);
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名和旧密码修改密码")
    @PutMapping("/updatepassword/{username}/{password}/{newpassword}")
    public Result<User> updatePassword(@ApiParam(value = "用户名", required = true) @PathVariable(
            "username") String username,
                               @ApiParam(value = "密码", required = true) @PathVariable("password") String password,
                               @ApiParam(value = "新密码", required = true) @PathVariable("newpassword") String newPassword) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username)
                    .eq("password", password);
            User user = userService.getOne(wrapper);
            // 若找不到用户则抛出异常
            if (user == null) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            user.setPassword(newPassword);
            userService.update(user, wrapper);
            wrapper = new QueryWrapper<>();
            wrapper.eq("username", username).eq("password", newPassword);
            user = userService.getOne(wrapper);
            // 若找不到更新后的用户则抛出异常
            if (user == null) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            return ResultUtils.success(user);
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名修改邮箱")
    @PutMapping("/updateemail/{username}/{email}")
    public Result<User> updateEmail(@ApiParam(value = "用户名", required = true) @PathVariable("username") String username,
                                    @ApiParam(value = "新邮箱", required = true) @PathVariable("email") String email) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            User user = userService.getOne(wrapper);
            // 若找不到用户则抛出异常
            if (user == null) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            user.setEmail(email);
            userService.update(user, wrapper);
            wrapper = new QueryWrapper<>();
            wrapper.eq("username", username).eq("email", email);
            user = userService.getOne(wrapper);
            // 若找不到更新后的用户则抛出异常
            if (user == null) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            return ResultUtils.success(user);
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名进行逻辑删除")
    @DeleteMapping("/logicaldeletebyusername/{username}")
    public Result<User> logicalDeleteByUsername(@ApiParam(value = "用户名", required = true) @PathVariable("username") String username) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            if (!userService.remove(wrapper)) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            return ResultUtils.success();
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据主键ID进行逻辑删除")
    @DeleteMapping("/logicaldeletebyid/{id}")
    public Result<User> logicalDeleteById(@ApiParam(value = "邮箱", required = true) @PathVariable("id") Long id) {
        try {
            if (!userService.removeById(id)) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            return ResultUtils.success();
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据主键ID进行物理删除")
    @DeleteMapping("/physicaldeletebyid/{id}")
    public Result<User> physicalDeleteById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") Long id) {
        try {
            userService.physicalDeleteById(id);
            if (userService.getUserByIdIgnoreLogicalDeletion(id) != null) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            return ResultUtils.success();
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名密码邮箱创建新账号")
    @PostMapping("/create/{username}/{password}/{email}")
    public Result<User> createUser(@ApiParam(value = "用户名", required = true) @PathVariable("username") String username,
                                   @ApiParam(value = "密码", required = true) @PathVariable("password") String password,
                                   @ApiParam(value = "邮箱", required = true) @PathVariable("email") String email) {
        try {
            // 检查是否存在此用户名 如果存在则抛出异常
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
            User user = userService.getOne(wrapper);
            if (user != null) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            if (!userService.save(user)) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            user = userService.getOne(wrapper);
            if (user == null) {
                throw new BaseException(ResultEnum.BAD_REQUEST);
            }
            return ResultUtils.success(user);
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

}

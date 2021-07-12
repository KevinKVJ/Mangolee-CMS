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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户Controller")
public class UserController {

    @Resource
    UserService userService;

    @ApiOperation("根据主键ID获取用户")
    @GetMapping("/{id}")
    public Result<User> getById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") Long id) {
        try {
            return ResultUtils.success(userService.getById(id));
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("获取所有未被逻辑删除的用户")
    @GetMapping("/all")
    public Result<List<User> > getUsers() {
        try {
            return ResultUtils.success(userService.list());
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("根据用户名和密码获取用户")
    @GetMapping("/{username}/{password}")
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
    @GetMapping("/allusers")
    public Result<List<User> > getAllUsers() {
        try {
            return ResultUtils.success(userService.getAllUsers());
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }
}

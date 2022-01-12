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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/permission")
@Api(tags = "权限Controller")
public class PermissionController {

    @Resource
    PermissionService permissionService;

    @Resource
    UserService userService;

    @ApiOperation("根据主键ID获取权限")
    @GetMapping("/getbyid/{id}")
    public Result<Permission> getById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        try {
            if (id == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(permissionService.getById(id));
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Permission>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Permission>().exceptionHandler(e);
        }
    }

    @ApiOperation("获取所有权限")
    @GetMapping("/get")
    public Result<List<Permission>> get() {
        try {
            return Result.success(permissionService.list());
        } catch (BaseException e) {
            return new GlobalExceptionHandler<List<Permission>>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<List<Permission>>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据主键ID和权限角色名修改权限名")
    @PostMapping("/update/{id}/{role}")
    public Result<Permission> update(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id,
            @ApiParam(value = "权限角色", required = true)
            @PathVariable("role")
            @NotNull String newRole
    ) {
        try {
            if (id == null || newRole == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 判断权限是否为null
            Permission permission = permissionService.getById(id);
            if (permission == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            String oldRole = permission.getRole();
            // 更新权限名称
            permission.setRole(newRole);
            if (!permissionService.updateById((permission))) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 批量更新User
            User user = new User();
            user.setRole(newRole);
            if (!userService.update(user, new QueryWrapper<User>().eq("role", oldRole))) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            permission = permissionService.getById(id);
            return Result.success(permission);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Permission>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Permission>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据主键ID进行删除")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteById(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id) {
        try {
            if (id == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            Permission permission = permissionService.getById(id);
            if (permission == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            String role = permission.getRole();
            if (!permissionService.removeById(id)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            userService.updateRoleBatchWithNull(role);
            return Result.success();
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Void>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Void>().exceptionHandler(e);
        }
    }
}

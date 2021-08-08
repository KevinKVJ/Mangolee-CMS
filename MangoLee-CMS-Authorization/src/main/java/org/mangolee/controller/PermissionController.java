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
    @GetMapping("/get/{id}")
    public Result<Permission> getById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        return Result.success(permissionService.getById(id));
    }

    @ApiOperation("获取所有未被逻辑删除的权限")
    @GetMapping("/get")
    public Result<List<Permission>> getPermissions() {
        return Result.success(permissionService.list());
    }

    @ApiOperation("获取所有权限")
    @GetMapping("/getall")
    public Result<List<Permission>> getAllPermissions() {
        return Result.success(permissionService.getAllPermissions());
    }

    @ApiOperation("根据权限角色名创建权限")
    @PostMapping("/create/{role}")
    public Result<Permission> createPermission(
            @ApiParam(value = "权限角色", required = true)
            @PathVariable("role")
            @NotNull String role
    ) {
        try {
            Permission permission = new Permission();
            permission.setRole(role);
            if (!permissionService.save(permission)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>().eq("role", role);
            permission = permissionService.getOne(wrapper);
            return Result.success(permission);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Permission>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Permission>().exceptionHandler(e);
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
            // 判断权限是否为null
            Permission permission = permissionService.getById(id);
            if (permission == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            String oldRole = permission.getRole();
            // 更新权限名称
            permission.setRole(newRole);
            permission.setGmtModified(null);
            if (!permissionService.updateById((permission))) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 批量更新User
            User user = new User();
            user.setRole(newRole);
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("role", oldRole);
            userService.update(user, wrapper);
            permission = permissionService.getById(id);
            return Result.success(permission);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Permission>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Permission>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据主键ID进行逻辑删除")
    @DeleteMapping("/logicaldeletebyid/{id}")
    public Result<Void> logicalDeleteById(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id) {
        try {
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

    @ApiOperation("根据角色权限进行逻辑删除")
    @DeleteMapping("/logicaldeletebyrole/{role}")
    public Result<Void> logicalDeleteByRole(
            @ApiParam(value = "权限角色", required = true)
            @PathVariable("role")
            @NotNull String role) {
        try {
            QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>().eq("role", role);
            if (!permissionService.remove(wrapper)) {
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

    @ApiOperation("根据主键ID进行物理删除")
    @DeleteMapping("/physicaldeletebyid/{id}")
    public Result<Void> physicalDeleteById(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id) {
        try {
            Permission permission = permissionService.getPermissionByIdIgnoreLogicalDeletion(id);
            if (permission == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            String role = permission.getRole();
            permissionService.physicalDeleteById(id);
            // User表只对未被逻辑删除的条目进行更新
            userService.updateRoleBatchWithNull(role);
            return Result.success();
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Void>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Void>().exceptionHandler(e);
        }
    }
}

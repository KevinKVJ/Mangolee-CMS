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

    @ApiOperation("获取所有未被逻辑删除的权限")
    @GetMapping("/get")
    public Result<List<Permission>> getPermissions() {
        try {
            return Result.success(permissionService.list());
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("获取所有权限")
    @GetMapping("/getall")
    public Result<List<Permission>> getAllPermissions() {
        try {
            return Result.success(permissionService.getAllPermissions());
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据权限角色名创建权限")
    @PostMapping("/create/{role}")
    public Result<Permission> createPermission(
            @ApiParam(value = "权限角色", required = true)
            @PathVariable("role")
            @NotNull String role
    ) {
        try {
            QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
                    .eq("role", role);
            Permission permission = permissionService.getOne(wrapper);
            // 检查是否存在此权限 如果存在则抛出异常
            if (permission != null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            permission = new Permission();
            permission.setRole(role);
            if (!permissionService.save(permission)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            permission = permissionService.getOne(wrapper);
            // 若创建的权限找不到则抛出异常
            if (permission == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(permission);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
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
            @NotNull String role
    ) {
        try {
            // 判断权限是否为null
            Permission permission = permissionService.getById(id);
            if (permission == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            String oldRole = permission.getRole();
            // 更新权限名称
            permission = new Permission();
            permission.setId(id);
            permission.setRole(role);
            if (!permissionService.save(permission)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            // 更新User表中的对应权限
            userService.saveBatchByRole(oldRole, role);
            permission = permissionService.getById(id);
            return Result.success(permission);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据主键ID进行逻辑删除")
    @DeleteMapping("/logicaldeletebyid/{id}")
    public Result<Permission> logicalDeleteById(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id) {
        try {
            if (!permissionService.removeById(id)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据主键ID进行物理删除")
    @DeleteMapping("/physicaldeletebyid/{id}")
    public Result<Permission> physicalDeleteById(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id) {
        try {
            permissionService.physicalDeleteById(id);
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据角色权限进行逻辑删除")
    @DeleteMapping("/logicaldeletebyrole/{role}")
    public Result<Permission> logicalDeleteByRole(
            @ApiParam(value = "权限角色", required = true)
            @PathVariable("role")
            @NotNull String role) {
        try {
            QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
                    .eq("role", role);
            if (!permissionService.remove(wrapper)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据角色权限进行物理删除")
    @DeleteMapping("/physicaldeletebyrole/{role}")
    public Result<Permission> physicalDeleteByRole(
            @ApiParam(value = "权限角色", required = true)
            @PathVariable("role")
            @NotNull String role) {
        try {
            permissionService.physicalDeleteByRole(role);
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }
}

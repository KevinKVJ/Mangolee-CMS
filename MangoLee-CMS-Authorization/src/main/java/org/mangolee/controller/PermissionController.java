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
import java.util.HashMap;
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
        if (id == null) {
            return Result.error(400, "id is null");
        }
        return Result.success(permissionService.getById(id));
    }

    @ApiOperation("获取所有权限")
    @GetMapping("/get")
    public Result<List<Permission>> get() {
        return Result.success(permissionService.list());
    }

    @ApiOperation("插入新权限")
    @PostMapping("/insert")
    public Result<Permission> insert(
            @RequestBody HashMap<String, Object> hashMap
            ) {
        if (!hashMap.containsKey("role") || hashMap.get("role") == null) {
            return Result.error(400, "role is null");
        }
        Permission permission = new Permission();
        permission.setRole((String)hashMap.get("role"));
        if (!permissionService.save(permission)) {
            return Result.error(400, "Failed to insert the item");
        }
        return Result.success(permission);
    }

    @ApiOperation("根据主键ID和权限角色名修改权限名")
    @PutMapping("/update/{id}/{role}")
    public Result<Permission> update(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id,
            @ApiParam(value = "权限角色", required = true)
            @PathVariable("role")
            @NotNull String newRole
    ) {
        if (id == null) {
            return Result.error(400, "id is null");
        }
        if (newRole == null) {
            return Result.error(400, "role is null");
        }
        // 判断权限是否为null
        Permission permission = permissionService.getById(id);
        if (permission == null) {
            return Result.error(400, "Cannot find the item with such id");
        }
        String oldRole = permission.getRole();
        // 更新权限名称
        permission.setRole(newRole);
        if (!permissionService.updateById((permission))) {
            return Result.error(400, "Failed to update the item");
        }
        // 批量更新User
        User user = new User();
        user.setRole(newRole);
        if (!userService.update(user, new QueryWrapper<User>().eq("role", oldRole))) {
            return Result.error(400, "Failed to update the item");
        }
        return Result.success(permissionService.getById(id));
    }

    @ApiOperation("根据主键ID进行删除")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteById(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id) {
        if (id == null) {
            return Result.error(400, "id is null");
        }
        Permission permission = permissionService.getById(id);
        if (permission == null) {
            return Result.error(400, "Cannot find item with such id");
        }
        String role = permission.getRole();
        if (!permissionService.removeById(id)) {
            return Result.error(400, "Failed to delete the item");
        }
        userService.updateRoleBatchWithNull(role);
        return Result.success();
    }
}

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
    public Result<Permission> getById(@ApiParam(value = "主键ID", required = true) @RequestParam("id") @NotNull Long id) {
        if (id == null) {
            return Result.error(400, "id不能为空");
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
        if (hashMap == null) {
            return Result.error(400, "参数对象不能为空");
        }
        if (!hashMap.containsKey("role")) {
            return Result.error(400, "role不存在");
        }
        if (hashMap.get("role") == null) {
            return Result.error(400, "role不能为空");
        }
        Permission permission = new Permission();
        permission.setRole((String) hashMap.get("role"));
        if (!permissionService.save(permission)) {
            return Result.error(400, "插入条目失败");
        }
        return Result.success(permission);
    }

    @ApiOperation("根据主键ID和权限角色名修改权限名")
    @PutMapping("/update")
    public Result<Permission> update(
            @ApiParam(value = "主键ID", required = true)
            @RequestParam("id")
            @NotNull Long id,
            @ApiParam(value = "权限角色", required = true)
            @RequestParam("role")
            @NotNull String newRole
    ) {
        if (id == null) {
            return Result.error(400, "id不能为空");
        }
        if (newRole == null) {
            return Result.error(400, "role不能为空");
        }
        // 判断权限是否为null
        Permission permission = permissionService.getById(id);
        if (permission == null) {
            return Result.error(400, "id不存在");
        }
        String oldRole = permission.getRole();
        // 更新权限名称
        permission.setRole(newRole);
        if (!permissionService.updateById((permission))) {
            return Result.error(400, "更新条目失败");
        }
        // 批量更新User
        User user = new User();
        user.setRole(newRole);
        if (!userService.update(user, new QueryWrapper<User>().eq("role", oldRole))) {
            return Result.error(400, "更新条目失败");
        }
        return Result.success(permissionService.getById(id));
    }

    @ApiOperation("根据主键ID进行删除")
    @DeleteMapping("/delete")
    public Result<Void> deleteById(
            @ApiParam(value = "主键ID", required = true)
            @RequestParam("id")
            @NotNull Long id) {
        if (id == null) {
            return Result.error(400, "id不能为空");
        }
        Permission permission = permissionService.getById(id);
        if (permission == null) {
            return Result.error(400, "id不存在");
        }
        String role = permission.getRole();
        if (!permissionService.removeById(id)) {
            return Result.error(400, "更新条目失败");
        }
        userService.updateRoleBatchWithNull(role);
        return Result.success();
    }
}

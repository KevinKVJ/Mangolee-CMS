package org.mangolee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mangolee.entity.Permission;
import org.mangolee.exception.BaseException;
import org.mangolee.service.PermissionService;
import org.mangolee.utils.Result;
import org.mangolee.utils.ResultEnum;
import org.mangolee.utils.ResultUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Validated
@RestController
@RequestMapping("/permission")
@Api(tags = "权限Controller")
public class PermissionController {

    @Resource
    PermissionService permissionService;

    @ApiOperation("获取所有未被逻辑删除的权限")
    @GetMapping("/find/all")
    public Result<List<Permission>> getPermissions() {
        try {
            return ResultUtils.success(permissionService.list());
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }

    @ApiOperation("获取所有权限")
    @GetMapping("/find/allpermissions")
    public Result<List<Permission>> getAllPermissions() {
        try {
            return ResultUtils.success(permissionService.getAllPermissions());
        } catch (Exception e) {
            throw new BaseException(ResultEnum.BAD_REQUEST);
        }
    }
}

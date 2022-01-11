package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Log;
import org.mangolee.entity.Result;
import org.mangolee.entity.User;
import org.mangolee.exception.BaseException;
import org.mangolee.service.LogService;
import org.mangolee.service.UserService;
import org.mangolee.utils.GlobalExceptionHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.regex.Pattern;

@Validated
@RestController
@RequestMapping("/log")
@Api(tags = "日志Controller")
public class LogController {

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;

    private static final Pattern ipv4Pattern = Pattern.compile("^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$))" +
            "{4}$");

    @ApiOperation("根据主键ID获取日志")
    @GetMapping("/getbyid/{id}")
    public Result<Log> getById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        return Result.success(logService.getById(id));
    }

    @ApiOperation("获取所有未被逻辑删除的日志")
    @GetMapping("/get")
    public Result<List<Log>> getLogs() {
        return Result.success(logService.list());
    }

    @ApiOperation("根据用户ID获取用户")
    @GetMapping("/get/{userid}")
    public Result<List<Log>> getByUsername(@ApiParam(value = "用户ID", required = true) @PathVariable(
            "userid") @NotNull Long userId) {
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>().eq("userId", userId);
        return Result.success(logService.list(wrapper));
    }

    @ApiOperation("根据状态码获取日志")
    @GetMapping("/getbystatuscode/{statusCode}")
    public Result<List<Log>> getUsersByStatusCode(@ApiParam(value = "状态码", required = true) @PathVariable(
            "statusCode") @NotNull String statusCode) {
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>().eq("statusCode", statusCode);
        return Result.success(logService.list(wrapper));
    }

    @ApiOperation("根据IPv4获取日志")
    @GetMapping("/getbyip/{ip}")
    public Result<List<Log>> getUsersByIpAddress(@ApiParam(value = "IPv4", required = true) @PathVariable(
            "ip") @NotNull String ipAddress) {
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>().eq("ipAddress", ipAddress);
        return Result.success(logService.list(wrapper));
    }

    @ApiOperation("创建日志")
    @PostMapping("/create/{userId}/{ipAddress}/{message}/{statusCode}")
    public Result<Void> createLog(
            @ApiParam(value = "用户ID", required = true) @PathVariable("userId") @NotNull Long userId,
            @ApiParam(value = "IPv4", required = true) @PathVariable("ipAddress") @NotNull String ipAddress,
            @ApiParam(value = "日志信息", required = true) @PathVariable("message") @NotNull String message,
            @ApiParam(value = "状态码", required = true) @PathVariable("statusCode") @NotNull String statusCode) {
        // 检查用户ID是否存在
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error(500,"User id does not exist");
        }
        // 检查ipv4是否合法
        if (!ipv4Pattern.matcher(ipAddress).matches()) {
            return Result.error(500,"ipv4 does not match");
        }
        Log log1 = new Log();
        log1.setUserId(userId);
        log1.setIpAddress(ipAddress);
        log1.setMessage(message);
        log1.setStatusCode(statusCode);
        if (!logService.save(log1)) {
            return Result.error(500,"log service error");
        }
        return Result.success();
    }

    @ApiOperation("根据主键ID逻辑删除日志")
    @DeleteMapping("/logicaldeletebyid/{id}")
    public Result<Void> logicalDeleteById(@ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id) {
        if (!logService.removeById(id)) {
            return Result.error(500,"log service remove logic id error");
        }
        return Result.success();
    }

    @ApiOperation("根据主键ID进行物理删除")
    @DeleteMapping("/physicaldeletebyid/{id}")
    public Result<Void> physicalDeleteById(
            @ApiParam(value = "主键ID", required = true)
            @PathVariable("id")
            @NotNull Long id) {
        Log log = logService.getLogByIdIgnoreLogicalDeletion(id);
        if (log == null) {
            return Result.error(500,"log service remove physical id error");
        }
        logService.physicalDeleteById(id);
        return Result.success();
    }

    @ApiOperation("根据主键ID修改日志")
    @PostMapping("/update/{id}/{userId}/{ipAddress}/{message}/{statusCode}")
    public Result<Log> updateLog(
            @ApiParam(value = "主键ID", required = true) @PathVariable("id") @NotNull Long id,
            @ApiParam(value = "用户ID", required = true) @PathVariable("userId") @NotNull Long userId,
                                             @ApiParam(value = "IPv4", required = true) @PathVariable("ipAddress") @NotNull String ipAddress,
                                             @ApiParam(value = "日志信息", required = true) @PathVariable("message") @NotNull String message,
                                             @ApiParam(value = "状态码", required = true) @PathVariable("statusCode") @NotNull String statusCode) {
        Log log = logService.getById(id);
        if (log == null) {
            return Result.error(500,"No log exist for the id");
        }
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error(500,"No user exist for the id");
        }
        // 检查ipv4是否合法
        if (!ipv4Pattern.matcher(ipAddress).matches()) {
            return Result.error(500,"Invalid ipv4");
        }
        log.setUserId(userId);
        log.setIpAddress(ipAddress);
        log.setMessage(message);
        log.setStatusCode(statusCode);
        // 更新修改时间
        log.setGmtModified(null);
        if (!logService.updateById(log)) {
            return Result.error(500,"Log update error");
        }
        return Result.success(logService.getById(log.getId()));
    }
}

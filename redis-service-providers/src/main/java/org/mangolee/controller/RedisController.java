package org.mangolee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Result;
import org.mangolee.entity.UserInfo;
import org.mangolee.service.RedisService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@Api(tags = "Redis Controller")
public class RedisController {

    @Resource
    private RedisService redisService;

    @ApiOperation("获取key的value value的类型为字符串")
    @GetMapping("/redisprovider/getvalue/{key}")
    public Result<String> getValue(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key) {
        return redisService.getValue(key);
    }

    @ApiOperation("存储或修改key的值为value value的类型为字符串")
    @PostMapping("/redisprovider/setvalue/{key}/{value}")
    public Result<Void> setValue(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key,
            @ApiParam(value = "值", required = true)
            @PathVariable("value") @NotNull String value) {
        return redisService.setValue(key, value);
    }


    @ApiOperation("获取redis中对应key的过期时间")
    @GetMapping("/redisprovider/getkeyttl/{key}")
    public Result<Long> getKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key) {
        return redisService.getKeyTtl(key);
    }

    @ApiOperation("根据键和新的过期时间更新redis旧的过期时间 以毫秒为单位")
    @PostMapping("/redisprovider/updatekeyttl/{key}/{newTtl}")
    public Result<Long> updateKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key,
            @ApiParam(value = "新的过期时间", required = true)
            @PathVariable("newTtl") @NotNull Long newTtl) {
        return redisService.updateKeyTtl(key, newTtl);
    }

    @ApiOperation("删除redis中的指定键")
    @DeleteMapping("/redisprovider/delete/{key}")
    public Result<Void> delete(
            @ApiParam(value = "键", required = true)
            @PathVariable("key")
            @NotNull String key) {
        return redisService.delete(key);
    }

    @ApiOperation("列出redis所有key")
    @GetMapping("/redisprovider/getkeys")
    public Result<List<String>> getKeys() {
        return redisService.getKeys();
    }

    @ApiOperation("验证key获取信息并更新过期时间")
    @GetMapping("/redisprovider/verify/{key}")
    public Result<UserInfo> verify( @ApiParam(value = "键", required = true)
                                        @PathVariable("key")
                                        @NotNull String key) {
        return redisService.verify(key);
    }
}

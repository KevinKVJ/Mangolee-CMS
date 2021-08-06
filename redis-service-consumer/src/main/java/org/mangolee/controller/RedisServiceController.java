package org.mangolee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.service.RedisFeignService;
import org.mangolee.entity.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

//用于测试RedisFeignService接口
@Validated
@RestController
@RequestMapping("/redisconsumer")
@Api(tags = "Redis Service Controller")
public class RedisServiceController {

    @Resource
    private RedisFeignService redisFeignService;

    @ApiOperation("往redis中存储token")
    @GetMapping("/settoken/{token}")
    public Result<Boolean> setToken(
            @ApiParam(value = "令牌", required = true)
            @PathVariable("token")
            @NotNull String token){
        return redisFeignService.setToken(token);
    }

    @ApiOperation("获取redis中对应key的过期时间")
    @GetMapping("/getkeyttl/{key}")
    public Result<Long> getKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key){
        return redisFeignService.getKeyTtl(key);
    }

    @ApiOperation("根据键和新的过期时间更新redis旧的过期时间")
    @PostMapping("/updatekeyttl/{key}/{newTtl}")
    public Result<Boolean> updateKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key,
            @ApiParam(value = "新的过期时间", required = true)
            @PathVariable("newTtl") @NotNull Long newTtl){
        return redisFeignService.updateKeyTtl(key,newTtl);
    }

    @ApiOperation("删除redis中的指定键")
    @DeleteMapping("/delete/{key}")
    public Result<Boolean> delete(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key){
        return redisFeignService.delete(key);
    }


}

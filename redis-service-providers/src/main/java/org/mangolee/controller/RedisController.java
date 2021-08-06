package org.mangolee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.exception.BaseException;
import org.mangolee.service.RedisService;
import org.mangolee.entity.Result;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@Api(tags = "Redis Controller")
public class RedisController {

    @Resource
    private RedisService redisService;

    @ApiOperation("获取key的value value的类型为字符串")
    @GetMapping("/redisprovider/get/{key}")
    public Result<String> get(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key) {
        try {
            String valueAsString = redisService.getValueAsString(key);
            if (valueAsString == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(valueAsString);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }


    @ApiOperation("存储或修改key的值为value value的类型为字符串")
    @PostMapping("/redisprovider/set/{key}/{value}")
    public Result<Void> set(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key,
            @ApiParam(value = "值", required = true)
            @PathVariable("value") @NotNull String value)
    {
        try {
            redisService.setValueAsString(key, value);
            return Result.success();
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("往redis中存储token")
    @GetMapping("/redisprovider/settoken/{token}")
    public Result<Boolean> setToken(
            @ApiParam(value = "令牌", required = true)
            @PathVariable("token") @NotNull String token) {
        try {
            Boolean aBoolean = redisService.setToken(token);
            if (!aBoolean) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(aBoolean);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("获取redis中对应key的过期时间")
    @GetMapping("/redisprovider/getkeyttl/{key}")
    public Result<Long> getKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key) {
        try {
            Long ttl = redisService.getKeyTtl(key);
            if (ttl == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(ttl);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据键和新的过期时间更新redis旧的过期时间")
    @PostMapping("/redisprovider/updatekeyttl/{key}/{newTtl}")
    public Result<Boolean> updateKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key,
            @ApiParam(value = "新的过期时间", required = true)
            @PathVariable("newTtl") @NotNull Long newTtl)
    {
        try {
            Boolean updateKeyTtl = redisService.updateKeyTtl(key, newTtl);
            if (!updateKeyTtl) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(updateKeyTtl);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("删除redis中的指定键")
    @DeleteMapping("/redisprovider/delete/{key}")
    public Result<Boolean> delete(
            @ApiParam(value = "键", required = true)
            @PathVariable("key")
            @NotNull String key) {
        try {
            Boolean delete = redisService.delete(key);
            if (!delete) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(delete);
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

}

package org.mangolee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Result;
import org.mangolee.exception.BaseException;
import org.mangolee.service.RedisService;
import org.mangolee.utils.GlobalExceptionHandler;
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
    @GetMapping("/redisprovider/getvalueasstring/{key}")
    public Result<String> getValueAsString(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key) {
        try {
            Result<String> result = redisService.getValueAsString(key);
            if (!Result.successful(result)) {
                System.out.println(result);
                throw new BaseException(Result.BAD_REQUEST);
            }
            return result;
        } catch (BaseException e) {
            return new GlobalExceptionHandler<String>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<String>().exceptionHandler(e);
        }
    }

    @ApiOperation("存储或修改key的值为value value的类型为字符串")
    @PostMapping("/redisprovider/setvalueasstring/{key}/{value}")
    public Result<Void> setValueAsString(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key,
            @ApiParam(value = "值", required = true)
            @PathVariable("value") @NotNull String value) {
        try {
            Result<Void> result = redisService.setValueAsString(key, value);
            if (!Result.successful(result)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return result;
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Void>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Void>().exceptionHandler(e);
        }
    }

    @ApiOperation("往redis中存储token")
    @GetMapping("/redisprovider/settoken/{token}")
    public Result<String> setToken(
            @ApiParam(value = "令牌", required = true)
            @PathVariable("token") @NotNull String token) {
        try {
            Result<String> result = redisService.setToken(token);
            if (!Result.successful(result)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return result;
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
            Result<Long> result = redisService.getKeyTtl(key);
            if (!Result.successful(result)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return result;
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("根据键和新的过期时间更新redis旧的过期时间 以毫秒为单位")
    @PostMapping("/redisprovider/updatekeyttl/{key}/{newTtl}")
    public Result<Long> updateKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key,
            @ApiParam(value = "新的过期时间", required = true)
            @PathVariable("newTtl") @NotNull Long newTtl) {
        try {
            Result<Long> result = redisService.updateKeyTtl(key, newTtl);
            if (!Result.successful(result)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return result;
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

    @ApiOperation("删除redis中的指定键")
    @DeleteMapping("/redisprovider/delete/{key}")
    public Result<Void> delete(
            @ApiParam(value = "键", required = true)
            @PathVariable("key")
            @NotNull String key) {
        try {
            Result<Void> result = redisService.delete(key);
            if (!Result.successful(result)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return result;
        } catch (Exception e) {
            throw new BaseException(Result.BAD_REQUEST);
        }
    }

}

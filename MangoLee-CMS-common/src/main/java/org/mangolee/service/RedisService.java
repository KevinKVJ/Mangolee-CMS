package org.mangolee.service;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Component
@FeignClient("redis-service-providers")
public interface RedisService {

    @ApiOperation("存储或修改key的值为value value的类型为字符串")
    @PostMapping("/redisprovider/setvalueasstring/{key}/{value}")
    Result<Void> setValueAsString(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull
                    String key,
            @ApiParam(value = "值", required = true)
            @PathVariable("value") @NotNull
                    String value);

    @ApiOperation("存储或修改key的值为value")
    @PostMapping("/redisprovider/set/{key}/{value}")
    Result<Void> set(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull
                    String key,
            @ApiParam(value = "值", required = true)
            @PathVariable("value") @NotNull
                    Object value);

    @ApiOperation("获取key的value value的类型为字符串")
    @GetMapping("/redisprovider/getvalueasstring/{key}")
    Result<String> getValueAsString(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull
                    String key);

    @ApiOperation("获取key的value")
    @PostMapping("/redisprovider/get/{key}")
    Result<Object> get(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull
                    String key);

    @ApiOperation("往redis中存储token")
    @GetMapping("/redisprovider/settoken/{token}")
    Result<String> setToken(
            @ApiParam(value = "令牌", required = true)
            @PathVariable("token") @NotNull
                    String token);

    @ApiOperation("获取redis中对应key的过期时间")
    @GetMapping("/redisprovider/getkeyttl/{key}")
    Result<Long> getKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull
                    String key);

    @ApiOperation("根据键和新的过期时间更新redis旧的过期时间 以毫秒为单位")
    @PostMapping("/redisprovider/updatekeyttl/{key}/{newTtl}")
    Result<Long> updateKeyTtl(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull String key,
            @ApiParam(value = "新的过期时间", required = true)
            @PathVariable("newTtl") @NotNull Long newTtl);

    @ApiOperation("删除redis中的指定键")
    @DeleteMapping("/redisprovider/delete/{key}")
    Result<Void> delete(
            @ApiParam(value = "键", required = true)
            @PathVariable("key")
            @NotNull
                    String key);

    @ApiOperation("列出redis所有key")
    @GetMapping("/redisprovider/getkeys")
    Result<List<String>> getKeys();
}

package org.mangolee.service;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Result;
import org.mangolee.entity.UserInfo;
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
    public final static Long DEFAULT_TTL = 24 * 60 * 60 * 1000L;
    public final static String DEFAULT_VALUE = "";
    @ApiOperation("存储或修改key的值为value value的类型为字符串")
    @PostMapping("/redisprovider/setvalue/{key}/{value}")
    Result<Void> setValue(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull
                    String key,
            @ApiParam(value = "值", required = true)
            @PathVariable("value") @NotNull
                    String value);

    @ApiOperation("获取key的value value的类型为字符串")
    @GetMapping("/redisprovider/getvalue/{key}")
    Result<String> getValue(
            @ApiParam(value = "键", required = true)
            @PathVariable("key") @NotNull
                    String key);


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

    @ApiOperation("验证key是否存在并获取对应用户信息")
    @GetMapping("/redisprovider/verify/{key}")
    Result<UserInfo> verify(
            @ApiParam(value = "键", required = true)
            @PathVariable("key")
            @NotNull
                    String key);
}

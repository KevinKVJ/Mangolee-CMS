package org.mangolee.service;

import org.mangolee.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.constraints.NotNull;

//可以直接复制到包里调用
@Validated
@Component
@FeignClient("redis-service-providers")
public interface RedisFeignService {

    //redis存储token
    @GetMapping("/redisprovider/settoken/{token}")
    Result<Boolean> setToken(@PathVariable("token") @NotNull String token);

    //redis获得指定key存活时间
    @GetMapping("/redisprovider/getkeyttl/{key}")
    Result<Long> getKeyTtl(@PathVariable("key") @NotNull String key);

    //redis更新key存活时间
    @PostMapping("/redisprovider/updatekeyttl/{key}/{newTtl}")
    Result<Boolean> updateKeyTtl(@PathVariable("key") @NotNull String key,
                         @PathVariable("newTtl") @NotNull Long newTtl);

    //redis删除键
    @DeleteMapping("/redisprovider/delete/{key}")
    Result<Boolean> delete(@PathVariable("key") @NotNull String key);
}

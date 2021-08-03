package org.mangolee.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

//可以直接复制到包里调用
@Component
@FeignClient("redis-service-providers")
public interface RedisFeignService {

    //用于测试异常处理
    @GetMapping("/test")
    String test();

    //redis存储token
    @GetMapping("/provider/redisService/set/{token}")
    Boolean setToken(@PathVariable("token") @NotNull String token);

    //redis获得指定token存活时间
    @GetMapping("/provider/redisService/getttl/{token}")
    Long getTokenTtl(@PathVariable("token") @NotNull String token);

    //redis更新token存活时间
    @GetMapping("/provider/redisService/updatettl/{token}/{newTtl}")
    Boolean updateTokenTtl(@PathVariable("token") @NotNull String token,
                           @PathVariable("newTtl") @NotNull Long newTtl);
}
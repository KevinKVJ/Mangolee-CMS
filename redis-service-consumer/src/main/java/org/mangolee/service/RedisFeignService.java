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
    Boolean setToken(@PathVariable("token")  String token);

    //redis获得指定key存活时间
    @GetMapping("/provider/redisService/getTTL/{key}")
    Long getKeyTtl(@PathVariable("key") String key);

    //redis更新key存活时间
    @PostMapping("/provider/redisService/updateTTL/{key}/{newTtl}")
    Boolean updateKeyTtl(@PathVariable("key")  String key,
                         @PathVariable("newTtl") Long newTtl);

    @PostMapping("/provider/redisService/delete/{key}")
    Boolean delete(@PathVariable("key") String key);
}

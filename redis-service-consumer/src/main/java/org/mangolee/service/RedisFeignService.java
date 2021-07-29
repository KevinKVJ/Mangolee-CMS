package org.mangolee.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
//可以直接复制到包里调用
@Component
@FeignClient("redis-service-providers")
public interface RedisFeignService {

    @GetMapping("/provider/redisService/get/{id}")
    String getValue(@PathVariable("id") String key);

    @GetMapping("/provider/redisService/set")
    void setValue(@RequestParam String key, @RequestParam String value);

    @GetMapping("/test")
    public String test();
}

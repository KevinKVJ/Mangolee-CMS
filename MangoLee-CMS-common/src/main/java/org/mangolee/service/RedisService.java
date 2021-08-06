package org.mangolee.service;

import org.mangolee.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("redis-service-providers")
public interface RedisService {
    @GetMapping("/verify")
    public Result verify(@RequestHeader String token);
}

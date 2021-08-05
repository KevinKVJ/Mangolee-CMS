package org.mangolee.controller;

import org.mangolee.entity.User;
import org.mangolee.exception.MyFeignException;
import org.mangolee.service.RedisFeignService;
import org.mangolee.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
//用于测试RedisFeignService接口
@RestController
public class RedisServiceController {

    @Resource
    private RedisFeignService redisFeignService;


    @GetMapping("consumer/testException")
    public String test() {
        try {
            return redisFeignService.test();
        } catch (Exception e) {
            throw new MyFeignException(new Result().INTERNAL_ERROR);
        }
    }
    @GetMapping("/consumer/redisService/set/{token}")
    public Boolean setToken(@PathVariable("token")  String token){
        return redisFeignService.setToken(token);
    }

    @GetMapping("/consumer/redisService/getTTL/{token}")
    public Long getTokenTTL(@PathVariable("token") String token){
        return redisFeignService.getTokenTTL(token);
    }

    @PostMapping("/consumer/redisService/updateTTL/{token}/{newTtl}")
    public Boolean updateTokenTTL(@PathVariable("token")  String token,
                           @PathVariable("newTtl") Long newTtl){
        return redisFeignService.updateTokenTTL(token,newTtl);
    }

    @PostMapping("/provider/redisService/remove/{key}")
    public Boolean remove(@PathVariable("key") String key){
        return redisFeignService.remove(key);
    }


}

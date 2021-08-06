package org.mangolee.controller;

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
            throw new MyFeignException(Result.INTERNAL_ERROR);
        }
    }
    @GetMapping("/consumer/redisService/set/{token}")
    public Boolean setToken(@PathVariable("token")  String token){
        return redisFeignService.setToken(token);
    }

    @GetMapping("/consumer/redisService/getTTL/{key}")
    public Long getKeyTtl(@PathVariable("key") String key){
        return redisFeignService.getKeyTtl(key);
    }

    @PostMapping("/consumer/redisService/updateTTL/{key}/{newTtl}")
    public Boolean updateKeyTtl(@PathVariable("key")  String key,
                                @PathVariable("newTtl") Long newTtl){
        return redisFeignService.updateKeyTtl(key,newTtl);
    }

    @PostMapping("/provider/redisService/delete/{key}")
    public Boolean delete(@PathVariable("key") String key){
        return redisFeignService.delete(key);
    }


}

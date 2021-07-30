package org.mangolee.controller;

import org.mangolee.exception.MyFeignException;
import org.mangolee.service.RedisFeignService;
import org.mangolee.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
public class RedisServiceController {

    @Resource
    private RedisFeignService redisFeignService;

    @GetMapping("/consumer/redisService/set")
    public void set(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisFeignService.setValue(key, value);
    }

    @GetMapping("/consumer/redisService/get/{id}")
    public String get(@PathVariable("id") String key) {
        return redisFeignService.getValue(key);
    }

    @GetMapping("consumer/testException")
    public String test(){
        try{
            return redisFeignService.test();
        }catch (Exception e){
            throw new MyFeignException(new Result().INTERNAL_ERROR);
        }

    }
}

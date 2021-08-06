package org.mangolee.controller;

import org.mangolee.service.RedisService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
public class RedisController {

    @Resource
    private RedisService redisService;

    //redis根据id获取值
    @GetMapping("/provider/redisService/get/{id}")
    public Object getValue(@PathVariable("id") String key) {
        System.out.println(redisService.get(key));
        return redisService.get(key);
    }

    //检测异常是否被捕获
    @GetMapping("/testException")
    public String test() throws InterruptedException {
        Thread.sleep(5000);
        return "Test";
    }

    //redis存储key value
    @PostMapping("/provider/redisService/set")
    public void setValue(@RequestParam("key") @NotNull String key,
                         @RequestBody @NotNull Object value)
    {
        redisService.set(key,value);
    }

    @GetMapping("/provider/redisService/set/{token}")
    public Boolean setToken(@PathVariable("token")  String token){
        return redisService.setToken(token);
    }

    @GetMapping("/provider/redisService/getTTL/{key}")
    public Long getKeyTtl(@PathVariable("key") String key){
        return redisService.getKeyTtl(key);
    }

    @PostMapping("/provider/redisService/updateTTL/{key}/{newTtl}")
    public Boolean updateKeyTtl(@PathVariable("key")  String key,
                                @PathVariable("newTtl") Long newTtl)
    {
        return redisService.updateKeyTtl(key,newTtl);
    }

    @PostMapping("/provider/redisService/delete/{key}")
    public Boolean delete(@PathVariable("key") String key){
        return redisService.delete(key);
    }

}

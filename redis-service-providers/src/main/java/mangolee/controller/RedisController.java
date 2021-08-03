package mangolee.controller;

import mangolee.service.RedisService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@RestController
public class RedisController {
    @Resource
    private RedisService redisService;

    //redis根据key获取value
    @GetMapping("/provider/redisService/get/{key}")
    public Object getValue(@PathVariable("key") @NotNull String key) {
        System.out.println(redisService.get(key));
        return redisService.get(key);
    }

    //检测异常是否被捕获
    @GetMapping("/testException")
    public String test() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(5000);
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

    @GetMapping("/provider/redisService/getttl/{token}")
    public Long getTokenTtl(@PathVariable("token") String token){
        return redisService.getTokenTtl(token);
    }

    @GetMapping("/provider/redisService/updatettl/{token}/{newTtl}")
    public Boolean updateTokenTtl(@PathVariable("token")  String token,
                                  @PathVariable("newTtl") Long newTtl)
    {
        return redisService.updateTokenTtl(token,newTtl);
    }




}
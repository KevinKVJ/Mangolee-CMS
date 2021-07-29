package mangoleeredis.controller;



import mangoleeredis.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RedisController {
    @Resource
    private RedisService redisService;

    //redis存储key value
    @GetMapping("/provider/redisService/set")
    public void setValue(@RequestParam String key, @RequestParam String value) {
        redisService.set(key, value);
    }


    //redis根据id获取值
    @GetMapping("/provider/redisService/get/{id}")
    public String getValue(@PathVariable("id") String key) {
        return redisService.get(key);
    }

    //检测异常是否被捕获
    @GetMapping("/testException")
    public String test() throws InterruptedException {
        Thread.currentThread().sleep(5000);
        return "Test";
    }

}

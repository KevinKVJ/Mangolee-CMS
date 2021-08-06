package mangolee;

import org.junit.jupiter.api.Test;
import org.mangolee.RedisServiceProvidersApplication;
import org.mangolee.service.RedisService;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = RedisServiceProvidersApplication.class)
class RedisServiceProvidersTests {

    // Test Redis
    @Resource
    private RedisService redisService;

    //@Test
    void testRedisSetAndGet() {
        redisService.set("test1", "test1value");
        System.out.println(redisService.get("test1"));
        System.out.println(redisService.get("test2"));
    }
}

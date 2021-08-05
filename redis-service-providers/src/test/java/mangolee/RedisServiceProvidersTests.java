package mangolee;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mangolee.service.RedisService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
class RedisServiceProvidersTests {

    // Test Redis
    @Resource
    private RedisService redisService;

    @Test
    void testRedisSetAndGet() {
        redisService.set("test1", "test1value");
        System.out.println(redisService.get("test1"));
        System.out.println(redisService.get("test2"));
    }

}

package mangolee.service.impl;

import mangolee.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    //空value
    private final static String EMPTY_VALUE = "";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${data.ttl}")
    private String                        ttl;


    //存值，默认一天
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, Integer.parseInt(ttl), TimeUnit.DAYS);
    }

    //取值
    @Override
    public Object get(String key) {
        // 判断键值是否为null
        if (key == null) {
            return null;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        // 判断hasKey是否为null 否则判断是否为true
        if (hasKey != null && hasKey) {
            return redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    @Override
    public Boolean setToken(String id) {
        try {
            redisTemplate.opsForValue().set(id, null, Integer.parseInt(ttl), TimeUnit.DAYS);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    @Override
    public Long getTokenTtl(String token) {
        // 判断键值是否为null
        if (token == null) {
            return null;
        }
        Boolean hasKey = redisTemplate.hasKey(token);
        // 判断hasKey是否为null 否则判断是否为true
        if (hasKey != null && hasKey) {
            return redisTemplate.getExpire(token, TimeUnit.MILLISECONDS);
        }
        return null;
    }

    @Override
    public Boolean updateTokenTtl(String token, Long newTtl) {
        // 判断参数是否为null
        if (token == null || newTtl == null) {
            return false;
        }
        // 判断newTtl是否合法
        if (newTtl <= 0) {
            return false;
        }
        Boolean hasKey = redisTemplate.hasKey(token);
        // 判断hasKey是否为null 否则判断是否为true
        if (hasKey != null && hasKey) {
            // 因为我们只关心key及其ttl但value不能为null 因此此次设置
            redisTemplate.opsForValue().set(token, EMPTY_VALUE, newTtl, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

}

package mangoleeredis.service.impl;

import mangoleeredis.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void set(String id, String value) {
        redisTemplate.opsForValue().set(id, value);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key).toString();
    }
}

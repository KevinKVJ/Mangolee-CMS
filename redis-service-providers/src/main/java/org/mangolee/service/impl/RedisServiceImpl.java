package org.mangolee.service.impl;

import org.mangolee.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public final static Long DEFAULT_TTL = 24*60*60*1000L;

    public final static String DEFAULT_VALUE = "";

    //存值
    @Override
    public void set(String key, Object value) {
        if (key != null && value != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    //取值
    @Override
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null || !hasKey) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    // 存token 默认一天
    @Override
    public Boolean setToken(String token) {
        if (token == null) {
            return false;
        }
        redisTemplate.opsForValue().set(token, DEFAULT_VALUE, DEFAULT_TTL, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public Long getKeyTtl(String key) {
        if (key == null) {
            return null;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null || !hasKey) {
            return null;
        }
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean updateKeyTtl(String key, Long newTtl) {
        if (key == null || newTtl == null || newTtl <= 0) {
            return false;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null || !hasKey) {
            return false;
        }
        redisTemplate.opsForValue().set(key, DEFAULT_VALUE, newTtl, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public Boolean delete(String key) {
        if (key == null) {
            return false;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && hasKey) {
            return redisTemplate.delete(key);
        }
        return false;
    }

}

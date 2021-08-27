package org.mangolee.service.impl;

import org.mangolee.entity.Result;
import org.mangolee.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("unchecked")
public class RedisServiceImpl implements RedisService {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<Void> setValueAsString(String key, String value) {
        if (key == null || value == null) {
            return Result.BAD_REQUEST;
        }
        stringRedisTemplate.opsForValue().set(key, value);
        return Result.success();
    }

    @Override
    public Result<Void> set(String key, Object value) {
        if (key == null || value == null) {
            return Result.BAD_REQUEST;
        }
        if (value instanceof String) {
            stringRedisTemplate.opsForValue().set(key, (String) value);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
        return Result.success();
    }

    @Override
    public Result<String> getValueAsString(String key) {
        if (key == null) {
            return Result.BAD_REQUEST;
        }
        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.BAD_REQUEST;
        }
        if (!hasKey) {
            return Result.success();
        }
        String value = stringRedisTemplate.opsForValue().get(key);
        return Result.success(value);
    }

    @Override
    public Result<Object> get(String key) {
        if (key == null) {
            return Result.BAD_REQUEST;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.BAD_REQUEST;
        }
        if (!hasKey) {
            return Result.success();
        }
        Object object = redisTemplate.opsForValue().get(key);
        if (object instanceof String) {
            String value = stringRedisTemplate.opsForValue().get(key);
            return Result.success(value);
        }
        return Result.success(object);
    }

    @Override
    public Result<String> setToken(String token) {
        if (token == null) {
            return Result.BAD_REQUEST;
        }
        redisTemplate.opsForValue().set(token, DEFAULT_VALUE, DEFAULT_TTL, TimeUnit.MILLISECONDS);
        return Result.success(token);
    }

    @Override
    public Result<Long> getKeyTtl(String key) {
        if (key == null) {
            return Result.BAD_REQUEST;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.BAD_REQUEST;
        }
        // 返回值和redis一致 返回-1说明key未设置ttl 返回-2说明key不存在
        Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        return Result.success(ttl);
    }

    @Override
    public Result<Long> updateKeyTtl(String key, Long newTtl) {
        if (key == null || newTtl == null || newTtl <= 0) {
            return Result.BAD_REQUEST;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null || !hasKey) {
            return Result.BAD_REQUEST;
        }
        Boolean expire = redisTemplate.expire(key, newTtl, TimeUnit.MILLISECONDS);
        if (expire == null || !expire) {
            return Result.BAD_REQUEST;
        }
        return Result.success(newTtl);
    }

    @Override
    public Result<Void> delete(String key) {
        if (key == null) {
            return Result.BAD_REQUEST;
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null || !hasKey) {
            return Result.BAD_REQUEST;
        }
        Boolean delete = redisTemplate.delete(key);
        if (delete == null || !delete) {
            return Result.BAD_REQUEST;
        }
        return Result.success();
    }

    @Override
    public Result<List<String>> getKeys() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null) {
            return Result.BAD_REQUEST;
        }
        List<String> keysList = new ArrayList<>(keys);
        return Result.success(keysList);
    }

}

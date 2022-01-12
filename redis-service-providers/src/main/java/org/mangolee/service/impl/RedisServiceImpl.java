package org.mangolee.service.impl;

import com.alibaba.fastjson.JSON;
import org.mangolee.entity.Result;
import org.mangolee.entity.UserInfo;
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
    public Result<Void> setValue(String key, String value) {
        if (key == null || value == null) {
            return Result.error(400,"Key or value should not be null");
        }
        stringRedisTemplate.opsForValue().set(key, value);
        return Result.success();
    }

    @Override
    public Result<String> getValue(String key) {
        if (key == null) {
            return Result.error(400,"Key should not be null");
        }
        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.error(500,"StringRedisTemplate error");
        }
        if (!hasKey) {
            return Result.error(400,"Key does not exist");
        }
        String value = stringRedisTemplate.opsForValue().get(key);
        return Result.success(value);
    }



    @Override
    public Result<Long> getKeyTtl(String key) {
        if (key == null) {
            return Result.error(400,"Key should not be null");
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.error(500,"StringRedisTemplate error");
        }
        // 返回值和redis一致 返回-1说明key未设置ttl 返回-2说明key不存在
        Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        if(ttl >= 0)
            return Result.success(ttl);
        else if(ttl == -1)
            return Result.error(400,"The Key does not have ttl");
        else
            return Result.error(400,"The key does not exist");
    }

    @Override
    public Result<Long> updateKeyTtl(String key, Long newTtl) {
        if (key == null || newTtl == null || newTtl <= 0) {
            return Result.error(400,"Key or ttl should not be null or negative");
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.error(500,"StringRedisTemplate error");
        }
        if (!hasKey)
        {
            return Result.error(400,"The key does not exist");
        }
        Boolean expire = redisTemplate.expire(key, newTtl, TimeUnit.MILLISECONDS);
        if (expire == null || !expire) {
            return Result.error(500,"RedisTemplate expire error");
        }
        return Result.success(newTtl);
    }

    @Override
    public Result<Void> delete(String key) {
        if (key == null) {
            return Result.error(400,"Key should not be null");
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null ) {
            return Result.error(500,"StringRedisTemplate error");
        }
        if (!hasKey)
        {
            return Result.error(400,"The key does not exist");
        }
        Boolean delete = redisTemplate.delete(key);
        if (delete == null || !delete) {
            return Result.error(500,"RedisTemplate delete error");
        }
        return Result.success();
    }

    @Override
    public Result<List<String>> getKeys() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null) {
            return Result.error(500,"RedisTemplate error");
        }
        List<String> keysList = new ArrayList<>(keys);
        return Result.success(keysList);
    }

    @Override
    public Result<UserInfo> verify(String key) {
        Result<String> value = getValue(key);
        if(value.getCode() != 200)
            return Result.error(value.getCode(), value.getMessage());
        UserInfo userInfo = JSON.parseObject(value.getData(), UserInfo.class);
        updateKeyTtl(key,DEFAULT_TTL);
        return  Result.success(userInfo);
    }

}

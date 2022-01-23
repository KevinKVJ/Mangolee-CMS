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
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("unchecked")
public class RedisServiceImpl implements RedisService {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate           stringRedisTemplate;

    @Override
    public Result<Void> setValue(String key, String value) {
        if (key == null) {
            return Result.error(400, "key不能为null");
        }
        if (value == null) {
            return Result.error(400, "value不能为null");
        }
        stringRedisTemplate.opsForValue().set(key, value);
        return Result.success();
    }

    @Override
    public Result<String> getValue(String key) {
        if (key == null) {
            return Result.error(400, "key不能为null");
        }
        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.error(500, "StringRedisTemplate出错");
        }
        if (!hasKey) {
            return Result.error(400, "key不存在");
        }
        String value = stringRedisTemplate.opsForValue().get(key);
        return Result.success(value);
    }


    @Override
    public Result<Long> getKeyTtl(String key) {
        if (key == null) {
            return Result.error(400, "key不能为null");
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.error(500, "StringRedisTemplate出错");
        }
        // 返回值和redis一致 返回-1说明key未设置ttl 返回-2说明key不存在
        Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        if (ttl == null) {
            return Result.error(400, "返回ttl为空");
        } else if (ttl >= 0) {
            return Result.success(ttl);
        } else if (ttl == -1) {
            return Result.error(400, "key不包含ttl");
        } else {
            return Result.error(400, "key不存在");
        }
    }

    @Override
    public Result<Long> updateKeyTtl(String key, Long ttl) {
        if (key == null) {
            return Result.error(400, "key不能为null");
        }
        if (ttl == null) {
            return Result.error(400, "ttl不能为null");
        }
        if (ttl <= 0) {
            return Result.error(400, "ttl不能为负");
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.error(500, "StringRedisTemplate出错");
        }
        if (!hasKey) {
            return Result.error(400, "key不存在");
        }
        Boolean expire = redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
        if (expire == null) {
            return Result.error(500, "调用redisTemplate.expire()出错");
        }
        if (!expire) {
            return Result.error(500, "key过期设置失败");
        }
        return Result.success(ttl);
    }

    @Override
    public Result<Void> delete(String key) {
        if (key == null) {
            return Result.error(400, "key不能为null");
        }
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null) {
            return Result.error(500, "StringRedisTemplate出错");
        }
        if (!hasKey) {
            return Result.error(400, "key不存在");
        }
        Boolean delete = redisTemplate.delete(key);
        if (delete == null) {
            return Result.error(500, "调用redisTemplate.delete()出错");
        }
        if (!delete) {
            return Result.error(500, "RedisTemplate删除失败");
        }
        return Result.success();
    }

    @Override
    public Result<List<String>> getKeys() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null) {
            return Result.error(500, "redisTemplate.keys()调用出错");
        }
        return Result.success(new ArrayList<>(keys));
    }

    @Override
    public Result<UserInfo> verify(String key) {
        if (key == null) {
            return Result.error(400, "key不能为null");
        }
        Result<String> value = getValue(key);
        if (value.getCode() != 200) {
            return Result.error(value.getCode(), value.getMessage());
        }
        UserInfo userInfo = JSON.parseObject(value.getData(), UserInfo.class);
        updateKeyTtl(key, DEFAULT_TTL);
        return Result.success(userInfo);
    }

}

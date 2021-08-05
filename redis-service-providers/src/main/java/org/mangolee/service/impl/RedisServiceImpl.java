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

    private final long ttl =1;


    //存值，默认一天
    @Override
    public void set(String key, Object value) {
        if(key==null){
            return;
        }
        redisTemplate.opsForValue().set(key,value,ttl, TimeUnit.DAYS);
    }
    //取值
    @Override
    public Object get(String key) {
        if(key!=null&&Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            return redisTemplate.opsForValue().get(key);
        }else{
            return null;
        }
    }

    @Override
    public Boolean setToken(String token) {
        if(token==null){
            return null;
        }
        try{
            redisTemplate.opsForValue().set(token,"",ttl,TimeUnit.DAYS);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Long getTokenTTL(String token) {
        if(token==null){
            return null;
        }
        try{
            if(Boolean.TRUE.equals(redisTemplate.hasKey(token))){
                return redisTemplate.getExpire(token,TimeUnit.MILLISECONDS);
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Boolean updateTokenTTL(String token, Long newTtl) {
        if(token==null){
            return false;
        }
        try{
            if(newTtl>0&&Boolean.TRUE.equals(redisTemplate.hasKey(token))){
                redisTemplate.opsForValue().set(token,"",newTtl,TimeUnit.MILLISECONDS);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean remove(String key) {
        if(key==null){
            return false;
        }
        try{
            if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){
                return redisTemplate.delete(key);
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

}

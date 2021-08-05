package org.mangolee.service;

public interface RedisService {

    void set(String key, Object value);

    Object get(String key);

    Boolean setToken(String token);

    Long getKeyTtl(String key);

    Boolean updateKeyTtl(String key, Long newTtl);

    Boolean delete(String key);

}

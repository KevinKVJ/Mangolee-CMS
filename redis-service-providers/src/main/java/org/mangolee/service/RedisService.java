package org.mangolee.service;

import com.sun.org.apache.xpath.internal.operations.Bool;

public interface RedisService {
    void set(String key, Object value);

    Object get(String key);

    Boolean setToken(String token);

    Long getTokenTTL(String token);

    Boolean updateTokenTTL(String token,Long newTtl);

    Boolean remove(String key);

}

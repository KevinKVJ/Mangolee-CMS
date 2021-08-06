package org.mangolee.service;

//TODO 把 RedisService 和 RedisFeignService 合并统一接口（使用result方式返回），并在provider中做好实现
public interface RedisService {
    // 存值 值类型为String
    void setValueAsString(String key, String value);
    // 存值
    void set(String key, Object value);
    // 取值 值类型为String
    String getValueAsString(String key);
    // 取值
    Object get(String key);
    // 存token 默认一天
    Boolean setToken(String token);
    // 获取键的ttl
    Long getKeyTtl(String key);
    // 更新键的ttl
    Boolean updateKeyTtl(String key, Long newTtl);
    // 删除键
    Boolean delete(String key);
}

package mangolee.service;

public interface RedisService {
    void set(String id, Object value);

    Object get(String id);

    Boolean setToken(String id);

    Long getTokenTtl(String token);

    Boolean updateTokenTtl(String token,Long newTtl);

}
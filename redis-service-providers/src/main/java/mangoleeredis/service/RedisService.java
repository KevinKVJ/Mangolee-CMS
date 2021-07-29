package mangoleeredis.service;

public interface RedisService {
    void set(String id, String value);

    String get(String id);
}

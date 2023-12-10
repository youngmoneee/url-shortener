package app.youngmon.surl.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class CacheRepositoryImpl implements CacheRepository {
    StringRedisTemplate redis;

    @Autowired
    public CacheRepositoryImpl(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public String   set(String key, String value) {
        this.redis.opsForValue().set(key, value);
        return value;
    }

    @Override
    public String   set(String key, String value, long expire) {
        this.redis.opsForValue().set(key, value, Duration.ofSeconds(expire));
        return value;
    }

    @Override
    public String   get(String key) {
        return redis.opsForValue().get(key);
    }
}

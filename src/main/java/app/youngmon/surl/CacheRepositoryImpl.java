package app.youngmon.surl;

import app.youngmon.surl.interfaces.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CacheRepositoryImpl implements CacheRepository {
    StringRedisTemplate redis;

    @Autowired
    public CacheRepositoryImpl(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public String   set(String key, String value) {
        redis.opsForValue().set(key, value);
        return value;
    }

    @Override
    public String   get(String key) {
        return redis.opsForValue().get(key);
    }
}

package app.youngmon.surl;

import app.youngmon.surl.interfaces.HashCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HashCacheImpl implements HashCache {
    StringRedisTemplate redis;

    @Autowired
    public HashCacheImpl(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public String   createKV(String key, String value) {
        redis.opsForValue().set(key, value);
        return key;
    }

    @Override
    public String   findUrlByKey(String key) {
        return redis.opsForValue().get(key);
    }
}

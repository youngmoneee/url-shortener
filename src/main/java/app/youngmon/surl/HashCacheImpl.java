package app.youngmon.surl;

import app.youngmon.surl.interfaces.HashCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class HashCacheImpl implements HashCache {
    @Autowired
    RedisTemplate<String, String>   redis;

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

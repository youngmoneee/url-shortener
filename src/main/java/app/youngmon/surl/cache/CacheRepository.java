package app.youngmon.surl.cache;

public interface CacheRepository {
    String   get(String key);
    String   set(String key, String value);
    String   set(String key, String value, long expire);
}

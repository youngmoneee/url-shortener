package app.youngmon.surl.interfaces;

public interface CacheRepository {
    String   set(String key, String value);
    String   get(String key);
}

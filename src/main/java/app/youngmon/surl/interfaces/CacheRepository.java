package app.youngmon.surl.interfaces;

public interface HashCache {
    String   set(String key, String value);
    String   get(String key);
}

package app.youngmon.surl.interfaces;

public interface HashCache {
    String   createKV(String key, String value);
    String   findUrlByKey(String key);
}

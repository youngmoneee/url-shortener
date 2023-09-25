package app.youngmon.surl;

public interface HashCache {
    String   createKV(String key, String value);
    String   findUrlByKey(String key);
}

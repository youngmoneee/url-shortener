package app.youngmon.surl.interfaces;

public interface HashService {
    char[]   getHashBaseArr();
    int      getHashBaseLength();
    String   getDomain();
    String   getShortUrl(String longUrl);
    String   getLongUrl(String shortUrl);
}

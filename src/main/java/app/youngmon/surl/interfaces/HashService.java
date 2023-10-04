package app.youngmon.surl.interfaces;

public interface HashService {
    char[]   getHashBaseArr();
    int      getHashBaseLength();
    String   getShortUrl(String longUrl);
    String   getLongUrl(String shortUrl);
}

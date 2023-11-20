package app.youngmon.surl.interfaces;

public interface HashService {
    char[]   getHashBaseArr();
    String   getShortUrl(String longUrl);
    String   getLongUrl(String shortUrl);
}

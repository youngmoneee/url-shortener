package app.youngmon.surl.interfaces;

public interface HashService {
    String   getShortUrl(String longUrl);
    String   getLongUrl(String shortUrl);
}

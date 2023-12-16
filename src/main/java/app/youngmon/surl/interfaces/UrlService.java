package app.youngmon.surl.interfaces;

public interface UrlService {

  String getShortUrl(String longUrl);

  String getLongUrl(String shortUrl);
}

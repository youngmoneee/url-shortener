package app.youngmon.surl.interfaces;

public interface UrlRepository {
	String  getLongUrl(String shortUrl);
	String  getShortUrl(String longUrl);
}
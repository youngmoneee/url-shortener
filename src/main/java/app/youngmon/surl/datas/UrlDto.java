package app.youngmon.surl.datas;

import lombok.Data;

@Data
public class UrlDto {
  public UrlDto() {}

  public UrlDto(UrlEntity urlEntity) {
    this.longUrl = urlEntity.getLongUrl();
    this.shortUrl = urlEntity.getShortUrl();
  }

  private String longUrl;
  private String shortUrl;
}

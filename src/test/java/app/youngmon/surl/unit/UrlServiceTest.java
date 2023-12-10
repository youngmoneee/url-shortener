package app.youngmon.surl.unit;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.cache.CacheRepository;
import app.youngmon.surl.interfaces.DbRepository;
import app.youngmon.surl.interfaces.Generator;
import app.youngmon.surl.interfaces.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UrlService Test")
@SpringBootTest
@Transactional
public class UrlServiceTest {
    @MockBean
    private DbRepository db;
    @MockBean
    private CacheRepository cache;
    @MockBean
    private Generator generator;
    @Autowired
    private UrlService service;

    @Test
    @DisplayName("Get Short Url Test")
    void getShortUrlTest() {
        //  given
        String  longUrl = "longUrl";
        String  shortUrl = "shortUrl";
        UrlEntity expectedUrlEntity = new UrlEntity(longUrl);
        expectedUrlEntity.setShortUrl(shortUrl);

        when(db.findUrlEntityByLongUrl(longUrl)).thenReturn(Optional.of(expectedUrlEntity));

        //  when
        String  res = service.getShortUrl(longUrl);

        //  then
        assertThat(res).isEqualTo(shortUrl);
    }

    @Test
    @DisplayName("Get Long Url Test::Cached")
    void getCachedLongUrlTest() {
        //  given
        String  shortUrl = "shortUrl";
        String  cacheHit = "cache-hit";
        String  cacheMiss = "cache-miss";
        UrlEntity urlEntity = new UrlEntity(cacheMiss);
        urlEntity.setId(1L);

        when(cache.get(shortUrl)).thenReturn(cacheHit);

        when(generator.decode(shortUrl)).thenReturn(1L);
        when(db.findById(1L)).thenReturn(Optional.of(urlEntity));

        //  when
        String  res = service.getLongUrl(shortUrl);

        //  then
        assertThat(res).isEqualTo(cacheHit);
    }

    @Test
    @DisplayName("Get Long Url Test::No Cached")
    void getLongUrlTest() {
        //  given
        String  shortUrl = "shortUrl";
        String  cacheHit = "cache-hit";
        String  cacheMiss = "cache-miss";
        UrlEntity urlEntity = new UrlEntity(cacheMiss);
        urlEntity.setId(1L);

        when(cache.get(shortUrl)).thenReturn(null);

        when(generator.decode(shortUrl)).thenReturn(1L);
        when(db.findById(1L)).thenReturn(Optional.of(urlEntity));

        //  when
        String  res = service.getLongUrl(shortUrl);

        //  then
        assertThat(res).isEqualTo(cacheMiss);
    }
}

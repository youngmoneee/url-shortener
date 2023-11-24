package app.youngmon.surl.integrate;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.interfaces.CacheRepository;
import app.youngmon.surl.interfaces.DbRepository;
import app.youngmon.surl.interfaces.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Integration Test")
@SpringBootTest
public class UrlIntegrateTest {

    @Autowired
    private UrlService          service;

    @MockBean
    private DbRepository        repository;

    @MockBean
    private CacheRepository     cache;

    @Test
    @DisplayName("Get Short URL and Validate")
    public void getShortUrlAndValidate() {
        // Given
        String longUrl = "http://example.com";
        UrlEntity urlEntity = new UrlEntity(longUrl);
        urlEntity.setId(1L);
        urlEntity.setShortUrl("1");

        // Mocking repository behavior
        when(repository.findUrlEntityByLongUrl(longUrl)).thenReturn(Optional.of(urlEntity));
        when(repository.save(new UrlEntity(longUrl))).thenReturn(urlEntity);

        // When
        String shortUrl = service.getShortUrl(longUrl);

        // Then
        assertThat(shortUrl).isNotNull().isEqualTo("1");
    }

    @Test
    @DisplayName("Get Long URL and Validate")
    public void getLongUrlAndValidate() {
        // Given
        String shortUrl = "1";
        String longUrl = "http://example.com";
        UrlEntity urlEntity = new UrlEntity(longUrl);
        urlEntity.setId(1L);
        urlEntity.setShortUrl(shortUrl);

        // Mocking repository behavior
        when(cache.get(shortUrl)).thenReturn(null);
        when(repository.findById(1L)).thenReturn(Optional.of(urlEntity));

        // When
        String res = service.getLongUrl(shortUrl);

        // Then
        assertThat(res).isNotNull().isEqualTo(longUrl);
    }
}

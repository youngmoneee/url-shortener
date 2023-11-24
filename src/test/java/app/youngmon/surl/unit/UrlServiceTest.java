package app.youngmon.surl.unit;

import app.youngmon.surl.interfaces.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashService Test")
public class UrlServiceTest {
    @Mock
    UrlService service;

    @Test
    @DisplayName("Get Short Url Test")
    void getShortUrlTest() {
        //  given
        String  longUrl = "longUrl";
        String  expectedUrl = "shortUrl";

        when(service.getShortUrl(longUrl)).thenReturn("shortUrl");

        //  when
        String  res = service.getShortUrl(longUrl);

        //  then
        assertThat(res).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Get Long Url Test")
    void getLongUrlTest() {
        //  given
        String  shortUrl = "shortUrl";
        String  expectedUrl = "longUrl";

        when(service.getLongUrl(shortUrl)).thenReturn("longUrl");

        //  when
        String  res = service.getLongUrl(shortUrl);

        //  then
        assertThat(res).isEqualTo(expectedUrl);
    }
}

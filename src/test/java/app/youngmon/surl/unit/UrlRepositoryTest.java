package app.youngmon.surl.unit;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.interfaces.DbRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashRepository Test")
public class UrlRepositoryTest {
    @Mock
    DbRepository mockHashRepository;

    @Test
    @DisplayName("createUrl not exist Test")
    void saveTest() {
        //  given
        String  longUrl = "url";
        String  shortUrl = "sUrl";
        Long    id = 1L;
        UrlEntity savedUrl = new UrlEntity();

        savedUrl.setLongUrl(longUrl);
        savedUrl.setShortUrl(shortUrl);
        savedUrl.setId(id);

        when(mockHashRepository.save(savedUrl)).thenReturn(savedUrl);

        //  when
        UrlEntity   res = mockHashRepository.save(savedUrl);

        //  then
        assertThat(res).isEqualTo(savedUrl);
    }

    @Test
    @DisplayName("findByUrl exist Test")
    void findByUrlExistTest() {
        //  given
        String      longUrl = "long";
        UrlEntity   expectedEntity = new UrlEntity();

        expectedEntity.setId(1L);
        expectedEntity.setLongUrl("long");
        expectedEntity.setShortUrl("1");

        when(mockHashRepository.findUrlEntityByLongUrl(longUrl)).thenReturn(Optional.of(new UrlEntity(longUrl)));

        //  when
        Optional<UrlEntity> res = mockHashRepository.findUrlEntityByLongUrl(longUrl);

        //  then
        assertThat(res.map(UrlEntity::getLongUrl)).isEqualTo(Optional.of(expectedEntity.getLongUrl()));
    }

    @Test
    @DisplayName("findById Test")
    void findByIdTest() {
        //  given
        Long    id = 1L;
        UrlEntity   expectedEntity = new UrlEntity();
        expectedEntity.setId(id);

        when(mockHashRepository.findById(id)).thenReturn(Optional.of(expectedEntity));

        //  when
        Optional<UrlEntity>  res = mockHashRepository.findById(id);

        //  then
        assertThat(res.map(UrlEntity::getId)).isEqualTo(Optional.of(id));
    }
}

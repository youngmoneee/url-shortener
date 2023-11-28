package app.youngmon.surl.unit;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.interfaces.DbRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("DB Repository Test")
@Transactional
@SpringBootTest
public class UrlRepositoryTest {
    @Autowired
    private DbRepository repository;

    @Test
    @DisplayName("존재하지 않는 URL 생성")
    public void saveTest() {
        //  given
        UrlEntity   urlEntity = new UrlEntity();
        String      longUrl = "url";
        String      shortUrl = "sUrl";

        urlEntity.setLongUrl(longUrl);
        urlEntity.setShortUrl(shortUrl);

        //  when
        UrlEntity   res = repository.save(urlEntity);

        //  then
        assertThat(res).isEqualTo(urlEntity);
    }

    @Test
    @DisplayName("findByUrl - 존재")
    public void findByUrlExistTest() {
        //  given
        String      longUrl = "https://long.url";
        String      shortUrl = "https://short.url";
        UrlEntity   urlEntity = new UrlEntity();

        urlEntity.setLongUrl(longUrl);
        urlEntity.setShortUrl(shortUrl);
        this.repository.save(urlEntity);

        //  when
        Optional<UrlEntity> res = this.repository.findUrlEntityByLongUrl(longUrl);

        //  then
        assertThat(res.isPresent()).isTrue();
        assertThat(res.get()).isEqualTo(urlEntity);
    }

    @Test
    @DisplayName("findById - 존재")
    public void findByIdTest() {
        //  given
        UrlEntity   urlEntity = new UrlEntity();
        Long        id = this.repository.save(urlEntity).getId();

        //  when
        Optional<UrlEntity>  res = this.repository.findById(id);

        //  then
        assertThat(res.isPresent()).isTrue();
        assertThat(res.get()).isEqualTo(urlEntity);
    }

    @Test
    @DisplayName("findById - 존재 X")
    public void findByIdNotExist() {
        //  given
        Long        id = 99999L;

        //  when
        Optional<UrlEntity> res = this.repository.findById(id);

        //  then
        assertThat(res.isPresent()).isFalse();
        assertThatThrownBy(res::get).isInstanceOf(NoSuchElementException.class);
    }
}

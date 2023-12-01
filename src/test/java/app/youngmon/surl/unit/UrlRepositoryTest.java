package app.youngmon.surl.unit;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.interfaces.DbRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void clear() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("DI 테스트")
    public void InitTest() {
        assertThat(repository).isInstanceOf(DbRepository.class);
    }

    @Test
    @DisplayName("새로운 short Url 생성")
    public void saveTest() {
        //  given
        UrlEntity   urlEntity = new UrlEntity();
        String      longUrl = "https://long.url";
        String      shortUrl = "https://short.url";

        urlEntity.setLongUrl(longUrl);
        urlEntity.setShortUrl(shortUrl);

        //  when
        UrlEntity   res = repository.save(urlEntity);

        //  then
        assertThat(res).isEqualTo(urlEntity);
    }

    @Test
    @DisplayName("기존 LongUrl을 재생성")
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
        assertThat(res).isPresent();
        assertThat(res.get()).isEqualTo(urlEntity);
    }

    @Test
    @DisplayName("존재 하는 값을 findById로 조회")
    public void findByIdTest() {
        //  given
        UrlEntity   urlEntity = repository.save(new UrlEntity());
        Long        id = urlEntity.getId();

        //  when
        Optional<UrlEntity>  res = this.repository.findById(id);

        //  then
        assertThat(res).isPresent();
        assertThat(res.get()).isEqualTo(urlEntity);
    }

    @Test
    @DisplayName("존재하지 않는 값을 findById로 조회")
    public void findByIdNotExist() {
        //  given
        Long        id = 99999L;

        //  when
        Optional<UrlEntity> res = this.repository.findById(id);

        //  then
        assertThat(res).isNotPresent();
        assertThatThrownBy(res::get).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("id가 음수일 때 findById를 통해 조회")
    public void findByIdNegative() {
        //  given
        Long        id = -1L;

        //  when
        Optional<UrlEntity> res = this.repository.findById(id);

        //  then
        assertThat(res).isNotPresent();
        assertThatThrownBy(res::get).isInstanceOf(NoSuchElementException.class);
    }
}

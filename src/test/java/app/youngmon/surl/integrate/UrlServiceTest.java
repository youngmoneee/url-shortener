package app.youngmon.surl.integrate;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.interfaces.UrlService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Integration UrlService Test")
@SpringBootTest
@Testcontainers
@Transactional
public class UrlServiceTest {
    @Container
    private static final GenericContainer redisContainer = new GenericContainer<>("redis:latest").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", () -> redisContainer.getHost());
        registry.add("spring.data.redis.port", () -> redisContainer.getFirstMappedPort());
    }

    @Autowired
    private UrlService          service;
    @Autowired
    private RedisTemplate<?,?>  redisTemplate;

    @BeforeEach
    void clearCache() {
        redisTemplate.getConnectionFactory().getConnection().commands().flushAll();
    }

    @Test
    @DisplayName("Get Short Url Test")
    public void getShortUrl() {
        // Given
        String longUrl = "http://example.com";

        // When
        String  created = service.getShortUrl(longUrl);
        String  res = service.getLongUrl(created);

        // Then
        assertThat(created).isNotNull();
        assertThat(res).isEqualTo(longUrl);
    }

    @Test
    @DisplayName("Get Long Url Test::Not Cached")
    public void cacheMissTest() {
        // Given
        String longUrl = "http://not-cached.com";
        String shortUrl = service.getShortUrl(longUrl);

        // When
        String res = service.getLongUrl(shortUrl);

        // Then
        assertThat(res).isNotNull().isEqualTo(longUrl);
    }

    //  TODO: Transaction method should be called by extern obj
    /*
    @Test
    @DisplayName("Concurrency Test")
    public void concurrencyCreateTest() throws InterruptedException, ExecutionException {
        //  given
        String  longUrl = "https://www.same.url";
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Future<String>>    jobs = new ArrayList<>(10);
        List<String>            res = new ArrayList<>(10);
        //  when
        for (int i = 0; i < 10; i++)
            jobs.add(executor.submit(() -> service.getShortUrl(longUrl)));
        for (Future<String> job : jobs) res.add(job.get());
        executor.shutdown();
        //  then
        boolean isSame = res.stream().allMatch(url -> url.equals(res.get(0)));
        //  True가 되어야함
        //assertThat(isSame).isFalse();
    }
    */
}
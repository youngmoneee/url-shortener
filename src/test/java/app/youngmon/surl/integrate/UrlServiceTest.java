package app.youngmon.surl.integrate;

import static org.assertj.core.api.Assertions.assertThat;

import app.youngmon.surl.interfaces.UrlService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DisplayName("Integration UrlService Test")
@SpringBootTest
@Testcontainers
@Transactional
public class UrlServiceTest {

  @Container
  private static final GenericContainer redisContainer = new GenericContainer<>(
      "redis:latest").withExposedPorts(6379);

  @DynamicPropertySource
  static void redisProperty(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", () -> redisContainer.getHost());
    registry.add("spring.data.redis.port", () -> redisContainer.getFirstMappedPort());
  }

  @Autowired
  private UrlService service;
  @Autowired
  private StringRedisTemplate redisTemplate;

  @BeforeEach
  void clearCache() {
    redisTemplate.getConnectionFactory().getConnection().commands().flushAll();
  }

  @Test
  @DisplayName("Init Test")
  public void initTest() {
    assertThat(this.service).isInstanceOf(UrlService.class);
    assertThat(AopUtils.isAopProxy(this.service)).isTrue();
  }

  @Test
  @DisplayName("Get Short Url Test")
  public void getShortUrl() {
    // Given
    String longUrl = "http://example.com";

    // When
    String created = service.getShortUrl(longUrl);
    String res = service.getLongUrl(created);

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

  @Test
  @DisplayName("Concurrency Test")
  public void concurrencyCreateTest() throws InterruptedException, ExecutionException {
    //  given
    String longUrl = "https://www.same.url";
    ExecutorService executor = Executors.newFixedThreadPool(100);

    List<Future<String>> jobs = new ArrayList<>(100);
    Set<String> res = Collections.synchronizedSet(new HashSet<>());

    //  when
    for (int i = 0; i < 100; i++) {
      jobs.add(executor.submit(() -> service.getShortUrl(longUrl)));
    }
    for (Future<String> job : jobs) {
      res.add(job.get());
    }
    executor.shutdown();

    //  then
    assertThat(res.size()).isEqualTo(1);
  }
}

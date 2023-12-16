package app.youngmon.surl.unit;

import static org.assertj.core.api.Assertions.assertThat;

import app.youngmon.surl.cache.CacheRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@DisplayName("Url Cache Test")
@SpringBootTest
@Testcontainers
public class CacheTest {

  @Container
  private static final GenericContainer<?> redis =
      new GenericContainer<>("redis:latest").withExposedPorts(6379);
  @Autowired
  private CacheRepository cache;

  @DynamicPropertySource
  static void redisProperty(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", redis::getFirstMappedPort);
  }

  @Test
  @DisplayName("Cache Set Test")
  void setTest() {
    //  given
    String key = "key";
    String value = "value";

    //  when
    String res = cache.set(key, value);

    //  then
    assertThat(res).isEqualTo(value);
  }

  @Test
  @DisplayName("Cache Hit Test")
  void cacheHitTest() {
    //  given
    String longUrl = "https://ymon.io";
    String expectedUrl = "expectedUrl";

    cache.set(longUrl, expectedUrl);

    //  when
    String res = cache.get(longUrl);

    //  then
    assertThat(res).isEqualTo(expectedUrl);
  }

  @Test
  @DisplayName("Cache Miss Test")
  void cacheMissTest() {
    //  given
    String longUrl = "https://no-url.com";

    //  when
    String res = cache.get(longUrl);

    //  then
    assertThat(res).isEqualTo(null);
  }
}

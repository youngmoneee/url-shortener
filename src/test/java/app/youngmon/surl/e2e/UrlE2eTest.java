package app.youngmon.surl.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlE2eTest {

  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private RedisTemplate<?, ?> redisTemplate;

  @Container
  private static final GenericContainer redisContainer = new GenericContainer<>(
      "redis:latest").withExposedPorts(6379);

  @DynamicPropertySource
  static void redisProperty(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", () -> redisContainer.getHost());
    registry.add("spring.data.redis.port", () -> redisContainer.getFirstMappedPort());
  }

  @BeforeEach
  void clearCache() {
    redisTemplate.getConnectionFactory().getConnection().commands().flushAll();
  }

  @Test
  @DisplayName("API Long to Short Transfer Test")
  public void getShortUrl() {
    //  given
    String longUrl = "https://" + "getShortUrl" + ".com";

    //  when
    ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/v1", longUrl,
        String.class);

    //  then
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("Http Format::Should Be 200 OK")
  public void httpTest() {
    //  given
    String url = "https://www.naver.com";

    //  when
    ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/v1", url,
        String.class);

    //  then
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("Http Format::Should Be 400 Bad Request")
  public void wrongHttpTest() {
    //  given
    String wrongUrl = "https:/badurl.com";

    //  when
    ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/v1", wrongUrl,
        String.class);

    //  then
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("No Payload::Should Be 400 Bad Reqeust")
  public void noPayloadTest() {
    //  given
    String noUrl = "";

    //  when
    ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/v1", noUrl,
        String.class);

    //  then
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Get Origin Url")
  public void getOriginUrl() {
    //  given
    String longUrl = "https://" + "getOriginUrl" + ".com";
    String shortUrl = restTemplate.postForEntity("/api/v1", longUrl, String.class).getBody();

    //  when
    ResponseEntity<String> getRes = restTemplate.getForEntity("/api/v1/" + shortUrl, String.class);

    //  then
    assertThat(getRes.getBody()).isEqualTo(longUrl);
  }

  @Test
  @DisplayName("Idempotent Test")
  public void idempotentTest() {
    //  given
    int cnt = 5;

    String originUrl = "https://idempotent.test";
    String createdShortUrl;
    String[] res = new String[cnt];

    //  when
    createdShortUrl = restTemplate.postForEntity("/api/v1", originUrl, String.class).getBody();
    System.err.println(
        restTemplate.getForEntity("/api/v1/" + createdShortUrl, String.class).getBody());

    for (int i = 0; i < cnt; i++) {
      res[i] = restTemplate.getForEntity("/api/v1/" + createdShortUrl, String.class).getBody();
    }

    //  then
    boolean allEqual = Arrays.stream(res)
        .allMatch(url -> url.equals(res[0]));

    assertThat(allEqual).isTrue();
  }
}
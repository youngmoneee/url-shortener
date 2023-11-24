package app.youngmon.surl.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlE2eTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Container
	private static final GenericContainer redisContainer = new GenericContainer<>("redis:latest").withExposedPorts(6379);
	static {
		redisContainer.start();
	}

	@DynamicPropertySource
	static void redisProperty(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", () -> redisContainer.getHost());
		registry.add("spring.data.redis.port", () -> redisContainer.getFirstMappedPort());
	}

	@Test
	@DisplayName("API Long to Short Transfer Test")
	public void getShortUrl() {
		//  given
		String longUrl = "https://www.example.com";

		//  when
		ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/v1", longUrl, String.class);

		//  then
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Http Format::Should Be 200 OK")
	public void httpTest() {
		//  given
		String  url = "https://www.naver.com";

		//  when
		ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/v1", url, String.class);

		//  then
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Http Format::Should Be 400 Bad Request")
	public void wrongHttpTest() {
		//  given
		String url = "https:/www.naver.com";

		//  when
		ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/v1", url, String.class);

		//  then
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("No Payload::Should Be 400 Bad Reqeust")
	public void noPayloadTest() {
		//  given
		String url = "";

		//  when
		ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/v1", url, String.class);

		//  then
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("Get Origin Url")
	public void getOriginUrl() {
		//  given
		String shortUrl = "1";
		String longUrl = "https://www.example.com";

		//  when
		ResponseEntity<String> postRes = restTemplate.postForEntity("/api/v1", longUrl, String.class);
		ResponseEntity<String> getRes = restTemplate.getForEntity("/api/v1/" + shortUrl, String.class);

		//  then
		assertThat(postRes.getBody()).isEqualTo(shortUrl);
		assertThat(getRes.getBody()).isEqualTo(longUrl);
	}
}

package app.youngmon.surl.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HashTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void getShortUrlAndRedirect() {
		//  given
		String longUrl = "https://www.example.com";

		//  when
		ResponseEntity<String> createResponse = restTemplate.postForEntity("/", longUrl, String.class);

		//  then
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		String shortUrl = createResponse.getBody();

		//  when
		ResponseEntity<String> redirectResponse = restTemplate.getForEntity("/" + shortUrl, String.class);

		//  then
		assertThat(redirectResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(redirectResponse.getBody()).isEqualTo(longUrl);
	}

	@Test
	public void shouldReturn404() {
		//  given
		String wrongUrl = "nonExistent";

		//  when
		ResponseEntity<String> response = restTemplate.getForEntity("/" + wrongUrl, String.class);

		//  then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void noPathShoulRedirectDocs() {
		//  when
		ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);

		//  then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("DOCS");
	}

	@Test
	public void getDocs_ShouldReturnDocs() {
		//  when
		ResponseEntity<String> response = restTemplate.getForEntity("/docs", String.class);

		//  then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("DOCS");
	}
}

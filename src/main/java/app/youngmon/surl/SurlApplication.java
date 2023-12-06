package app.youngmon.surl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class SurlApplication {
	public static void main(String[] args) {
		SpringApplication.run(SurlApplication.class, args);
	}
}
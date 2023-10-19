package app.youngmon.surl;

import app.youngmon.surl.interfaces.HashCache;
import app.youngmon.surl.interfaces.HashJpaRepository;
import app.youngmon.surl.interfaces.HashService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashConfig {
    @Bean
    public HashCache hashCache() {
        return new HashCacheImpl();
    }

    @Bean
    public HashService hashService(HashJpaRepository hashRepository) {
        return new HashServiceImpl(hashCache(), hashRepository);
    }
}

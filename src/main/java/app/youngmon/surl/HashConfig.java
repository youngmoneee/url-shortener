package app.youngmon.surl;

import app.youngmon.surl.interfaces.HashCache;
import app.youngmon.surl.interfaces.HashJpaRepository;
import app.youngmon.surl.interfaces.HashService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashConfig {
    @Value("${hash.base}")
    String hashBase;

    @Bean
    public HashCache hashCache() {
        return new HashCacheImpl();
    }

    @Bean
    public HashService hashService(HashJpaRepository hashRepository) {
        return new HashServiceImpl(hashBase, hashCache(), hashRepository);
    }
}

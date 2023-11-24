package app.youngmon.surl;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.exception.NotFoundException;
import app.youngmon.surl.interfaces.CacheRepository;
import app.youngmon.surl.interfaces.JpaRepository;
import app.youngmon.surl.interfaces.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class HashServiceImpl implements HashService {
    private final
    JpaRepository hashRepository;
    private final
    CacheRepository hashCache;

    @Autowired
    HashServiceImpl(CacheRepository cache, JpaRepository hashRepository)
    {
        this.hashCache = cache;
        this.hashRepository = hashRepository;
    }

    @Override
    public String
    getShortUrl(String longUrl) {
        return findShortUrlOnRepository(longUrl).orElseGet(() ->
                createNewShortUrl(longUrl)
        );
    }

    @Override public String
    getLongUrl(String shortUrl) {
        //  Cache Hit
        String  url = this.hashCache.get(shortUrl);
        if (url != null) return url;

        //  Else
        Long    id = decode(shortUrl);
        String  longUrl =  this.hashRepository.findById(id)
                .map(UrlEntity::getLongUrl)
                .orElseThrow(()-> new NotFoundException("Not Found Url"));

        //  Cache Registry
        this.hashCache.set(shortUrl, longUrl);
        log.info("Registry {} : {}", shortUrl, longUrl);
        return longUrl;
    }

    private Optional<String>
    findShortUrlOnRepository(String longUrl) {
        return this.hashRepository.findUrlEntityByLongUrl(longUrl)
                .map(UrlEntity::getShortUrl);
    }

    private String
    createNewShortUrl(String longUrl) {
        UrlEntity newUrl = this.hashRepository.save(new UrlEntity(longUrl));
        String    shortUrl = encode(newUrl.getId());

        newUrl.setShortUrl(shortUrl);
        this.hashRepository.save(newUrl);
        return shortUrl;
    }
}

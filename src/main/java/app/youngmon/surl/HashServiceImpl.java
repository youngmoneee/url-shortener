package app.youngmon.surl;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.exception.NotFoundException;
import app.youngmon.surl.interfaces.HashCache;
import app.youngmon.surl.interfaces.HashJpaRepository;
import app.youngmon.surl.interfaces.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class HashServiceImpl implements HashService {
    @Value("${const.base}")
    private String      hashBase;
    private final
    HashJpaRepository   hashRepository;
    private final
    HashCache           hashCache;


    @Autowired
    HashServiceImpl(HashCache cache, HashJpaRepository hashRepository)
    {
        this.hashCache = cache;
        this.hashRepository = hashRepository;
    }

    @Override
    public char[]
    getHashBaseArr() {
        return this.hashBase.toCharArray();
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
        String  url = this.hashCache.findUrlByKey(shortUrl);
        if (url != null) return url;

        //  Else
        Long    id = decode(shortUrl);
        String  longUrl =  this.hashRepository.findById(id)
                .map(UrlEntity::getLongUrl)
                .orElseThrow(()-> new NotFoundException("Not Found Url"));

        //  Cache Registry
        this.hashCache.createKV(shortUrl, longUrl);
        log.info("Registry {} : {}", shortUrl, longUrl);
        return longUrl;
    }

    public String
    encode(Long id) {
        char    tmp = getHashBaseArr()[(int) (id % hashBase.length())];
        if (id < hashBase.length()) return String.valueOf(tmp);
        return tmp + encode(id / hashBase.length());
    }

    public Long
    decode(String code) {
        long    res = 0L;
        for (char c : code.toCharArray()) {
            res *= hashBase.length();
            res += hashBase.indexOf(c);
        }
        return res;
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

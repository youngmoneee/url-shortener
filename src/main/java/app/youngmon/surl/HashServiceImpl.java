package app.youngmon.surl;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.exception.NotFoundException;
import app.youngmon.surl.interfaces.HashCache;
import app.youngmon.surl.interfaces.HashJpaRepository;
import app.youngmon.surl.interfaces.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@Slf4j
public class HashServiceImpl implements HashService {
    @Value("${const.root}")
    private String      domain;
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
    public int
    getHashBaseLength() {
        return this.hashBase.length();
    }

    @Override
    public String
    getDomain() { return domain; }

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
        if (url != null) {
            log.info("Cache Hit");
            return url;
        }

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
        char    tmp = getHashBaseArr()[(int) (id % getHashBaseLength())];
        if (id < getHashBaseLength()) return String.valueOf(tmp);
        return tmp + encode(id / getHashBaseLength());
    };

    public Long
    decode(String code) {
        Long    res = 0L;
        for (char c : code.toCharArray()) {
            res *= getHashBaseLength();
            res += decode(c);
        }
        return res;
    }

    private Long
    decode(char c) {
        if (c >= '0' && c <= '9') return (long)(c - '0');
        if (c >= 'A' && c <= 'Z') return (long)(c - 'A' + 10);
        return (long)(c - 'a' + 10 + 26);
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

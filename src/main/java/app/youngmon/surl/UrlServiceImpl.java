package app.youngmon.surl;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.exception.NotFoundException;
import app.youngmon.surl.interfaces.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UrlServiceImpl implements UrlService {
    private final DbRepository      db;
    private final CacheRepository   cache;
    private final Generator         generator;

    @Autowired
    UrlServiceImpl(DbRepository db, CacheRepository cache, Generator generator)
    {
        this.db = db;
        this.cache = cache;
        this.generator = generator;
    }

    @Override
    public String
    getShortUrl(String longUrl) {
        UrlEntity   url;
        try {
            url = this.db.findUrlEntityByLongUrl(longUrl).orElseGet(
                    () -> this.db.save(new UrlEntity(longUrl)));;
        } catch (DataIntegrityViolationException e) {
            url = this.db.findUrlEntityByLongUrl(longUrl).get();
            if (url.getShortUrl() != null) return url.getShortUrl();
        }
        if (url.getShortUrl() != null) return url.getShortUrl();
        url.setShortUrl(this.generator.encode(url.getId()));
        this.db.save(url);
        return url.getShortUrl();
    }

    @Override
    @Transactional(readOnly = true)
    public String
    getLongUrl(String shortUrl) {
        //  Cache Hit
        String url = this.cache.get(shortUrl);
        if (url != null) return url;

        //  Else
        Long    id = this.generator.decode(shortUrl);
        url = this.db.findById(id)
                .map(UrlEntity::getLongUrl)
                .orElseThrow(()-> new NotFoundException("Not Found Url"));

        //  Register
        this.cache.set(shortUrl, url);
        log.debug("Registry {} : {}", shortUrl, url);
        return url;
    }
}

package app.youngmon.surl;

import app.youngmon.surl.cache.Cacheable;
import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.exception.NotFoundException;
import app.youngmon.surl.interfaces.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UrlServiceImpl implements UrlService {
    private final DbRepository      db;
    private final Generator         generator;

    @Autowired
    UrlServiceImpl(DbRepository db, Generator generator)
    {
        this.db = db;
        this.generator = generator;
    }

    @Override
    @Retryable(retryFor = DataIntegrityViolationException.class)
    public String
    getShortUrl(String longUrl) {
        //  Retry if Duplicated
        UrlEntity   url = this.db.findUrlEntityByLongUrl(longUrl).orElseGet(
                () -> this.db.save(new UrlEntity(longUrl)));

        if (url.getShortUrl() != null) return url.getShortUrl();
        url.setShortUrl(this.generator.encode(url.getId()));
        this.db.save(url);
        return url.getShortUrl();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public String
    getLongUrl(String shortUrl) {
        Long    id = this.generator.decode(shortUrl);

        return this.db.findById(id)
                .map(UrlEntity::getLongUrl)
                .orElseThrow(()-> new NotFoundException("Not Found Url"));
    }
}

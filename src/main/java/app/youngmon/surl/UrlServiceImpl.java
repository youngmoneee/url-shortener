package app.youngmon.surl;

import app.youngmon.surl.interfaces.UrlRepository;
import app.youngmon.surl.interfaces.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlServiceImpl implements UrlService {
    private final UrlRepository repository;

    @Autowired
    UrlServiceImpl(UrlRepository urlRepository)
    {
        this.repository = urlRepository;
    }

    @Override
    public String
    getShortUrl(String longUrl) {
        return this.repository.getShortUrl(longUrl);
    }

    @Override public String
    getLongUrl(String shortUrl) {
        return this.repository.getLongUrl(shortUrl);
    }
}

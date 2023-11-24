package app.youngmon.surl;

import app.youngmon.surl.datas.UrlEntity;
import app.youngmon.surl.exception.NotFoundException;
import app.youngmon.surl.interfaces.CacheRepository;
import app.youngmon.surl.interfaces.Generator;
import app.youngmon.surl.interfaces.DbRepository;
import app.youngmon.surl.interfaces.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UrlRepositoryImpl implements UrlRepository {
	private final CacheRepository   cache;
	private final DbRepository db;
	private final Generator         generator;

	@Autowired
	public UrlRepositoryImpl(CacheRepository cache, DbRepository db, Generator generator) {
		this.cache = cache;
		this.db = db;
		this.generator = generator;
	}

	@Override
	public String  getLongUrl(String shortUrl) {
		//  Cache Hit
		String url = this.cache.get(shortUrl);
		if (url != null) return url;

		//  Else
		Long id = this.generator.decode(shortUrl);
		url = this.db.findById(id)
				.map(UrlEntity::getLongUrl)
				.orElseThrow(()-> new NotFoundException("Not Found Url"));

		//  Register
		this.cache.set(shortUrl, url);
		log.info("Registry {} : {}", shortUrl, url);
		return url;
	}

	@Override
	public String  getShortUrl(String longUrl) {

		//  트랜잭션 시작
		UrlEntity url = this.db.findUrlEntityByLongUrl(longUrl).orElseGet(
				() -> this.db.save(new UrlEntity(longUrl)));
		//  트랜잭션 종료

		if (url.getShortUrl() != null) return url.getShortUrl();

		//  Else
		String    shortUrl = this.generator.encode(url.getId());
		url.setShortUrl(shortUrl);
		this.db.save(url);
		return shortUrl;
	}
}

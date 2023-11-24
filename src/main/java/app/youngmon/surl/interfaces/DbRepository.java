package app.youngmon.surl.interfaces;

import app.youngmon.surl.datas.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DbRepository extends JpaRepository<UrlEntity, Long> {
    Optional<UrlEntity> findUrlEntityByLongUrl(String longUrl);
}

package app.youngmon.surl.interfaces;

import app.youngmon.surl.datas.UrlEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbRepository extends JpaRepository<UrlEntity, Long> {

  Optional<UrlEntity> findUrlEntityByLongUrl(String longUrl);
}

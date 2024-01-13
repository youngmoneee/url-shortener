package app.youngmon.surl.cache;

import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@ConditionalOnProperty(name = "spring.cache", havingValue = "true")
public class CacheAspect {

  private final CacheRepository cache;

  @Autowired
  CacheAspect(CacheRepository cache) {
    this.cache = cache;
  }

  @Around("@annotation(cacheable)")
  public Object cache(ProceedingJoinPoint jp, Cacheable cacheable) throws Throwable {
    String arg = (String) jp.getArgs()[0];
    String res;
    try {
      res = this.cache.get(arg);

      //  Cache Hit
      if (res != null) {
        return res;
      }
    } catch (RedisException e) {
      log.debug("Cache Get Failed: " + e.getMessage());
    } catch (RuntimeException e) {
      log.debug("Cache Access Failed: " + e.getMessage());
    }

    //  Find DB If Cache Miss
    res = (String) jp.proceed();

    //  And Registry
    try {
      this.cache.set(arg, res, cacheable.expire());
    } catch (RedisException e) {
      log.debug("Cache Update Failed: " + e.getMessage());
    } catch (RuntimeException e) {
      log.debug("Cache Access Failed: " + e.getMessage());
    }
    return res;
  }
}

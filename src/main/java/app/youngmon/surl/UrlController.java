package app.youngmon.surl;

import app.youngmon.surl.interfaces.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

/*
 * URL 단축 및 리디렉션을 처리하는 컨트롤러
 */
@Controller("/")
@Slf4j
public class UrlController {

  private final UrlService service;

  @Autowired
  UrlController(UrlService service) {
    this.service = service;
  }

  @GetMapping
  String index() {
    return "/index.html";
  }

  @GetMapping("/{code:\\w+}")
  RedirectView redirectUrl(@PathVariable String code) {
    try {
      log.info(code);
      String destUrl = this.service.getLongUrl(code);
      log.info(destUrl);
      return new RedirectView(destUrl);
    } catch (RuntimeException e) {
      log.info(e.getMessage());
      return new RedirectView("/");
    }
  }
}

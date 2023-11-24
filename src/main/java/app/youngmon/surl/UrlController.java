package app.youngmon.surl;

import app.youngmon.surl.datas.UrlDto;
import app.youngmon.surl.interfaces.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
@Slf4j
public class HashController {
	private final HashService service;

	@Autowired
	HashController(HashService service) {
		this.service = service;
	}

	@GetMapping
	String  index(Model model) {
		model.addAttribute("urlDto", new UrlDto());
		return "index";
	}

	@GetMapping("/{code}")
	RedirectView    redirectUrl(@PathVariable String code) {
		try {
			String  destUrl = this.service.getLongUrl(code);
			log.info(destUrl);
			return new RedirectView(destUrl);
		} catch (Exception e) {
			log.info(e.getMessage());
			return new RedirectView("/");
		}
	}
}

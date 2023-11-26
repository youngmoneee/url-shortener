package app.youngmon.surl;

import app.youngmon.surl.exception.BadRequestException;
import app.youngmon.surl.exception.NotFoundException;
import app.youngmon.surl.interfaces.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UrlRestController {
    private final UrlService hashService;

    @Autowired
    UrlRestController(UrlService hashService) {
        this.hashService = hashService;
    }

    @PostMapping
    String makeShort(@RequestBody String code) {
        //  TODO: Validation -> Annotation으로 변경하기
        if (code == null || !code.matches(
                "^(https?://)?([\\da-zA-Z.-]+)\\.([a-zA-Z.]{2,6})([/\\w .-]*)*/?(\\?[\\w =&.-]*)?$"
        ))
            throw new BadRequestException("Required Origin Url");
        return this.hashService.getShortUrl(code);
    }

    @GetMapping("/{code}")
    String getOrigin(@PathVariable String code) { return this.hashService.getLongUrl(code); }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String>  notFoundException(NotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<String>  badRequestException(BadRequestException e) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}

package app.youngmon.surl;

import app.youngmon.surl.exception.NotFoundException;
import app.youngmon.surl.interfaces.HashService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class HashController {
    private final HashService hashService;

    @Autowired
    HashController(HashService hashService) {
        this.hashService = hashService;
    }

    @GetMapping("/")
    void index(HttpServletResponse res) {
        res.setHeader("Location", "/docs");
        res.setStatus(301);
    }

    @GetMapping("/docs")
    String docs() {
        return "[TMP DOCS]\nYou can convert shortUrl to longUrl or longUrl to shortUrl";
    }

    @PostMapping("/")
    String makeShort(@RequestBody String code) {
        return this.hashService.getShortUrl(code);
    }

    @GetMapping("/{code:^.+}")
    String getOrigin(@PathVariable String code) {
        return this.hashService.getLongUrl(code);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String>  notFoundException(NotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

package app.youngmon.surl;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UrlEntity {
    public UrlEntity() {}
    public UrlEntity(String longUrl) {
        this.longUrl = longUrl;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    @Column
    private String  shortUrl;
    @Column
    private String  longUrl;
}

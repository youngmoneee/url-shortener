package app.youngmon.surl;

public interface HashRepository {
    int      createUrl(String url);
    String   findUrlById(int id);
}

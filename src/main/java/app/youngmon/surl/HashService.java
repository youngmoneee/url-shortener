package app.youngmon.surl;

public interface HashService {
    String   getHashBase();
    int      getHashBaseLength();
    String   encode(int id);
    int      decode(String code);
}

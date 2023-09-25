package app.youngmon.surl;

public interface HashService {
    String   getHashBase();
    int      getHashBaseLength();
    String   encode();
    int      decode();
}

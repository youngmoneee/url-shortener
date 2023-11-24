package app.youngmon.surl.interfaces;

public interface Generator {
	String  encode(Long id);
	Long    decode(String code);
}

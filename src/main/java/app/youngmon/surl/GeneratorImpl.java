package app.youngmon.surl;

import app.youngmon.surl.interfaces.Generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeneratorImpl implements Generator {
	@Value("${const.base}")
	private String      baseCode;
	@Override
	public String encode(Long id) {
		char tmp = baseCode.charAt((int)(id % baseCode.length()));
		if (id < baseCode.length()) return String.valueOf(tmp);
		return encode(id / baseCode.length()) + tmp;
	}

	@Override
	public Long decode(String code) {
		long    res = 0L;
		for (char c : code.toCharArray()) {
			res *= baseCode.length();
			res += baseCode.indexOf(c);
		}
		return res;
	}
}

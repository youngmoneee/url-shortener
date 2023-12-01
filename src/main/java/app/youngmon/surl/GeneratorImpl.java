package app.youngmon.surl;

import app.youngmon.surl.interfaces.Generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeneratorImpl implements Generator {
	@Value("${const.base}")
	private String      baseCode;
	@Override
	public String encode(Long id) throws IllegalArgumentException {
		try {
			char tmp = baseCode.charAt((int) (id % baseCode.length()));
			if (id < baseCode.length()) return String.valueOf(tmp);
			return encode(id / baseCode.length()) + tmp;
		} catch (StringIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Encode Error");
		}
	}

	@Override
	public Long decode(String code) throws IllegalArgumentException {
		long    res = 0L;
		int prod = baseCode.length();
		for (char c : code.toCharArray()) {
			int idx = baseCode.indexOf(c);
			if (idx < 0) throw new IllegalArgumentException("Decode Error");
			if (Long.MAX_VALUE / prod < res || Long.MAX_VALUE - idx < res * prod)
				throw new IllegalArgumentException("Decode Overflow");
			res = res * prod + idx;
		}
		return res;
	}
}

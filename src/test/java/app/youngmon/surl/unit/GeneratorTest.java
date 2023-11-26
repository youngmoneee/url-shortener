package app.youngmon.surl.unit;

import app.youngmon.surl.interfaces.Generator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GeneratorTest {
	@Autowired
	Generator   generator;

	@Test
	@DisplayName("Encode Test")
	public void encodeTest() {
		//  given
		Long    id = 1L;

		//  when
		String  code = generator.encode(id);

		//  then
		assertThat(code).isEqualTo("1");
	}

	@Test
	@DisplayName("Decode Test")
	public void decodeTest() {
		//  given
		String code = "1";

		//  when
		Long    id = generator.decode(code);

		//  then
		assertThat(id).isEqualTo(1L);
	}

	@Test
	@DisplayName("Encode Boundary Test")
	public void encodeBoundaryTest() {
		//  given
		Long    id = 61L;
		Long    id2 = 62L;
		Long    id3 = (long)Math.pow(62, 5);
		Long    id4 = id3 - 1;

		//  when
		String  code = generator.encode(id);
		String  code2 = generator.encode(id2);
		String  code3 = generator.encode(id3);
		String  code4 = generator.encode(id4);

		//  then
		assertThat(code).isEqualTo("Z");
		assertThat(code2).isEqualTo("10");
		assertThat(code3).isEqualTo("100000");
		assertThat(code4).isEqualTo("ZZZZZ");
	}

	@Test
	@DisplayName("Decode Boundary Test")
	public void decodeLongTest() {
		//  given
		String  code = "Z";
		String  code2 = "10";
		String  code3 = "100000";
		String  code4 = "ZZZZZ";

		//  when
		Long    id = generator.decode(code);
		Long    id2 = generator.decode(code2);
		Long    id3 = generator.decode(code3);
		Long    id4 = generator.decode(code4);

		//  then
		assertThat(id).isEqualTo(61L);
		assertThat(id2).isEqualTo(62L);
		assertThat(id3).isEqualTo((long) Math.pow(62, 5));
		assertThat(id4).isEqualTo((long) Math.pow(62, 5) - 1);
	}
}

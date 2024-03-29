package app.youngmon.surl.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import app.youngmon.surl.interfaces.Generator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeneratorTest {

  @Autowired
  Generator generator;

  @Test
  @DisplayName("Encode 동작 테스트")
  public void encodeTest() {
    //  given
    Long id = 1L;

    //  when
    String code = generator.encode(id);

    //  then
    assertThat(code).isEqualTo("1");
  }

  @Test
  @DisplayName("Decode 동작 테스트")
  public void decodeTest() {
    //  given
    String code = "1";

    //  when
    Long id = generator.decode(code);

    //  then
    assertThat(id).isEqualTo(1L);
  }

  @Test
  @DisplayName("Encode 인코드 경계값 테스트")
  public void encodeBoundaryTest() {
    //  given
    long id = 61L;
    long id2 = 62L;
    long id3 = 62L * 62L * 62L * 62L * 62L;
    long id4 = id3 - 1;

    //  when
    String code = generator.encode(id);
    String code2 = generator.encode(id2);
    String code3 = generator.encode(id3);
    String code4 = generator.encode(id4);

    //  then
    assertThat(code).isEqualTo("Z");
    assertThat(code2).isEqualTo("10");
    assertThat(code3).isEqualTo("100000");
    assertThat(code4).isEqualTo("ZZZZZ");
  }

  @Test
  @DisplayName("Decode 경계값 테스트")
  public void decodeLongTest() {
    //  given
    String code = "Z";
    String code2 = "10";
    String code3 = "100000";
    String code4 = "ZZZZZ";

    //  when
    Long id = generator.decode(code);
    Long id2 = generator.decode(code2);
    Long id3 = generator.decode(code3);
    Long id4 = generator.decode(code4);

    //  then
    assertThat(id).isEqualTo(61L);
    assertThat(id2).isEqualTo(62L);
    assertThat(id3).isEqualTo((long) Math.pow(62, 5));
    assertThat(id4).isEqualTo((long) Math.pow(62, 5) - 1);
  }

  @Test
  @DisplayName("Encode 잘못된 값 테스트")
  public void validationTest() {
    //  given
    long id = -1;

    //  when & then
    assertThatThrownBy(() -> generator.encode(id))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Decode 잘못된 값 테스트")
  public void validationDecodeTest() {
    //  given
    String code = "-=+";

    //  when & then
    assertThatThrownBy(() -> generator.decode(code))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Encode 경계값 테스트 LongMAX")
  public void longMaxEncodeTest() {
    //  given
    Long id = Long.MAX_VALUE;

    //  when
    String res = generator.encode(id);

    //  then
    assertThat(res).isEqualTo("aZl8N0y58M7");
  }

  @Test
  @DisplayName("Encode 경계값 테스트 LongMAX + 1")
  public void overFlowEncodeTest() {
    //  given
    Long id = Long.MAX_VALUE + 1;

    //  when & then
    assertThatThrownBy(() -> generator.encode(id))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Decode 경계값 테스트 LongMAX")
  public void longMaxDecodeTest() {
    //  given
    String code = "aZl8N0y58M7";

    //  when
    Long id = generator.decode(code);

    //  then
    assertThat(id).isEqualTo(Long.MAX_VALUE);
  }

  @Test
  @DisplayName("Decode 경계값 테스트 LongMAX + 1")
  public void overFlowDecodeTest() {
    //  given
    String code = "aZl8N0y58M8";

    //  when & then
    assertThatThrownBy(() -> generator.decode(code))
        .isInstanceOf(IllegalArgumentException.class);
  }
}

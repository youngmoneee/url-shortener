package app.youngmon.surl.unit;

import app.youngmon.surl.HashRepository;
import app.youngmon.surl.interfaces.HashService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashService Test")
public class HashServiceTest {
    @Mock
    HashService mockHashService;
    @Mock
    HashRepository mockHashRepository;

    @Test
    @DisplayName("getHashBase Test")
    void getHashBaseTest() {
        //  given
        String  hashBase = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        when(mockHashService.getHashBaseArr()).thenReturn(hashBase.toCharArray());

        //  when
        char[]  res = mockHashService.getHashBaseArr();

        //  then
        assertThat(res).isEqualTo(hashBase.toCharArray());
    }

    @Test
    @DisplayName("getHashBaseLength Test")
    void getHashBaseLengthTest() {
        //  given
        String  hashBase = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        when(mockHashService.getHashBaseLength()).thenReturn(hashBase.length());

        //  when
        int     res = mockHashService.getHashBaseLength();

        //  then
        assertThat(res).isEqualTo(hashBase.length());
    }

    @Test
    @DisplayName("encode Test")
    void encodeTest() {
        //  given
        String  url = "http://www.google.com";
        String  hashBase = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String  expectedCode = "1";

        when(mockHashRepository.createUrl(url)).thenReturn(1L);
        Long    id = mockHashRepository.createUrl(url);

        when(mockHashService.encode(id)).thenReturn("" + hashBase.charAt(id % hashBase.length()));

        //  when

        //  then
        assertThat(res).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("decode Test")
    void decodeTest() {
        //  given
        String  code = "1";
        String  hashBase = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Long    expectedId = 1L;

        when(mockHashService.decode(code)).thenReturn((long) hashBase.indexOf(code));

        //  when
        Long res = mockHashService.decode(code);

        //  then
        assertThat(res).isEqualTo(expectedId);
    }
}

package app.youngmon.surl.unit.hash;

import app.youngmon.surl.HashService;
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

    @Test
    @DisplayName("getHashBase Test")
    void getHashBaseTest() {
        //  given
        String  hashBase = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        when(mockHashService.getHashBase()).thenReturn(hashBase);

        //  when
        String  res = mockHashService.getHashBase();

        //  then
        assertThat(res).isEqualTo(hashBase);
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
        int id = 1;
        String  hashBase = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String  expectedCode = "1";

        when(mockHashService.encode(id)).thenReturn("" + hashBase.charAt(id % hashBase.length()));

        //  when
        String  res = mockHashService.encode(id);

        //  then
        assertThat(res).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("decode Test")
    void decodeTest() {
        //  given
        String  code = "1";
        String  hashBase = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int     expectedId = 1;

        when(mockHashService.decode(code)).thenReturn(hashBase.indexOf(code));

        //  when
        int res = mockHashService.decode(code);

        //  then
        assertThat(res).isEqualTo(expectedId);
    }
}

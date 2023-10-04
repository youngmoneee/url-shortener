package app.youngmon.surl.unit;

import app.youngmon.surl.interfaces.HashCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@DisplayName("Hash Cache Test")
public class HashCacheTest {
    @Mock
    HashCache mockHashCache;

    @Test
    @DisplayName("createKV Test")
    void    createKVTest() {
        //  given
        String  key = "key";
        String  value = "value";
        when(mockHashCache.createKV(key, value)).thenReturn(value);

        //  when
        String  res = mockHashCache.createKV(key, value);

        //  then
        assertThat(res).isEqualTo(value);
    }

    @Test
    @DisplayName("findUrlByKeyTest")
    void    findUrlByKeyTest() {
        //  given
        String  key = "key";
        String  expectedUrl = "url";
        when(mockHashCache.findUrlByKey(key)).thenReturn(expectedUrl);

        //  when
        String  res = mockHashCache.findUrlByKey(key);

        //  then
        assertThat(res).isEqualTo(expectedUrl);
    }
}

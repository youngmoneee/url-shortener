package app.youngmon.surl.unit.hash;

import app.youngmon.surl.HashRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashRepository Test")
public class HashRepositoryTest {
    @Mock
    HashRepository mockHashRepository;

    @Test
    @DisplayName("createUrl Test")
    void createUrlTest() {
        //  given
        String  url = "url";
        int     expectedId = 1;
        when(mockHashRepository.createUrl(url)).thenReturn(expectedId);

        //  when
        int     res = mockHashRepository.createUrl(url);

        //  then
        assertThat(res).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findUrlById Test")
    void findUrlByIdTest() {
        //  given
        int     id = 1;
        String  expectedUrl = "url";
        when(mockHashRepository.findUrlById(id)).thenReturn(expectedUrl);

        //  when
        String  res = mockHashRepository.findUrlById(id);

        //  then
        assertThat(res).isEqualTo(expectedUrl);
    }
}

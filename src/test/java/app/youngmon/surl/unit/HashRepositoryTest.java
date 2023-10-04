package app.youngmon.surl.unit;

import app.youngmon.surl.HashRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashRepository Test")
public class HashRepositoryTest {
    @Mock
    HashRepository mockHashRepository;

    @Test
    @DisplayName("createUrl not exist Test")
    void createUrlTest() {
        //  given
        String  url = "url";
        Long    expectedId = 1L;
        when(mockHashRepository.createUrl(url)).thenReturn(expectedId);

        //  when
        Long     res = mockHashRepository.createUrl(url);

        //  then
        assertThat(res).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("createUrl exist Test")
    void createUrlExistTest() {
        String  url = "url";
        when(mockHashRepository.)
    }

    @Test
    @DisplayName("findUrlById Test")
    void findUrlByIdTest() {
        //  given
        Long    id = 1L;
        String  expectedUrl = "url";
        when(mockHashRepository.findUrlById(id)).thenReturn(Optional.ofNullable(expectedUrl));

        //  when
        Optional<String>  res = mockHashRepository.findUrlById(id);

        //  then
        assertThat(res).isEqualTo(expectedUrl);
    }
}

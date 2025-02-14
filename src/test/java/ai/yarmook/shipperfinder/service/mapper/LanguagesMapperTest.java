package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.LanguagesAsserts.*;
import static ai.yarmook.shipperfinder.domain.LanguagesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LanguagesMapperTest {

    private LanguagesMapper languagesMapper;

    @BeforeEach
    void setUp() {
        languagesMapper = new LanguagesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLanguagesSample1();
        var actual = languagesMapper.toEntity(languagesMapper.toDto(expected));
        assertLanguagesAllPropertiesEquals(expected, actual);
    }
}

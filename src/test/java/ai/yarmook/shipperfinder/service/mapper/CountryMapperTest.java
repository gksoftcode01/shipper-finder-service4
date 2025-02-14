package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.CountryAsserts.*;
import static ai.yarmook.shipperfinder.domain.CountryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryMapperTest {

    private CountryMapper countryMapper;

    @BeforeEach
    void setUp() {
        countryMapper = new CountryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCountrySample1();
        var actual = countryMapper.toEntity(countryMapper.toDto(expected));
        assertCountryAllPropertiesEquals(expected, actual);
    }
}

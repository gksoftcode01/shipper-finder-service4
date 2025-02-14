package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.UserRateAsserts.*;
import static ai.yarmook.shipperfinder.domain.UserRateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRateMapperTest {

    private UserRateMapper userRateMapper;

    @BeforeEach
    void setUp() {
        userRateMapper = new UserRateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserRateSample1();
        var actual = userRateMapper.toEntity(userRateMapper.toDto(expected));
        assertUserRateAllPropertiesEquals(expected, actual);
    }
}

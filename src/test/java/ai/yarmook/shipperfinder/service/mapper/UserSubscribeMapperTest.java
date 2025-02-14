package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.UserSubscribeAsserts.*;
import static ai.yarmook.shipperfinder.domain.UserSubscribeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserSubscribeMapperTest {

    private UserSubscribeMapper userSubscribeMapper;

    @BeforeEach
    void setUp() {
        userSubscribeMapper = new UserSubscribeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserSubscribeSample1();
        var actual = userSubscribeMapper.toEntity(userSubscribeMapper.toDto(expected));
        assertUserSubscribeAllPropertiesEquals(expected, actual);
    }
}

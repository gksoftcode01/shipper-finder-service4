package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.SubscribeTypeDetailAsserts.*;
import static ai.yarmook.shipperfinder.domain.SubscribeTypeDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscribeTypeDetailMapperTest {

    private SubscribeTypeDetailMapper subscribeTypeDetailMapper;

    @BeforeEach
    void setUp() {
        subscribeTypeDetailMapper = new SubscribeTypeDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubscribeTypeDetailSample1();
        var actual = subscribeTypeDetailMapper.toEntity(subscribeTypeDetailMapper.toDto(expected));
        assertSubscribeTypeDetailAllPropertiesEquals(expected, actual);
    }
}

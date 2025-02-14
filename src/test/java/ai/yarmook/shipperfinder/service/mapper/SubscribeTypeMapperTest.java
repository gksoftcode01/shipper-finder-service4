package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.SubscribeTypeAsserts.*;
import static ai.yarmook.shipperfinder.domain.SubscribeTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscribeTypeMapperTest {

    private SubscribeTypeMapper subscribeTypeMapper;

    @BeforeEach
    void setUp() {
        subscribeTypeMapper = new SubscribeTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubscribeTypeSample1();
        var actual = subscribeTypeMapper.toEntity(subscribeTypeMapper.toDto(expected));
        assertSubscribeTypeAllPropertiesEquals(expected, actual);
    }
}

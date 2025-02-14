package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.ItemAsserts.*;
import static ai.yarmook.shipperfinder.domain.ItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemMapperTest {

    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getItemSample1();
        var actual = itemMapper.toEntity(itemMapper.toDto(expected));
        assertItemAllPropertiesEquals(expected, actual);
    }
}

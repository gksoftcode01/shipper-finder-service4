package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.ItemTypeAsserts.*;
import static ai.yarmook.shipperfinder.domain.ItemTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemTypeMapperTest {

    private ItemTypeMapper itemTypeMapper;

    @BeforeEach
    void setUp() {
        itemTypeMapper = new ItemTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getItemTypeSample1();
        var actual = itemTypeMapper.toEntity(itemTypeMapper.toDto(expected));
        assertItemTypeAllPropertiesEquals(expected, actual);
    }
}

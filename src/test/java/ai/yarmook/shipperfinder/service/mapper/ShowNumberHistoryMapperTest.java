package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.ShowNumberHistoryAsserts.*;
import static ai.yarmook.shipperfinder.domain.ShowNumberHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShowNumberHistoryMapperTest {

    private ShowNumberHistoryMapper showNumberHistoryMapper;

    @BeforeEach
    void setUp() {
        showNumberHistoryMapper = new ShowNumberHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShowNumberHistorySample1();
        var actual = showNumberHistoryMapper.toEntity(showNumberHistoryMapper.toDto(expected));
        assertShowNumberHistoryAllPropertiesEquals(expected, actual);
    }
}

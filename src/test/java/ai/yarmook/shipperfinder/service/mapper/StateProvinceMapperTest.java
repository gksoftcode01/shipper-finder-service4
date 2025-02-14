package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.StateProvinceAsserts.*;
import static ai.yarmook.shipperfinder.domain.StateProvinceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateProvinceMapperTest {

    private StateProvinceMapper stateProvinceMapper;

    @BeforeEach
    void setUp() {
        stateProvinceMapper = new StateProvinceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStateProvinceSample1();
        var actual = stateProvinceMapper.toEntity(stateProvinceMapper.toDto(expected));
        assertStateProvinceAllPropertiesEquals(expected, actual);
    }
}

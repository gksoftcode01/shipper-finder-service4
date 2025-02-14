package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.CargoRequestItemAsserts.*;
import static ai.yarmook.shipperfinder.domain.CargoRequestItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CargoRequestItemMapperTest {

    private CargoRequestItemMapper cargoRequestItemMapper;

    @BeforeEach
    void setUp() {
        cargoRequestItemMapper = new CargoRequestItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCargoRequestItemSample1();
        var actual = cargoRequestItemMapper.toEntity(cargoRequestItemMapper.toDto(expected));
        assertCargoRequestItemAllPropertiesEquals(expected, actual);
    }
}

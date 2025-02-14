package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.CargoRequestAsserts.*;
import static ai.yarmook.shipperfinder.domain.CargoRequestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CargoRequestMapperTest {

    private CargoRequestMapper cargoRequestMapper;

    @BeforeEach
    void setUp() {
        cargoRequestMapper = new CargoRequestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCargoRequestSample1();
        var actual = cargoRequestMapper.toEntity(cargoRequestMapper.toDto(expected));
        assertCargoRequestAllPropertiesEquals(expected, actual);
    }
}

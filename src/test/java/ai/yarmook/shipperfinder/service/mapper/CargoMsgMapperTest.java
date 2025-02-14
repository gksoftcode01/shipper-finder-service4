package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.CargoMsgAsserts.*;
import static ai.yarmook.shipperfinder.domain.CargoMsgTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CargoMsgMapperTest {

    private CargoMsgMapper cargoMsgMapper;

    @BeforeEach
    void setUp() {
        cargoMsgMapper = new CargoMsgMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCargoMsgSample1();
        var actual = cargoMsgMapper.toEntity(cargoMsgMapper.toDto(expected));
        assertCargoMsgAllPropertiesEquals(expected, actual);
    }
}

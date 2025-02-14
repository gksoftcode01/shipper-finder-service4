package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.TripAsserts.*;
import static ai.yarmook.shipperfinder.domain.TripTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripMapperTest {

    private TripMapper tripMapper;

    @BeforeEach
    void setUp() {
        tripMapper = new TripMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTripSample1();
        var actual = tripMapper.toEntity(tripMapper.toDto(expected));
        assertTripAllPropertiesEquals(expected, actual);
    }
}

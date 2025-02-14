package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.TripItemAsserts.*;
import static ai.yarmook.shipperfinder.domain.TripItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripItemMapperTest {

    private TripItemMapper tripItemMapper;

    @BeforeEach
    void setUp() {
        tripItemMapper = new TripItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTripItemSample1();
        var actual = tripItemMapper.toEntity(tripItemMapper.toDto(expected));
        assertTripItemAllPropertiesEquals(expected, actual);
    }
}

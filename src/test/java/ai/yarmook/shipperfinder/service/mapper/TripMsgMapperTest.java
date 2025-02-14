package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.TripMsgAsserts.*;
import static ai.yarmook.shipperfinder.domain.TripMsgTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripMsgMapperTest {

    private TripMsgMapper tripMsgMapper;

    @BeforeEach
    void setUp() {
        tripMsgMapper = new TripMsgMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTripMsgSample1();
        var actual = tripMsgMapper.toEntity(tripMsgMapper.toDto(expected));
        assertTripMsgAllPropertiesEquals(expected, actual);
    }
}

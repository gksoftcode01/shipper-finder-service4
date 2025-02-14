package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.AppUserDeviceAsserts.*;
import static ai.yarmook.shipperfinder.domain.AppUserDeviceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppUserDeviceMapperTest {

    private AppUserDeviceMapper appUserDeviceMapper;

    @BeforeEach
    void setUp() {
        appUserDeviceMapper = new AppUserDeviceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppUserDeviceSample1();
        var actual = appUserDeviceMapper.toEntity(appUserDeviceMapper.toDto(expected));
        assertAppUserDeviceAllPropertiesEquals(expected, actual);
    }
}

package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.AppUserDeviceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppUserDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUserDevice.class);
        AppUserDevice appUserDevice1 = getAppUserDeviceSample1();
        AppUserDevice appUserDevice2 = new AppUserDevice();
        assertThat(appUserDevice1).isNotEqualTo(appUserDevice2);

        appUserDevice2.setId(appUserDevice1.getId());
        assertThat(appUserDevice1).isEqualTo(appUserDevice2);

        appUserDevice2 = getAppUserDeviceSample2();
        assertThat(appUserDevice1).isNotEqualTo(appUserDevice2);
    }
}

package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppUserDeviceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUserDeviceDTO.class);
        AppUserDeviceDTO appUserDeviceDTO1 = new AppUserDeviceDTO();
        appUserDeviceDTO1.setId(1L);
        AppUserDeviceDTO appUserDeviceDTO2 = new AppUserDeviceDTO();
        assertThat(appUserDeviceDTO1).isNotEqualTo(appUserDeviceDTO2);
        appUserDeviceDTO2.setId(appUserDeviceDTO1.getId());
        assertThat(appUserDeviceDTO1).isEqualTo(appUserDeviceDTO2);
        appUserDeviceDTO2.setId(2L);
        assertThat(appUserDeviceDTO1).isNotEqualTo(appUserDeviceDTO2);
        appUserDeviceDTO1.setId(null);
        assertThat(appUserDeviceDTO1).isNotEqualTo(appUserDeviceDTO2);
    }
}

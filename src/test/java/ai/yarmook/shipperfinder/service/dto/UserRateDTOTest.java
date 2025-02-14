package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserRateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRateDTO.class);
        UserRateDTO userRateDTO1 = new UserRateDTO();
        userRateDTO1.setId(1L);
        UserRateDTO userRateDTO2 = new UserRateDTO();
        assertThat(userRateDTO1).isNotEqualTo(userRateDTO2);
        userRateDTO2.setId(userRateDTO1.getId());
        assertThat(userRateDTO1).isEqualTo(userRateDTO2);
        userRateDTO2.setId(2L);
        assertThat(userRateDTO1).isNotEqualTo(userRateDTO2);
        userRateDTO1.setId(null);
        assertThat(userRateDTO1).isNotEqualTo(userRateDTO2);
    }
}

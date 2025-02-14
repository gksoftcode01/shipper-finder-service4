package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSubscribeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSubscribeDTO.class);
        UserSubscribeDTO userSubscribeDTO1 = new UserSubscribeDTO();
        userSubscribeDTO1.setId(1L);
        UserSubscribeDTO userSubscribeDTO2 = new UserSubscribeDTO();
        assertThat(userSubscribeDTO1).isNotEqualTo(userSubscribeDTO2);
        userSubscribeDTO2.setId(userSubscribeDTO1.getId());
        assertThat(userSubscribeDTO1).isEqualTo(userSubscribeDTO2);
        userSubscribeDTO2.setId(2L);
        assertThat(userSubscribeDTO1).isNotEqualTo(userSubscribeDTO2);
        userSubscribeDTO1.setId(null);
        assertThat(userSubscribeDTO1).isNotEqualTo(userSubscribeDTO2);
    }
}

package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscribeTypeDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscribeTypeDetailDTO.class);
        SubscribeTypeDetailDTO subscribeTypeDetailDTO1 = new SubscribeTypeDetailDTO();
        subscribeTypeDetailDTO1.setId(1L);
        SubscribeTypeDetailDTO subscribeTypeDetailDTO2 = new SubscribeTypeDetailDTO();
        assertThat(subscribeTypeDetailDTO1).isNotEqualTo(subscribeTypeDetailDTO2);
        subscribeTypeDetailDTO2.setId(subscribeTypeDetailDTO1.getId());
        assertThat(subscribeTypeDetailDTO1).isEqualTo(subscribeTypeDetailDTO2);
        subscribeTypeDetailDTO2.setId(2L);
        assertThat(subscribeTypeDetailDTO1).isNotEqualTo(subscribeTypeDetailDTO2);
        subscribeTypeDetailDTO1.setId(null);
        assertThat(subscribeTypeDetailDTO1).isNotEqualTo(subscribeTypeDetailDTO2);
    }
}

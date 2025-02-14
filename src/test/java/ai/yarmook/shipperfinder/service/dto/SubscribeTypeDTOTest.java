package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscribeTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscribeTypeDTO.class);
        SubscribeTypeDTO subscribeTypeDTO1 = new SubscribeTypeDTO();
        subscribeTypeDTO1.setId(1L);
        SubscribeTypeDTO subscribeTypeDTO2 = new SubscribeTypeDTO();
        assertThat(subscribeTypeDTO1).isNotEqualTo(subscribeTypeDTO2);
        subscribeTypeDTO2.setId(subscribeTypeDTO1.getId());
        assertThat(subscribeTypeDTO1).isEqualTo(subscribeTypeDTO2);
        subscribeTypeDTO2.setId(2L);
        assertThat(subscribeTypeDTO1).isNotEqualTo(subscribeTypeDTO2);
        subscribeTypeDTO1.setId(null);
        assertThat(subscribeTypeDTO1).isNotEqualTo(subscribeTypeDTO2);
    }
}

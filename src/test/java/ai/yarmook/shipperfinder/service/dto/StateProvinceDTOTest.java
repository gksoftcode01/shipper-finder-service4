package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateProvinceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateProvinceDTO.class);
        StateProvinceDTO stateProvinceDTO1 = new StateProvinceDTO();
        stateProvinceDTO1.setId(1L);
        StateProvinceDTO stateProvinceDTO2 = new StateProvinceDTO();
        assertThat(stateProvinceDTO1).isNotEqualTo(stateProvinceDTO2);
        stateProvinceDTO2.setId(stateProvinceDTO1.getId());
        assertThat(stateProvinceDTO1).isEqualTo(stateProvinceDTO2);
        stateProvinceDTO2.setId(2L);
        assertThat(stateProvinceDTO1).isNotEqualTo(stateProvinceDTO2);
        stateProvinceDTO1.setId(null);
        assertThat(stateProvinceDTO1).isNotEqualTo(stateProvinceDTO2);
    }
}

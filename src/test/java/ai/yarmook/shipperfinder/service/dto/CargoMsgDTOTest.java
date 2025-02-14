package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoMsgDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoMsgDTO.class);
        CargoMsgDTO cargoMsgDTO1 = new CargoMsgDTO();
        cargoMsgDTO1.setId(1L);
        CargoMsgDTO cargoMsgDTO2 = new CargoMsgDTO();
        assertThat(cargoMsgDTO1).isNotEqualTo(cargoMsgDTO2);
        cargoMsgDTO2.setId(cargoMsgDTO1.getId());
        assertThat(cargoMsgDTO1).isEqualTo(cargoMsgDTO2);
        cargoMsgDTO2.setId(2L);
        assertThat(cargoMsgDTO1).isNotEqualTo(cargoMsgDTO2);
        cargoMsgDTO1.setId(null);
        assertThat(cargoMsgDTO1).isNotEqualTo(cargoMsgDTO2);
    }
}

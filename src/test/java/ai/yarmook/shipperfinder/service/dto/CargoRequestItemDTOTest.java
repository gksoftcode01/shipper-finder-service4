package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoRequestItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoRequestItemDTO.class);
        CargoRequestItemDTO cargoRequestItemDTO1 = new CargoRequestItemDTO();
        cargoRequestItemDTO1.setId(1L);
        CargoRequestItemDTO cargoRequestItemDTO2 = new CargoRequestItemDTO();
        assertThat(cargoRequestItemDTO1).isNotEqualTo(cargoRequestItemDTO2);
        cargoRequestItemDTO2.setId(cargoRequestItemDTO1.getId());
        assertThat(cargoRequestItemDTO1).isEqualTo(cargoRequestItemDTO2);
        cargoRequestItemDTO2.setId(2L);
        assertThat(cargoRequestItemDTO1).isNotEqualTo(cargoRequestItemDTO2);
        cargoRequestItemDTO1.setId(null);
        assertThat(cargoRequestItemDTO1).isNotEqualTo(cargoRequestItemDTO2);
    }
}

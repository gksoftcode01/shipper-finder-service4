package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoRequestDTO.class);
        CargoRequestDTO cargoRequestDTO1 = new CargoRequestDTO();
        cargoRequestDTO1.setId(1L);
        CargoRequestDTO cargoRequestDTO2 = new CargoRequestDTO();
        assertThat(cargoRequestDTO1).isNotEqualTo(cargoRequestDTO2);
        cargoRequestDTO2.setId(cargoRequestDTO1.getId());
        assertThat(cargoRequestDTO1).isEqualTo(cargoRequestDTO2);
        cargoRequestDTO2.setId(2L);
        assertThat(cargoRequestDTO1).isNotEqualTo(cargoRequestDTO2);
        cargoRequestDTO1.setId(null);
        assertThat(cargoRequestDTO1).isNotEqualTo(cargoRequestDTO2);
    }
}

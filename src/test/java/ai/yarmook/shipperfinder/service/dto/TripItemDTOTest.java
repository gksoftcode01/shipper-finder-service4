package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripItemDTO.class);
        TripItemDTO tripItemDTO1 = new TripItemDTO();
        tripItemDTO1.setId(1L);
        TripItemDTO tripItemDTO2 = new TripItemDTO();
        assertThat(tripItemDTO1).isNotEqualTo(tripItemDTO2);
        tripItemDTO2.setId(tripItemDTO1.getId());
        assertThat(tripItemDTO1).isEqualTo(tripItemDTO2);
        tripItemDTO2.setId(2L);
        assertThat(tripItemDTO1).isNotEqualTo(tripItemDTO2);
        tripItemDTO1.setId(null);
        assertThat(tripItemDTO1).isNotEqualTo(tripItemDTO2);
    }
}

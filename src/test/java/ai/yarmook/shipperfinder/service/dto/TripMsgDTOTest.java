package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripMsgDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripMsgDTO.class);
        TripMsgDTO tripMsgDTO1 = new TripMsgDTO();
        tripMsgDTO1.setId(1L);
        TripMsgDTO tripMsgDTO2 = new TripMsgDTO();
        assertThat(tripMsgDTO1).isNotEqualTo(tripMsgDTO2);
        tripMsgDTO2.setId(tripMsgDTO1.getId());
        assertThat(tripMsgDTO1).isEqualTo(tripMsgDTO2);
        tripMsgDTO2.setId(2L);
        assertThat(tripMsgDTO1).isNotEqualTo(tripMsgDTO2);
        tripMsgDTO1.setId(null);
        assertThat(tripMsgDTO1).isNotEqualTo(tripMsgDTO2);
    }
}

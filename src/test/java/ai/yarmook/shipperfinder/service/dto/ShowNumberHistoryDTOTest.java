package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowNumberHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShowNumberHistoryDTO.class);
        ShowNumberHistoryDTO showNumberHistoryDTO1 = new ShowNumberHistoryDTO();
        showNumberHistoryDTO1.setId(1L);
        ShowNumberHistoryDTO showNumberHistoryDTO2 = new ShowNumberHistoryDTO();
        assertThat(showNumberHistoryDTO1).isNotEqualTo(showNumberHistoryDTO2);
        showNumberHistoryDTO2.setId(showNumberHistoryDTO1.getId());
        assertThat(showNumberHistoryDTO1).isEqualTo(showNumberHistoryDTO2);
        showNumberHistoryDTO2.setId(2L);
        assertThat(showNumberHistoryDTO1).isNotEqualTo(showNumberHistoryDTO2);
        showNumberHistoryDTO1.setId(null);
        assertThat(showNumberHistoryDTO1).isNotEqualTo(showNumberHistoryDTO2);
    }
}

package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.ShowNumberHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowNumberHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShowNumberHistory.class);
        ShowNumberHistory showNumberHistory1 = getShowNumberHistorySample1();
        ShowNumberHistory showNumberHistory2 = new ShowNumberHistory();
        assertThat(showNumberHistory1).isNotEqualTo(showNumberHistory2);

        showNumberHistory2.setId(showNumberHistory1.getId());
        assertThat(showNumberHistory1).isEqualTo(showNumberHistory2);

        showNumberHistory2 = getShowNumberHistorySample2();
        assertThat(showNumberHistory1).isNotEqualTo(showNumberHistory2);
    }
}

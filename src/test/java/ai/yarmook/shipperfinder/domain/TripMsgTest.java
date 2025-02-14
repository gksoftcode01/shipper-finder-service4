package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.TripMsgTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripMsgTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripMsg.class);
        TripMsg tripMsg1 = getTripMsgSample1();
        TripMsg tripMsg2 = new TripMsg();
        assertThat(tripMsg1).isNotEqualTo(tripMsg2);

        tripMsg2.setId(tripMsg1.getId());
        assertThat(tripMsg1).isEqualTo(tripMsg2);

        tripMsg2 = getTripMsgSample2();
        assertThat(tripMsg1).isNotEqualTo(tripMsg2);
    }
}

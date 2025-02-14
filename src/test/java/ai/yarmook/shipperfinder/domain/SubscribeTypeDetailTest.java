package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.SubscribeTypeDetailTestSamples.*;
import static ai.yarmook.shipperfinder.domain.SubscribeTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscribeTypeDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscribeTypeDetail.class);
        SubscribeTypeDetail subscribeTypeDetail1 = getSubscribeTypeDetailSample1();
        SubscribeTypeDetail subscribeTypeDetail2 = new SubscribeTypeDetail();
        assertThat(subscribeTypeDetail1).isNotEqualTo(subscribeTypeDetail2);

        subscribeTypeDetail2.setId(subscribeTypeDetail1.getId());
        assertThat(subscribeTypeDetail1).isEqualTo(subscribeTypeDetail2);

        subscribeTypeDetail2 = getSubscribeTypeDetailSample2();
        assertThat(subscribeTypeDetail1).isNotEqualTo(subscribeTypeDetail2);
    }

    @Test
    void subscribeTypeTest() {
        SubscribeTypeDetail subscribeTypeDetail = getSubscribeTypeDetailRandomSampleGenerator();
        SubscribeType subscribeTypeBack = getSubscribeTypeRandomSampleGenerator();

        subscribeTypeDetail.setSubscribeType(subscribeTypeBack);
        assertThat(subscribeTypeDetail.getSubscribeType()).isEqualTo(subscribeTypeBack);

        subscribeTypeDetail.subscribeType(null);
        assertThat(subscribeTypeDetail.getSubscribeType()).isNull();
    }
}

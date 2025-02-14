package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.SubscribeTypeDetailTestSamples.*;
import static ai.yarmook.shipperfinder.domain.SubscribeTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscribeTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscribeType.class);
        SubscribeType subscribeType1 = getSubscribeTypeSample1();
        SubscribeType subscribeType2 = new SubscribeType();
        assertThat(subscribeType1).isNotEqualTo(subscribeType2);

        subscribeType2.setId(subscribeType1.getId());
        assertThat(subscribeType1).isEqualTo(subscribeType2);

        subscribeType2 = getSubscribeTypeSample2();
        assertThat(subscribeType1).isNotEqualTo(subscribeType2);
    }

    @Test
    void subscribeTypeDetailTest() {
        SubscribeType subscribeType = getSubscribeTypeRandomSampleGenerator();
        SubscribeTypeDetail subscribeTypeDetailBack = getSubscribeTypeDetailRandomSampleGenerator();

        subscribeType.setSubscribeTypeDetail(subscribeTypeDetailBack);
        assertThat(subscribeType.getSubscribeTypeDetail()).isEqualTo(subscribeTypeDetailBack);
        assertThat(subscribeTypeDetailBack.getSubscribeType()).isEqualTo(subscribeType);

        subscribeType.subscribeTypeDetail(null);
        assertThat(subscribeType.getSubscribeTypeDetail()).isNull();
        assertThat(subscribeTypeDetailBack.getSubscribeType()).isNull();
    }
}

package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.SubscribeTypeTestSamples.*;
import static ai.yarmook.shipperfinder.domain.UserSubscribeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSubscribeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSubscribe.class);
        UserSubscribe userSubscribe1 = getUserSubscribeSample1();
        UserSubscribe userSubscribe2 = new UserSubscribe();
        assertThat(userSubscribe1).isNotEqualTo(userSubscribe2);

        userSubscribe2.setId(userSubscribe1.getId());
        assertThat(userSubscribe1).isEqualTo(userSubscribe2);

        userSubscribe2 = getUserSubscribeSample2();
        assertThat(userSubscribe1).isNotEqualTo(userSubscribe2);
    }

    @Test
    void subscribeTypeTest() {
        UserSubscribe userSubscribe = getUserSubscribeRandomSampleGenerator();
        SubscribeType subscribeTypeBack = getSubscribeTypeRandomSampleGenerator();

        userSubscribe.setSubscribeType(subscribeTypeBack);
        assertThat(userSubscribe.getSubscribeType()).isEqualTo(subscribeTypeBack);

        userSubscribe.subscribeType(null);
        assertThat(userSubscribe.getSubscribeType()).isNull();
    }
}

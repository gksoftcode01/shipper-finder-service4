package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.UserRateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserRateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRate.class);
        UserRate userRate1 = getUserRateSample1();
        UserRate userRate2 = new UserRate();
        assertThat(userRate1).isNotEqualTo(userRate2);

        userRate2.setId(userRate1.getId());
        assertThat(userRate1).isEqualTo(userRate2);

        userRate2 = getUserRateSample2();
        assertThat(userRate1).isNotEqualTo(userRate2);
    }
}

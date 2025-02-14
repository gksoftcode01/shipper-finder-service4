package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.LanguagesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanguagesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Languages.class);
        Languages languages1 = getLanguagesSample1();
        Languages languages2 = new Languages();
        assertThat(languages1).isNotEqualTo(languages2);

        languages2.setId(languages1.getId());
        assertThat(languages1).isEqualTo(languages2);

        languages2 = getLanguagesSample2();
        assertThat(languages1).isNotEqualTo(languages2);
    }
}

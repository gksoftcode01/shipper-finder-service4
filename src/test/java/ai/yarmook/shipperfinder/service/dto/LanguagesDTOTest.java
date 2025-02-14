package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanguagesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanguagesDTO.class);
        LanguagesDTO languagesDTO1 = new LanguagesDTO();
        languagesDTO1.setId(1L);
        LanguagesDTO languagesDTO2 = new LanguagesDTO();
        assertThat(languagesDTO1).isNotEqualTo(languagesDTO2);
        languagesDTO2.setId(languagesDTO1.getId());
        assertThat(languagesDTO1).isEqualTo(languagesDTO2);
        languagesDTO2.setId(2L);
        assertThat(languagesDTO1).isNotEqualTo(languagesDTO2);
        languagesDTO1.setId(null);
        assertThat(languagesDTO1).isNotEqualTo(languagesDTO2);
    }
}

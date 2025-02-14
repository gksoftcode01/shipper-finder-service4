package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.AppUserTestSamples.*;
import static ai.yarmook.shipperfinder.domain.CountryTestSamples.*;
import static ai.yarmook.shipperfinder.domain.LanguagesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void preferdLanguageTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Languages languagesBack = getLanguagesRandomSampleGenerator();

        appUser.setPreferdLanguage(languagesBack);
        assertThat(appUser.getPreferdLanguage()).isEqualTo(languagesBack);

        appUser.preferdLanguage(null);
        assertThat(appUser.getPreferdLanguage()).isNull();
    }

    @Test
    void locationTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        appUser.setLocation(countryBack);
        assertThat(appUser.getLocation()).isEqualTo(countryBack);

        appUser.location(null);
        assertThat(appUser.getLocation()).isNull();
    }
}

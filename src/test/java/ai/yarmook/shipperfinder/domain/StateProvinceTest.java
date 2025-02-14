package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.CountryTestSamples.*;
import static ai.yarmook.shipperfinder.domain.StateProvinceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateProvinceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateProvince.class);
        StateProvince stateProvince1 = getStateProvinceSample1();
        StateProvince stateProvince2 = new StateProvince();
        assertThat(stateProvince1).isNotEqualTo(stateProvince2);

        stateProvince2.setId(stateProvince1.getId());
        assertThat(stateProvince1).isEqualTo(stateProvince2);

        stateProvince2 = getStateProvinceSample2();
        assertThat(stateProvince1).isNotEqualTo(stateProvince2);
    }

    @Test
    void countryTest() {
        StateProvince stateProvince = getStateProvinceRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        stateProvince.setCountry(countryBack);
        assertThat(stateProvince.getCountry()).isEqualTo(countryBack);

        stateProvince.country(null);
        assertThat(stateProvince.getCountry()).isNull();
    }
}

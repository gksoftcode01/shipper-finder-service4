package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.CountryTestSamples.*;
import static ai.yarmook.shipperfinder.domain.StateProvinceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void stateProvinceTest() {
        Country country = getCountryRandomSampleGenerator();
        StateProvince stateProvinceBack = getStateProvinceRandomSampleGenerator();

        country.addStateProvince(stateProvinceBack);
        assertThat(country.getStateProvinces()).containsOnly(stateProvinceBack);
        assertThat(stateProvinceBack.getCountry()).isEqualTo(country);

        country.removeStateProvince(stateProvinceBack);
        assertThat(country.getStateProvinces()).doesNotContain(stateProvinceBack);
        assertThat(stateProvinceBack.getCountry()).isNull();

        country.stateProvinces(new HashSet<>(Set.of(stateProvinceBack)));
        assertThat(country.getStateProvinces()).containsOnly(stateProvinceBack);
        assertThat(stateProvinceBack.getCountry()).isEqualTo(country);

        country.setStateProvinces(new HashSet<>());
        assertThat(country.getStateProvinces()).doesNotContain(stateProvinceBack);
        assertThat(stateProvinceBack.getCountry()).isNull();
    }
}

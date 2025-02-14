package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.CargoRequestItemTestSamples.*;
import static ai.yarmook.shipperfinder.domain.CargoRequestTestSamples.*;
import static ai.yarmook.shipperfinder.domain.CountryTestSamples.*;
import static ai.yarmook.shipperfinder.domain.StateProvinceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CargoRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoRequest.class);
        CargoRequest cargoRequest1 = getCargoRequestSample1();
        CargoRequest cargoRequest2 = new CargoRequest();
        assertThat(cargoRequest1).isNotEqualTo(cargoRequest2);

        cargoRequest2.setId(cargoRequest1.getId());
        assertThat(cargoRequest1).isEqualTo(cargoRequest2);

        cargoRequest2 = getCargoRequestSample2();
        assertThat(cargoRequest1).isNotEqualTo(cargoRequest2);
    }

    @Test
    void itemsTest() {
        CargoRequest cargoRequest = getCargoRequestRandomSampleGenerator();
        CargoRequestItem cargoRequestItemBack = getCargoRequestItemRandomSampleGenerator();

        cargoRequest.addItems(cargoRequestItemBack);
        assertThat(cargoRequest.getItems()).containsOnly(cargoRequestItemBack);
        assertThat(cargoRequestItemBack.getCargoRequest()).isEqualTo(cargoRequest);

        cargoRequest.removeItems(cargoRequestItemBack);
        assertThat(cargoRequest.getItems()).doesNotContain(cargoRequestItemBack);
        assertThat(cargoRequestItemBack.getCargoRequest()).isNull();

        cargoRequest.items(new HashSet<>(Set.of(cargoRequestItemBack)));
        assertThat(cargoRequest.getItems()).containsOnly(cargoRequestItemBack);
        assertThat(cargoRequestItemBack.getCargoRequest()).isEqualTo(cargoRequest);

        cargoRequest.setItems(new HashSet<>());
        assertThat(cargoRequest.getItems()).doesNotContain(cargoRequestItemBack);
        assertThat(cargoRequestItemBack.getCargoRequest()).isNull();
    }

    @Test
    void fromCountryTest() {
        CargoRequest cargoRequest = getCargoRequestRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        cargoRequest.setFromCountry(countryBack);
        assertThat(cargoRequest.getFromCountry()).isEqualTo(countryBack);

        cargoRequest.fromCountry(null);
        assertThat(cargoRequest.getFromCountry()).isNull();
    }

    @Test
    void toCountryTest() {
        CargoRequest cargoRequest = getCargoRequestRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        cargoRequest.setToCountry(countryBack);
        assertThat(cargoRequest.getToCountry()).isEqualTo(countryBack);

        cargoRequest.toCountry(null);
        assertThat(cargoRequest.getToCountry()).isNull();
    }

    @Test
    void fromStateTest() {
        CargoRequest cargoRequest = getCargoRequestRandomSampleGenerator();
        StateProvince stateProvinceBack = getStateProvinceRandomSampleGenerator();

        cargoRequest.setFromState(stateProvinceBack);
        assertThat(cargoRequest.getFromState()).isEqualTo(stateProvinceBack);

        cargoRequest.fromState(null);
        assertThat(cargoRequest.getFromState()).isNull();
    }

    @Test
    void toStateTest() {
        CargoRequest cargoRequest = getCargoRequestRandomSampleGenerator();
        StateProvince stateProvinceBack = getStateProvinceRandomSampleGenerator();

        cargoRequest.setToState(stateProvinceBack);
        assertThat(cargoRequest.getToState()).isEqualTo(stateProvinceBack);

        cargoRequest.toState(null);
        assertThat(cargoRequest.getToState()).isNull();
    }
}

package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.CountryTestSamples.*;
import static ai.yarmook.shipperfinder.domain.StateProvinceTestSamples.*;
import static ai.yarmook.shipperfinder.domain.TripItemTestSamples.*;
import static ai.yarmook.shipperfinder.domain.TripTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TripTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trip.class);
        Trip trip1 = getTripSample1();
        Trip trip2 = new Trip();
        assertThat(trip1).isNotEqualTo(trip2);

        trip2.setId(trip1.getId());
        assertThat(trip1).isEqualTo(trip2);

        trip2 = getTripSample2();
        assertThat(trip1).isNotEqualTo(trip2);
    }

    @Test
    void itemsTest() {
        Trip trip = getTripRandomSampleGenerator();
        TripItem tripItemBack = getTripItemRandomSampleGenerator();

        trip.addItems(tripItemBack);
        assertThat(trip.getItems()).containsOnly(tripItemBack);
        assertThat(tripItemBack.getTrip()).isEqualTo(trip);

        trip.removeItems(tripItemBack);
        assertThat(trip.getItems()).doesNotContain(tripItemBack);
        assertThat(tripItemBack.getTrip()).isNull();

        trip.items(new HashSet<>(Set.of(tripItemBack)));
        assertThat(trip.getItems()).containsOnly(tripItemBack);
        assertThat(tripItemBack.getTrip()).isEqualTo(trip);

        trip.setItems(new HashSet<>());
        assertThat(trip.getItems()).doesNotContain(tripItemBack);
        assertThat(tripItemBack.getTrip()).isNull();
    }

    @Test
    void fromCountryTest() {
        Trip trip = getTripRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        trip.setFromCountry(countryBack);
        assertThat(trip.getFromCountry()).isEqualTo(countryBack);

        trip.fromCountry(null);
        assertThat(trip.getFromCountry()).isNull();
    }

    @Test
    void toCountryTest() {
        Trip trip = getTripRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        trip.setToCountry(countryBack);
        assertThat(trip.getToCountry()).isEqualTo(countryBack);

        trip.toCountry(null);
        assertThat(trip.getToCountry()).isNull();
    }

    @Test
    void fromStateTest() {
        Trip trip = getTripRandomSampleGenerator();
        StateProvince stateProvinceBack = getStateProvinceRandomSampleGenerator();

        trip.setFromState(stateProvinceBack);
        assertThat(trip.getFromState()).isEqualTo(stateProvinceBack);

        trip.fromState(null);
        assertThat(trip.getFromState()).isNull();
    }

    @Test
    void toStateTest() {
        Trip trip = getTripRandomSampleGenerator();
        StateProvince stateProvinceBack = getStateProvinceRandomSampleGenerator();

        trip.setToState(stateProvinceBack);
        assertThat(trip.getToState()).isEqualTo(stateProvinceBack);

        trip.toState(null);
        assertThat(trip.getToState()).isNull();
    }
}

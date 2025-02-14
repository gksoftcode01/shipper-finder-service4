package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.CargoRequestItemTestSamples.*;
import static ai.yarmook.shipperfinder.domain.TagTestSamples.*;
import static ai.yarmook.shipperfinder.domain.TripItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void tripItemTest() {
        Tag tag = getTagRandomSampleGenerator();
        TripItem tripItemBack = getTripItemRandomSampleGenerator();

        tag.addTripItem(tripItemBack);
        assertThat(tag.getTripItems()).containsOnly(tripItemBack);
        assertThat(tripItemBack.getTags()).containsOnly(tag);

        tag.removeTripItem(tripItemBack);
        assertThat(tag.getTripItems()).doesNotContain(tripItemBack);
        assertThat(tripItemBack.getTags()).doesNotContain(tag);

        tag.tripItems(new HashSet<>(Set.of(tripItemBack)));
        assertThat(tag.getTripItems()).containsOnly(tripItemBack);
        assertThat(tripItemBack.getTags()).containsOnly(tag);

        tag.setTripItems(new HashSet<>());
        assertThat(tag.getTripItems()).doesNotContain(tripItemBack);
        assertThat(tripItemBack.getTags()).doesNotContain(tag);
    }

    @Test
    void cargoRequestItemTest() {
        Tag tag = getTagRandomSampleGenerator();
        CargoRequestItem cargoRequestItemBack = getCargoRequestItemRandomSampleGenerator();

        tag.addCargoRequestItem(cargoRequestItemBack);
        assertThat(tag.getCargoRequestItems()).containsOnly(cargoRequestItemBack);
        assertThat(cargoRequestItemBack.getTags()).containsOnly(tag);

        tag.removeCargoRequestItem(cargoRequestItemBack);
        assertThat(tag.getCargoRequestItems()).doesNotContain(cargoRequestItemBack);
        assertThat(cargoRequestItemBack.getTags()).doesNotContain(tag);

        tag.cargoRequestItems(new HashSet<>(Set.of(cargoRequestItemBack)));
        assertThat(tag.getCargoRequestItems()).containsOnly(cargoRequestItemBack);
        assertThat(cargoRequestItemBack.getTags()).containsOnly(tag);

        tag.setCargoRequestItems(new HashSet<>());
        assertThat(tag.getCargoRequestItems()).doesNotContain(cargoRequestItemBack);
        assertThat(cargoRequestItemBack.getTags()).doesNotContain(tag);
    }
}

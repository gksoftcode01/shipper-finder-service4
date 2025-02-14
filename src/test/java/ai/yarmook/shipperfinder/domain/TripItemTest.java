package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.ItemTestSamples.*;
import static ai.yarmook.shipperfinder.domain.TagTestSamples.*;
import static ai.yarmook.shipperfinder.domain.TripItemTestSamples.*;
import static ai.yarmook.shipperfinder.domain.TripTestSamples.*;
import static ai.yarmook.shipperfinder.domain.UnitTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TripItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripItem.class);
        TripItem tripItem1 = getTripItemSample1();
        TripItem tripItem2 = new TripItem();
        assertThat(tripItem1).isNotEqualTo(tripItem2);

        tripItem2.setId(tripItem1.getId());
        assertThat(tripItem1).isEqualTo(tripItem2);

        tripItem2 = getTripItemSample2();
        assertThat(tripItem1).isNotEqualTo(tripItem2);
    }

    @Test
    void itemTest() {
        TripItem tripItem = getTripItemRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        tripItem.setItem(itemBack);
        assertThat(tripItem.getItem()).isEqualTo(itemBack);

        tripItem.item(null);
        assertThat(tripItem.getItem()).isNull();
    }

    @Test
    void unitTest() {
        TripItem tripItem = getTripItemRandomSampleGenerator();
        Unit unitBack = getUnitRandomSampleGenerator();

        tripItem.setUnit(unitBack);
        assertThat(tripItem.getUnit()).isEqualTo(unitBack);

        tripItem.unit(null);
        assertThat(tripItem.getUnit()).isNull();
    }

    @Test
    void tagTest() {
        TripItem tripItem = getTripItemRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        tripItem.addTag(tagBack);
        assertThat(tripItem.getTags()).containsOnly(tagBack);

        tripItem.removeTag(tagBack);
        assertThat(tripItem.getTags()).doesNotContain(tagBack);

        tripItem.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(tripItem.getTags()).containsOnly(tagBack);

        tripItem.setTags(new HashSet<>());
        assertThat(tripItem.getTags()).doesNotContain(tagBack);
    }

    @Test
    void tripTest() {
        TripItem tripItem = getTripItemRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        tripItem.setTrip(tripBack);
        assertThat(tripItem.getTrip()).isEqualTo(tripBack);

        tripItem.trip(null);
        assertThat(tripItem.getTrip()).isNull();
    }
}

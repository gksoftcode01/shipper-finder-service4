package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.CargoRequestItemTestSamples.*;
import static ai.yarmook.shipperfinder.domain.CargoRequestTestSamples.*;
import static ai.yarmook.shipperfinder.domain.ItemTestSamples.*;
import static ai.yarmook.shipperfinder.domain.TagTestSamples.*;
import static ai.yarmook.shipperfinder.domain.UnitTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CargoRequestItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoRequestItem.class);
        CargoRequestItem cargoRequestItem1 = getCargoRequestItemSample1();
        CargoRequestItem cargoRequestItem2 = new CargoRequestItem();
        assertThat(cargoRequestItem1).isNotEqualTo(cargoRequestItem2);

        cargoRequestItem2.setId(cargoRequestItem1.getId());
        assertThat(cargoRequestItem1).isEqualTo(cargoRequestItem2);

        cargoRequestItem2 = getCargoRequestItemSample2();
        assertThat(cargoRequestItem1).isNotEqualTo(cargoRequestItem2);
    }

    @Test
    void itemTest() {
        CargoRequestItem cargoRequestItem = getCargoRequestItemRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        cargoRequestItem.setItem(itemBack);
        assertThat(cargoRequestItem.getItem()).isEqualTo(itemBack);

        cargoRequestItem.item(null);
        assertThat(cargoRequestItem.getItem()).isNull();
    }

    @Test
    void unitTest() {
        CargoRequestItem cargoRequestItem = getCargoRequestItemRandomSampleGenerator();
        Unit unitBack = getUnitRandomSampleGenerator();

        cargoRequestItem.setUnit(unitBack);
        assertThat(cargoRequestItem.getUnit()).isEqualTo(unitBack);

        cargoRequestItem.unit(null);
        assertThat(cargoRequestItem.getUnit()).isNull();
    }

    @Test
    void tagTest() {
        CargoRequestItem cargoRequestItem = getCargoRequestItemRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        cargoRequestItem.addTag(tagBack);
        assertThat(cargoRequestItem.getTags()).containsOnly(tagBack);

        cargoRequestItem.removeTag(tagBack);
        assertThat(cargoRequestItem.getTags()).doesNotContain(tagBack);

        cargoRequestItem.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(cargoRequestItem.getTags()).containsOnly(tagBack);

        cargoRequestItem.setTags(new HashSet<>());
        assertThat(cargoRequestItem.getTags()).doesNotContain(tagBack);
    }

    @Test
    void cargoRequestTest() {
        CargoRequestItem cargoRequestItem = getCargoRequestItemRandomSampleGenerator();
        CargoRequest cargoRequestBack = getCargoRequestRandomSampleGenerator();

        cargoRequestItem.setCargoRequest(cargoRequestBack);
        assertThat(cargoRequestItem.getCargoRequest()).isEqualTo(cargoRequestBack);

        cargoRequestItem.cargoRequest(null);
        assertThat(cargoRequestItem.getCargoRequest()).isNull();
    }
}

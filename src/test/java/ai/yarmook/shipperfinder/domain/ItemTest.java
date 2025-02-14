package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.ItemTestSamples.*;
import static ai.yarmook.shipperfinder.domain.ItemTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = getItemSample1();
        Item item2 = new Item();
        assertThat(item1).isNotEqualTo(item2);

        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);

        item2 = getItemSample2();
        assertThat(item1).isNotEqualTo(item2);
    }

    @Test
    void itemTypeTest() {
        Item item = getItemRandomSampleGenerator();
        ItemType itemTypeBack = getItemTypeRandomSampleGenerator();

        item.setItemType(itemTypeBack);
        assertThat(item.getItemType()).isEqualTo(itemTypeBack);

        item.itemType(null);
        assertThat(item.getItemType()).isNull();
    }
}

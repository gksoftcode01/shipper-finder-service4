package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.ItemTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemType.class);
        ItemType itemType1 = getItemTypeSample1();
        ItemType itemType2 = new ItemType();
        assertThat(itemType1).isNotEqualTo(itemType2);

        itemType2.setId(itemType1.getId());
        assertThat(itemType1).isEqualTo(itemType2);

        itemType2 = getItemTypeSample2();
        assertThat(itemType1).isNotEqualTo(itemType2);
    }
}

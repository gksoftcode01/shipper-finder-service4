package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemTypeDTO.class);
        ItemTypeDTO itemTypeDTO1 = new ItemTypeDTO();
        itemTypeDTO1.setId(1L);
        ItemTypeDTO itemTypeDTO2 = new ItemTypeDTO();
        assertThat(itemTypeDTO1).isNotEqualTo(itemTypeDTO2);
        itemTypeDTO2.setId(itemTypeDTO1.getId());
        assertThat(itemTypeDTO1).isEqualTo(itemTypeDTO2);
        itemTypeDTO2.setId(2L);
        assertThat(itemTypeDTO1).isNotEqualTo(itemTypeDTO2);
        itemTypeDTO1.setId(null);
        assertThat(itemTypeDTO1).isNotEqualTo(itemTypeDTO2);
    }
}

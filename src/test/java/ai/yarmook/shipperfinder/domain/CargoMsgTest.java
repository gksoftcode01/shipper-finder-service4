package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.CargoMsgTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoMsgTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoMsg.class);
        CargoMsg cargoMsg1 = getCargoMsgSample1();
        CargoMsg cargoMsg2 = new CargoMsg();
        assertThat(cargoMsg1).isNotEqualTo(cargoMsg2);

        cargoMsg2.setId(cargoMsg1.getId());
        assertThat(cargoMsg1).isEqualTo(cargoMsg2);

        cargoMsg2 = getCargoMsgSample2();
        assertThat(cargoMsg1).isNotEqualTo(cargoMsg2);
    }
}

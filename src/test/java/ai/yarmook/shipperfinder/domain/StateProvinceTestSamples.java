package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StateProvinceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StateProvince getStateProvinceSample1() {
        return new StateProvince().id(1L).name("name1").localName("localName1").isoCode("isoCode1");
    }

    public static StateProvince getStateProvinceSample2() {
        return new StateProvince().id(2L).name("name2").localName("localName2").isoCode("isoCode2");
    }

    public static StateProvince getStateProvinceRandomSampleGenerator() {
        return new StateProvince()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .localName(UUID.randomUUID().toString())
            .isoCode(UUID.randomUUID().toString());
    }
}

package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CargoRequestItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CargoRequestItem getCargoRequestItemSample1() {
        return new CargoRequestItem().id(1L).maxQty(1L).photoUrl("photoUrl1");
    }

    public static CargoRequestItem getCargoRequestItemSample2() {
        return new CargoRequestItem().id(2L).maxQty(2L).photoUrl("photoUrl2");
    }

    public static CargoRequestItem getCargoRequestItemRandomSampleGenerator() {
        return new CargoRequestItem()
            .id(longCount.incrementAndGet())
            .maxQty(longCount.incrementAndGet())
            .photoUrl(UUID.randomUUID().toString());
    }
}

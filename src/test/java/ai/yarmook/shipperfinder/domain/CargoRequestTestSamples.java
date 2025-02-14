package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CargoRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CargoRequest getCargoRequestSample1() {
        return new CargoRequest()
            .id(1L)
            .createdByEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .takenByEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .encId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static CargoRequest getCargoRequestSample2() {
        return new CargoRequest()
            .id(2L)
            .createdByEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .takenByEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .encId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static CargoRequest getCargoRequestRandomSampleGenerator() {
        return new CargoRequest()
            .id(longCount.incrementAndGet())
            .createdByEncId(UUID.randomUUID())
            .takenByEncId(UUID.randomUUID())
            .encId(UUID.randomUUID());
    }
}

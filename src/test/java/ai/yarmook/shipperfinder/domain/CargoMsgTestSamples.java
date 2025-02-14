package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CargoMsgTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CargoMsg getCargoMsgSample1() {
        return new CargoMsg()
            .id(1L)
            .msg("msg1")
            .fromUserEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .toUserEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .cargoRequestId(1L);
    }

    public static CargoMsg getCargoMsgSample2() {
        return new CargoMsg()
            .id(2L)
            .msg("msg2")
            .fromUserEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .toUserEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .cargoRequestId(2L);
    }

    public static CargoMsg getCargoMsgRandomSampleGenerator() {
        return new CargoMsg()
            .id(longCount.incrementAndGet())
            .msg(UUID.randomUUID().toString())
            .fromUserEncId(UUID.randomUUID())
            .toUserEncId(UUID.randomUUID())
            .cargoRequestId(longCount.incrementAndGet());
    }
}

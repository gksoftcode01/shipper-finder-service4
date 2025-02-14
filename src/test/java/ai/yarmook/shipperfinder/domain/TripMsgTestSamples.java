package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TripMsgTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TripMsg getTripMsgSample1() {
        return new TripMsg()
            .id(1L)
            .msg("msg1")
            .fromUserEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .toUserEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .tripId(1L);
    }

    public static TripMsg getTripMsgSample2() {
        return new TripMsg()
            .id(2L)
            .msg("msg2")
            .fromUserEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .toUserEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .tripId(2L);
    }

    public static TripMsg getTripMsgRandomSampleGenerator() {
        return new TripMsg()
            .id(longCount.incrementAndGet())
            .msg(UUID.randomUUID().toString())
            .fromUserEncId(UUID.randomUUID())
            .toUserEncId(UUID.randomUUID())
            .tripId(longCount.incrementAndGet());
    }
}

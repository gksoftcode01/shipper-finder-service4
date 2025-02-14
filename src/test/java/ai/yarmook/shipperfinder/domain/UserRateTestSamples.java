package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserRateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserRate getUserRateSample1() {
        return new UserRate()
            .id(1L)
            .rate(1L)
            .note("note1")
            .ratedByEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .ratedEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static UserRate getUserRateSample2() {
        return new UserRate()
            .id(2L)
            .rate(2L)
            .note("note2")
            .ratedByEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .ratedEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static UserRate getUserRateRandomSampleGenerator() {
        return new UserRate()
            .id(longCount.incrementAndGet())
            .rate(longCount.incrementAndGet())
            .note(UUID.randomUUID().toString())
            .ratedByEncId(UUID.randomUUID())
            .ratedEncId(UUID.randomUUID());
    }
}

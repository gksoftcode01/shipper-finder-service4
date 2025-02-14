package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShowNumberHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ShowNumberHistory getShowNumberHistorySample1() {
        return new ShowNumberHistory()
            .id(1L)
            .actionByEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .entityType(1)
            .entityEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static ShowNumberHistory getShowNumberHistorySample2() {
        return new ShowNumberHistory()
            .id(2L)
            .actionByEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .entityType(2)
            .entityEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static ShowNumberHistory getShowNumberHistoryRandomSampleGenerator() {
        return new ShowNumberHistory()
            .id(longCount.incrementAndGet())
            .actionByEncId(UUID.randomUUID())
            .entityType(intCount.incrementAndGet())
            .entityEncId(UUID.randomUUID());
    }
}

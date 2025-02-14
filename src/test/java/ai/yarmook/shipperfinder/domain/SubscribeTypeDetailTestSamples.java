package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubscribeTypeDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscribeTypeDetail getSubscribeTypeDetailSample1() {
        return new SubscribeTypeDetail().id(1L).maxTrip(1).maxItems(1).maxRequest(1).maxNumberView(1);
    }

    public static SubscribeTypeDetail getSubscribeTypeDetailSample2() {
        return new SubscribeTypeDetail().id(2L).maxTrip(2).maxItems(2).maxRequest(2).maxNumberView(2);
    }

    public static SubscribeTypeDetail getSubscribeTypeDetailRandomSampleGenerator() {
        return new SubscribeTypeDetail()
            .id(longCount.incrementAndGet())
            .maxTrip(intCount.incrementAndGet())
            .maxItems(intCount.incrementAndGet())
            .maxRequest(intCount.incrementAndGet())
            .maxNumberView(intCount.incrementAndGet());
    }
}

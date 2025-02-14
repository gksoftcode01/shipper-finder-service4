package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TripItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TripItem getTripItemSample1() {
        return new TripItem().id(1L).maxQty(1L);
    }

    public static TripItem getTripItemSample2() {
        return new TripItem().id(2L).maxQty(2L);
    }

    public static TripItem getTripItemRandomSampleGenerator() {
        return new TripItem().id(longCount.incrementAndGet()).maxQty(longCount.incrementAndGet());
    }
}

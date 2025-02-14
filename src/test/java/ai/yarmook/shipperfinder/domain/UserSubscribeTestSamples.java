package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserSubscribeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserSubscribe getUserSubscribeSample1() {
        return new UserSubscribe().id(1L).subscribedUserEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static UserSubscribe getUserSubscribeSample2() {
        return new UserSubscribe().id(2L).subscribedUserEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static UserSubscribe getUserSubscribeRandomSampleGenerator() {
        return new UserSubscribe().id(longCount.incrementAndGet()).subscribedUserEncId(UUID.randomUUID());
    }
}

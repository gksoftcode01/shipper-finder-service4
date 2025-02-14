package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserDeviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppUserDevice getAppUserDeviceSample1() {
        return new AppUserDevice()
            .id(1L)
            .deviceCode("deviceCode1")
            .notificationToken("notificationToken1")
            .userEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static AppUserDevice getAppUserDeviceSample2() {
        return new AppUserDevice()
            .id(2L)
            .deviceCode("deviceCode2")
            .notificationToken("notificationToken2")
            .userEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static AppUserDevice getAppUserDeviceRandomSampleGenerator() {
        return new AppUserDevice()
            .id(longCount.incrementAndGet())
            .deviceCode(UUID.randomUUID().toString())
            .notificationToken(UUID.randomUUID().toString())
            .userEncId(UUID.randomUUID());
    }
}

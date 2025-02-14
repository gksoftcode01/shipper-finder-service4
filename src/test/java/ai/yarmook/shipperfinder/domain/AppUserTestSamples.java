package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser()
            .id(1L)
            .phoneNumber("phoneNumber1")
            .mobileNumber("mobileNumber1")
            .fullName("fullName1")
            .userId(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .encId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static AppUser getAppUserSample2() {
        return new AppUser()
            .id(2L)
            .phoneNumber("phoneNumber2")
            .mobileNumber("mobileNumber2")
            .fullName("fullName2")
            .userId(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .encId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(longCount.incrementAndGet())
            .phoneNumber(UUID.randomUUID().toString())
            .mobileNumber(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .userId(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .encId(UUID.randomUUID());
    }
}

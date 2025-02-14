package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubscribeTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubscribeType getSubscribeTypeSample1() {
        return new SubscribeType()
            .id(1L)
            .nameEn("nameEn1")
            .nameAr("nameAr1")
            .nameFr("nameFr1")
            .nameDe("nameDe1")
            .nameUrdu("nameUrdu1")
            .detailsEn("detailsEn1")
            .detailsAr("detailsAr1")
            .detailsFr("detailsFr1")
            .detailsDe("detailsDe1")
            .detailsUrdu("detailsUrdu1");
    }

    public static SubscribeType getSubscribeTypeSample2() {
        return new SubscribeType()
            .id(2L)
            .nameEn("nameEn2")
            .nameAr("nameAr2")
            .nameFr("nameFr2")
            .nameDe("nameDe2")
            .nameUrdu("nameUrdu2")
            .detailsEn("detailsEn2")
            .detailsAr("detailsAr2")
            .detailsFr("detailsFr2")
            .detailsDe("detailsDe2")
            .detailsUrdu("detailsUrdu2");
    }

    public static SubscribeType getSubscribeTypeRandomSampleGenerator() {
        return new SubscribeType()
            .id(longCount.incrementAndGet())
            .nameEn(UUID.randomUUID().toString())
            .nameAr(UUID.randomUUID().toString())
            .nameFr(UUID.randomUUID().toString())
            .nameDe(UUID.randomUUID().toString())
            .nameUrdu(UUID.randomUUID().toString())
            .detailsEn(UUID.randomUUID().toString())
            .detailsAr(UUID.randomUUID().toString())
            .detailsFr(UUID.randomUUID().toString())
            .detailsDe(UUID.randomUUID().toString())
            .detailsUrdu(UUID.randomUUID().toString());
    }
}

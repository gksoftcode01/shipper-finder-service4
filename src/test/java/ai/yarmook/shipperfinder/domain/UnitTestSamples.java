package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UnitTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Unit getUnitSample1() {
        return new Unit()
            .id(1L)
            .name("name1")
            .nameEn("nameEn1")
            .nameAr("nameAr1")
            .nameFr("nameFr1")
            .nameDe("nameDe1")
            .nameUrdu("nameUrdu1");
    }

    public static Unit getUnitSample2() {
        return new Unit()
            .id(2L)
            .name("name2")
            .nameEn("nameEn2")
            .nameAr("nameAr2")
            .nameFr("nameFr2")
            .nameDe("nameDe2")
            .nameUrdu("nameUrdu2");
    }

    public static Unit getUnitRandomSampleGenerator() {
        return new Unit()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .nameEn(UUID.randomUUID().toString())
            .nameAr(UUID.randomUUID().toString())
            .nameFr(UUID.randomUUID().toString())
            .nameDe(UUID.randomUUID().toString())
            .nameUrdu(UUID.randomUUID().toString());
    }
}

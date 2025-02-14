package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LanguagesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Languages getLanguagesSample1() {
        return new Languages().id(1L).name("name1");
    }

    public static Languages getLanguagesSample2() {
        return new Languages().id(2L).name("name2");
    }

    public static Languages getLanguagesRandomSampleGenerator() {
        return new Languages().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}

package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CountryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Country getCountrySample1() {
        return new Country()
            .id(1L)
            .name("name1")
            .localName("localName1")
            .iso2("iso21")
            .iso3("iso31")
            .numericCode("numericCode1")
            .phoneCode("phoneCode1")
            .currency("currency1")
            .currencyName("currencyName1")
            .currencySymbol("currencySymbol1")
            .emoji("emoji1")
            .emojiU("emojiU1");
    }

    public static Country getCountrySample2() {
        return new Country()
            .id(2L)
            .name("name2")
            .localName("localName2")
            .iso2("iso22")
            .iso3("iso32")
            .numericCode("numericCode2")
            .phoneCode("phoneCode2")
            .currency("currency2")
            .currencyName("currencyName2")
            .currencySymbol("currencySymbol2")
            .emoji("emoji2")
            .emojiU("emojiU2");
    }

    public static Country getCountryRandomSampleGenerator() {
        return new Country()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .localName(UUID.randomUUID().toString())
            .iso2(UUID.randomUUID().toString())
            .iso3(UUID.randomUUID().toString())
            .numericCode(UUID.randomUUID().toString())
            .phoneCode(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .currencyName(UUID.randomUUID().toString())
            .currencySymbol(UUID.randomUUID().toString())
            .emoji(UUID.randomUUID().toString())
            .emojiU(UUID.randomUUID().toString());
    }
}

package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CountryCriteriaTest {

    @Test
    void newCountryCriteriaHasAllFiltersNullTest() {
        var countryCriteria = new CountryCriteria();
        assertThat(countryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void countryCriteriaFluentMethodsCreatesFiltersTest() {
        var countryCriteria = new CountryCriteria();

        setAllFilters(countryCriteria);

        assertThat(countryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void countryCriteriaCopyCreatesNullFilterTest() {
        var countryCriteria = new CountryCriteria();
        var copy = countryCriteria.copy();

        assertThat(countryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(countryCriteria)
        );
    }

    @Test
    void countryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var countryCriteria = new CountryCriteria();
        setAllFilters(countryCriteria);

        var copy = countryCriteria.copy();

        assertThat(countryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(countryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var countryCriteria = new CountryCriteria();

        assertThat(countryCriteria).hasToString("CountryCriteria{}");
    }

    private static void setAllFilters(CountryCriteria countryCriteria) {
        countryCriteria.id();
        countryCriteria.name();
        countryCriteria.localName();
        countryCriteria.iso2();
        countryCriteria.iso3();
        countryCriteria.numericCode();
        countryCriteria.phoneCode();
        countryCriteria.currency();
        countryCriteria.currencyName();
        countryCriteria.currencySymbol();
        countryCriteria.emoji();
        countryCriteria.emojiU();
        countryCriteria.stateProvinceId();
        countryCriteria.distinct();
    }

    private static Condition<CountryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getLocalName()) &&
                condition.apply(criteria.getIso2()) &&
                condition.apply(criteria.getIso3()) &&
                condition.apply(criteria.getNumericCode()) &&
                condition.apply(criteria.getPhoneCode()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getCurrencyName()) &&
                condition.apply(criteria.getCurrencySymbol()) &&
                condition.apply(criteria.getEmoji()) &&
                condition.apply(criteria.getEmojiU()) &&
                condition.apply(criteria.getStateProvinceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CountryCriteria> copyFiltersAre(CountryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getLocalName(), copy.getLocalName()) &&
                condition.apply(criteria.getIso2(), copy.getIso2()) &&
                condition.apply(criteria.getIso3(), copy.getIso3()) &&
                condition.apply(criteria.getNumericCode(), copy.getNumericCode()) &&
                condition.apply(criteria.getPhoneCode(), copy.getPhoneCode()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getCurrencyName(), copy.getCurrencyName()) &&
                condition.apply(criteria.getCurrencySymbol(), copy.getCurrencySymbol()) &&
                condition.apply(criteria.getEmoji(), copy.getEmoji()) &&
                condition.apply(criteria.getEmojiU(), copy.getEmojiU()) &&
                condition.apply(criteria.getStateProvinceId(), copy.getStateProvinceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

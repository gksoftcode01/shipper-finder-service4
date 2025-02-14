package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StateProvinceCriteriaTest {

    @Test
    void newStateProvinceCriteriaHasAllFiltersNullTest() {
        var stateProvinceCriteria = new StateProvinceCriteria();
        assertThat(stateProvinceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void stateProvinceCriteriaFluentMethodsCreatesFiltersTest() {
        var stateProvinceCriteria = new StateProvinceCriteria();

        setAllFilters(stateProvinceCriteria);

        assertThat(stateProvinceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void stateProvinceCriteriaCopyCreatesNullFilterTest() {
        var stateProvinceCriteria = new StateProvinceCriteria();
        var copy = stateProvinceCriteria.copy();

        assertThat(stateProvinceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(stateProvinceCriteria)
        );
    }

    @Test
    void stateProvinceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var stateProvinceCriteria = new StateProvinceCriteria();
        setAllFilters(stateProvinceCriteria);

        var copy = stateProvinceCriteria.copy();

        assertThat(stateProvinceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(stateProvinceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var stateProvinceCriteria = new StateProvinceCriteria();

        assertThat(stateProvinceCriteria).hasToString("StateProvinceCriteria{}");
    }

    private static void setAllFilters(StateProvinceCriteria stateProvinceCriteria) {
        stateProvinceCriteria.id();
        stateProvinceCriteria.name();
        stateProvinceCriteria.localName();
        stateProvinceCriteria.isoCode();
        stateProvinceCriteria.countryId();
        stateProvinceCriteria.distinct();
    }

    private static Condition<StateProvinceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getLocalName()) &&
                condition.apply(criteria.getIsoCode()) &&
                condition.apply(criteria.getCountryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StateProvinceCriteria> copyFiltersAre(
        StateProvinceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getLocalName(), copy.getLocalName()) &&
                condition.apply(criteria.getIsoCode(), copy.getIsoCode()) &&
                condition.apply(criteria.getCountryId(), copy.getCountryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

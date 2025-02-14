package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CargoRequestCriteriaTest {

    @Test
    void newCargoRequestCriteriaHasAllFiltersNullTest() {
        var cargoRequestCriteria = new CargoRequestCriteria();
        assertThat(cargoRequestCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cargoRequestCriteriaFluentMethodsCreatesFiltersTest() {
        var cargoRequestCriteria = new CargoRequestCriteria();

        setAllFilters(cargoRequestCriteria);

        assertThat(cargoRequestCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cargoRequestCriteriaCopyCreatesNullFilterTest() {
        var cargoRequestCriteria = new CargoRequestCriteria();
        var copy = cargoRequestCriteria.copy();

        assertThat(cargoRequestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cargoRequestCriteria)
        );
    }

    @Test
    void cargoRequestCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cargoRequestCriteria = new CargoRequestCriteria();
        setAllFilters(cargoRequestCriteria);

        var copy = cargoRequestCriteria.copy();

        assertThat(cargoRequestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cargoRequestCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cargoRequestCriteria = new CargoRequestCriteria();

        assertThat(cargoRequestCriteria).hasToString("CargoRequestCriteria{}");
    }

    private static void setAllFilters(CargoRequestCriteria cargoRequestCriteria) {
        cargoRequestCriteria.id();
        cargoRequestCriteria.createDate();
        cargoRequestCriteria.validUntil();
        cargoRequestCriteria.status();
        cargoRequestCriteria.isNegotiable();
        cargoRequestCriteria.budget();
        cargoRequestCriteria.createdByEncId();
        cargoRequestCriteria.takenByEncId();
        cargoRequestCriteria.encId();
        cargoRequestCriteria.itemsId();
        cargoRequestCriteria.fromCountryId();
        cargoRequestCriteria.toCountryId();
        cargoRequestCriteria.fromStateId();
        cargoRequestCriteria.toStateId();
        cargoRequestCriteria.distinct();
    }

    private static Condition<CargoRequestCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreateDate()) &&
                condition.apply(criteria.getValidUntil()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getIsNegotiable()) &&
                condition.apply(criteria.getBudget()) &&
                condition.apply(criteria.getCreatedByEncId()) &&
                condition.apply(criteria.getTakenByEncId()) &&
                condition.apply(criteria.getEncId()) &&
                condition.apply(criteria.getItemsId()) &&
                condition.apply(criteria.getFromCountryId()) &&
                condition.apply(criteria.getToCountryId()) &&
                condition.apply(criteria.getFromStateId()) &&
                condition.apply(criteria.getToStateId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CargoRequestCriteria> copyFiltersAre(
        CargoRequestCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreateDate(), copy.getCreateDate()) &&
                condition.apply(criteria.getValidUntil(), copy.getValidUntil()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getIsNegotiable(), copy.getIsNegotiable()) &&
                condition.apply(criteria.getBudget(), copy.getBudget()) &&
                condition.apply(criteria.getCreatedByEncId(), copy.getCreatedByEncId()) &&
                condition.apply(criteria.getTakenByEncId(), copy.getTakenByEncId()) &&
                condition.apply(criteria.getEncId(), copy.getEncId()) &&
                condition.apply(criteria.getItemsId(), copy.getItemsId()) &&
                condition.apply(criteria.getFromCountryId(), copy.getFromCountryId()) &&
                condition.apply(criteria.getToCountryId(), copy.getToCountryId()) &&
                condition.apply(criteria.getFromStateId(), copy.getFromStateId()) &&
                condition.apply(criteria.getToStateId(), copy.getToStateId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

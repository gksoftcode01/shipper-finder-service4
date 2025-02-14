package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CargoRequestItemCriteriaTest {

    @Test
    void newCargoRequestItemCriteriaHasAllFiltersNullTest() {
        var cargoRequestItemCriteria = new CargoRequestItemCriteria();
        assertThat(cargoRequestItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cargoRequestItemCriteriaFluentMethodsCreatesFiltersTest() {
        var cargoRequestItemCriteria = new CargoRequestItemCriteria();

        setAllFilters(cargoRequestItemCriteria);

        assertThat(cargoRequestItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cargoRequestItemCriteriaCopyCreatesNullFilterTest() {
        var cargoRequestItemCriteria = new CargoRequestItemCriteria();
        var copy = cargoRequestItemCriteria.copy();

        assertThat(cargoRequestItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cargoRequestItemCriteria)
        );
    }

    @Test
    void cargoRequestItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cargoRequestItemCriteria = new CargoRequestItemCriteria();
        setAllFilters(cargoRequestItemCriteria);

        var copy = cargoRequestItemCriteria.copy();

        assertThat(cargoRequestItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cargoRequestItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cargoRequestItemCriteria = new CargoRequestItemCriteria();

        assertThat(cargoRequestItemCriteria).hasToString("CargoRequestItemCriteria{}");
    }

    private static void setAllFilters(CargoRequestItemCriteria cargoRequestItemCriteria) {
        cargoRequestItemCriteria.id();
        cargoRequestItemCriteria.maxQty();
        cargoRequestItemCriteria.photoUrl();
        cargoRequestItemCriteria.itemId();
        cargoRequestItemCriteria.unitId();
        cargoRequestItemCriteria.tagId();
        cargoRequestItemCriteria.cargoRequestId();
        cargoRequestItemCriteria.distinct();
    }

    private static Condition<CargoRequestItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMaxQty()) &&
                condition.apply(criteria.getPhotoUrl()) &&
                condition.apply(criteria.getItemId()) &&
                condition.apply(criteria.getUnitId()) &&
                condition.apply(criteria.getTagId()) &&
                condition.apply(criteria.getCargoRequestId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CargoRequestItemCriteria> copyFiltersAre(
        CargoRequestItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMaxQty(), copy.getMaxQty()) &&
                condition.apply(criteria.getPhotoUrl(), copy.getPhotoUrl()) &&
                condition.apply(criteria.getItemId(), copy.getItemId()) &&
                condition.apply(criteria.getUnitId(), copy.getUnitId()) &&
                condition.apply(criteria.getTagId(), copy.getTagId()) &&
                condition.apply(criteria.getCargoRequestId(), copy.getCargoRequestId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

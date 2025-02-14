package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TripItemCriteriaTest {

    @Test
    void newTripItemCriteriaHasAllFiltersNullTest() {
        var tripItemCriteria = new TripItemCriteria();
        assertThat(tripItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tripItemCriteriaFluentMethodsCreatesFiltersTest() {
        var tripItemCriteria = new TripItemCriteria();

        setAllFilters(tripItemCriteria);

        assertThat(tripItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tripItemCriteriaCopyCreatesNullFilterTest() {
        var tripItemCriteria = new TripItemCriteria();
        var copy = tripItemCriteria.copy();

        assertThat(tripItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tripItemCriteria)
        );
    }

    @Test
    void tripItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tripItemCriteria = new TripItemCriteria();
        setAllFilters(tripItemCriteria);

        var copy = tripItemCriteria.copy();

        assertThat(tripItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tripItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tripItemCriteria = new TripItemCriteria();

        assertThat(tripItemCriteria).hasToString("TripItemCriteria{}");
    }

    private static void setAllFilters(TripItemCriteria tripItemCriteria) {
        tripItemCriteria.id();
        tripItemCriteria.itemPrice();
        tripItemCriteria.maxQty();
        tripItemCriteria.itemId();
        tripItemCriteria.unitId();
        tripItemCriteria.tagId();
        tripItemCriteria.tripId();
        tripItemCriteria.distinct();
    }

    private static Condition<TripItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getItemPrice()) &&
                condition.apply(criteria.getMaxQty()) &&
                condition.apply(criteria.getItemId()) &&
                condition.apply(criteria.getUnitId()) &&
                condition.apply(criteria.getTagId()) &&
                condition.apply(criteria.getTripId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TripItemCriteria> copyFiltersAre(TripItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getItemPrice(), copy.getItemPrice()) &&
                condition.apply(criteria.getMaxQty(), copy.getMaxQty()) &&
                condition.apply(criteria.getItemId(), copy.getItemId()) &&
                condition.apply(criteria.getUnitId(), copy.getUnitId()) &&
                condition.apply(criteria.getTagId(), copy.getTagId()) &&
                condition.apply(criteria.getTripId(), copy.getTripId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TripCriteriaTest {

    @Test
    void newTripCriteriaHasAllFiltersNullTest() {
        var tripCriteria = new TripCriteria();
        assertThat(tripCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tripCriteriaFluentMethodsCreatesFiltersTest() {
        var tripCriteria = new TripCriteria();

        setAllFilters(tripCriteria);

        assertThat(tripCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tripCriteriaCopyCreatesNullFilterTest() {
        var tripCriteria = new TripCriteria();
        var copy = tripCriteria.copy();

        assertThat(tripCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tripCriteria)
        );
    }

    @Test
    void tripCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tripCriteria = new TripCriteria();
        setAllFilters(tripCriteria);

        var copy = tripCriteria.copy();

        assertThat(tripCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tripCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tripCriteria = new TripCriteria();

        assertThat(tripCriteria).hasToString("TripCriteria{}");
    }

    private static void setAllFilters(TripCriteria tripCriteria) {
        tripCriteria.id();
        tripCriteria.tripDate();
        tripCriteria.arriveDate();
        tripCriteria.maxWeight();
        tripCriteria.notes();
        tripCriteria.createDate();
        tripCriteria.isNegotiate();
        tripCriteria.status();
        tripCriteria.createdByEncId();
        tripCriteria.encId();
        tripCriteria.itemsId();
        tripCriteria.fromCountryId();
        tripCriteria.toCountryId();
        tripCriteria.fromStateId();
        tripCriteria.toStateId();
        tripCriteria.distinct();
    }

    private static Condition<TripCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTripDate()) &&
                condition.apply(criteria.getArriveDate()) &&
                condition.apply(criteria.getMaxWeight()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getCreateDate()) &&
                condition.apply(criteria.getIsNegotiate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedByEncId()) &&
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

    private static Condition<TripCriteria> copyFiltersAre(TripCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTripDate(), copy.getTripDate()) &&
                condition.apply(criteria.getArriveDate(), copy.getArriveDate()) &&
                condition.apply(criteria.getMaxWeight(), copy.getMaxWeight()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getCreateDate(), copy.getCreateDate()) &&
                condition.apply(criteria.getIsNegotiate(), copy.getIsNegotiate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedByEncId(), copy.getCreatedByEncId()) &&
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

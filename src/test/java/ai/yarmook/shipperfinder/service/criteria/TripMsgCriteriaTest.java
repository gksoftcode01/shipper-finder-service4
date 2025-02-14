package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TripMsgCriteriaTest {

    @Test
    void newTripMsgCriteriaHasAllFiltersNullTest() {
        var tripMsgCriteria = new TripMsgCriteria();
        assertThat(tripMsgCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tripMsgCriteriaFluentMethodsCreatesFiltersTest() {
        var tripMsgCriteria = new TripMsgCriteria();

        setAllFilters(tripMsgCriteria);

        assertThat(tripMsgCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tripMsgCriteriaCopyCreatesNullFilterTest() {
        var tripMsgCriteria = new TripMsgCriteria();
        var copy = tripMsgCriteria.copy();

        assertThat(tripMsgCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tripMsgCriteria)
        );
    }

    @Test
    void tripMsgCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tripMsgCriteria = new TripMsgCriteria();
        setAllFilters(tripMsgCriteria);

        var copy = tripMsgCriteria.copy();

        assertThat(tripMsgCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tripMsgCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tripMsgCriteria = new TripMsgCriteria();

        assertThat(tripMsgCriteria).hasToString("TripMsgCriteria{}");
    }

    private static void setAllFilters(TripMsgCriteria tripMsgCriteria) {
        tripMsgCriteria.id();
        tripMsgCriteria.msg();
        tripMsgCriteria.fromUserEncId();
        tripMsgCriteria.toUserEncId();
        tripMsgCriteria.tripId();
        tripMsgCriteria.distinct();
    }

    private static Condition<TripMsgCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMsg()) &&
                condition.apply(criteria.getFromUserEncId()) &&
                condition.apply(criteria.getToUserEncId()) &&
                condition.apply(criteria.getTripId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TripMsgCriteria> copyFiltersAre(TripMsgCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMsg(), copy.getMsg()) &&
                condition.apply(criteria.getFromUserEncId(), copy.getFromUserEncId()) &&
                condition.apply(criteria.getToUserEncId(), copy.getToUserEncId()) &&
                condition.apply(criteria.getTripId(), copy.getTripId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

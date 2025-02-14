package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserSubscribeCriteriaTest {

    @Test
    void newUserSubscribeCriteriaHasAllFiltersNullTest() {
        var userSubscribeCriteria = new UserSubscribeCriteria();
        assertThat(userSubscribeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userSubscribeCriteriaFluentMethodsCreatesFiltersTest() {
        var userSubscribeCriteria = new UserSubscribeCriteria();

        setAllFilters(userSubscribeCriteria);

        assertThat(userSubscribeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userSubscribeCriteriaCopyCreatesNullFilterTest() {
        var userSubscribeCriteria = new UserSubscribeCriteria();
        var copy = userSubscribeCriteria.copy();

        assertThat(userSubscribeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userSubscribeCriteria)
        );
    }

    @Test
    void userSubscribeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userSubscribeCriteria = new UserSubscribeCriteria();
        setAllFilters(userSubscribeCriteria);

        var copy = userSubscribeCriteria.copy();

        assertThat(userSubscribeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userSubscribeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userSubscribeCriteria = new UserSubscribeCriteria();

        assertThat(userSubscribeCriteria).hasToString("UserSubscribeCriteria{}");
    }

    private static void setAllFilters(UserSubscribeCriteria userSubscribeCriteria) {
        userSubscribeCriteria.id();
        userSubscribeCriteria.fromDate();
        userSubscribeCriteria.toDate();
        userSubscribeCriteria.isActive();
        userSubscribeCriteria.subscribedUserEncId();
        userSubscribeCriteria.subscribeTypeId();
        userSubscribeCriteria.distinct();
    }

    private static Condition<UserSubscribeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getToDate()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getSubscribedUserEncId()) &&
                condition.apply(criteria.getSubscribeTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserSubscribeCriteria> copyFiltersAre(
        UserSubscribeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getToDate(), copy.getToDate()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getSubscribedUserEncId(), copy.getSubscribedUserEncId()) &&
                condition.apply(criteria.getSubscribeTypeId(), copy.getSubscribeTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

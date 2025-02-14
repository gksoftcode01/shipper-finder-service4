package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShowNumberHistoryCriteriaTest {

    @Test
    void newShowNumberHistoryCriteriaHasAllFiltersNullTest() {
        var showNumberHistoryCriteria = new ShowNumberHistoryCriteria();
        assertThat(showNumberHistoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void showNumberHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var showNumberHistoryCriteria = new ShowNumberHistoryCriteria();

        setAllFilters(showNumberHistoryCriteria);

        assertThat(showNumberHistoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void showNumberHistoryCriteriaCopyCreatesNullFilterTest() {
        var showNumberHistoryCriteria = new ShowNumberHistoryCriteria();
        var copy = showNumberHistoryCriteria.copy();

        assertThat(showNumberHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(showNumberHistoryCriteria)
        );
    }

    @Test
    void showNumberHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var showNumberHistoryCriteria = new ShowNumberHistoryCriteria();
        setAllFilters(showNumberHistoryCriteria);

        var copy = showNumberHistoryCriteria.copy();

        assertThat(showNumberHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(showNumberHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var showNumberHistoryCriteria = new ShowNumberHistoryCriteria();

        assertThat(showNumberHistoryCriteria).hasToString("ShowNumberHistoryCriteria{}");
    }

    private static void setAllFilters(ShowNumberHistoryCriteria showNumberHistoryCriteria) {
        showNumberHistoryCriteria.id();
        showNumberHistoryCriteria.createdDate();
        showNumberHistoryCriteria.actionByEncId();
        showNumberHistoryCriteria.entityType();
        showNumberHistoryCriteria.entityEncId();
        showNumberHistoryCriteria.distinct();
    }

    private static Condition<ShowNumberHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getActionByEncId()) &&
                condition.apply(criteria.getEntityType()) &&
                condition.apply(criteria.getEntityEncId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShowNumberHistoryCriteria> copyFiltersAre(
        ShowNumberHistoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getActionByEncId(), copy.getActionByEncId()) &&
                condition.apply(criteria.getEntityType(), copy.getEntityType()) &&
                condition.apply(criteria.getEntityEncId(), copy.getEntityEncId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

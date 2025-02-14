package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SubscribeTypeCriteriaTest {

    @Test
    void newSubscribeTypeCriteriaHasAllFiltersNullTest() {
        var subscribeTypeCriteria = new SubscribeTypeCriteria();
        assertThat(subscribeTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void subscribeTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var subscribeTypeCriteria = new SubscribeTypeCriteria();

        setAllFilters(subscribeTypeCriteria);

        assertThat(subscribeTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void subscribeTypeCriteriaCopyCreatesNullFilterTest() {
        var subscribeTypeCriteria = new SubscribeTypeCriteria();
        var copy = subscribeTypeCriteria.copy();

        assertThat(subscribeTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(subscribeTypeCriteria)
        );
    }

    @Test
    void subscribeTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var subscribeTypeCriteria = new SubscribeTypeCriteria();
        setAllFilters(subscribeTypeCriteria);

        var copy = subscribeTypeCriteria.copy();

        assertThat(subscribeTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(subscribeTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var subscribeTypeCriteria = new SubscribeTypeCriteria();

        assertThat(subscribeTypeCriteria).hasToString("SubscribeTypeCriteria{}");
    }

    private static void setAllFilters(SubscribeTypeCriteria subscribeTypeCriteria) {
        subscribeTypeCriteria.id();
        subscribeTypeCriteria.type();
        subscribeTypeCriteria.nameEn();
        subscribeTypeCriteria.nameAr();
        subscribeTypeCriteria.nameFr();
        subscribeTypeCriteria.nameDe();
        subscribeTypeCriteria.nameUrdu();
        subscribeTypeCriteria.detailsEn();
        subscribeTypeCriteria.detailsAr();
        subscribeTypeCriteria.detailsFr();
        subscribeTypeCriteria.detailsDe();
        subscribeTypeCriteria.detailsUrdu();
        subscribeTypeCriteria.subscribeTypeDetailId();
        subscribeTypeCriteria.distinct();
    }

    private static Condition<SubscribeTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getNameEn()) &&
                condition.apply(criteria.getNameAr()) &&
                condition.apply(criteria.getNameFr()) &&
                condition.apply(criteria.getNameDe()) &&
                condition.apply(criteria.getNameUrdu()) &&
                condition.apply(criteria.getDetailsEn()) &&
                condition.apply(criteria.getDetailsAr()) &&
                condition.apply(criteria.getDetailsFr()) &&
                condition.apply(criteria.getDetailsDe()) &&
                condition.apply(criteria.getDetailsUrdu()) &&
                condition.apply(criteria.getSubscribeTypeDetailId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SubscribeTypeCriteria> copyFiltersAre(
        SubscribeTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getNameEn(), copy.getNameEn()) &&
                condition.apply(criteria.getNameAr(), copy.getNameAr()) &&
                condition.apply(criteria.getNameFr(), copy.getNameFr()) &&
                condition.apply(criteria.getNameDe(), copy.getNameDe()) &&
                condition.apply(criteria.getNameUrdu(), copy.getNameUrdu()) &&
                condition.apply(criteria.getDetailsEn(), copy.getDetailsEn()) &&
                condition.apply(criteria.getDetailsAr(), copy.getDetailsAr()) &&
                condition.apply(criteria.getDetailsFr(), copy.getDetailsFr()) &&
                condition.apply(criteria.getDetailsDe(), copy.getDetailsDe()) &&
                condition.apply(criteria.getDetailsUrdu(), copy.getDetailsUrdu()) &&
                condition.apply(criteria.getSubscribeTypeDetailId(), copy.getSubscribeTypeDetailId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

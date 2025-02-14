package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UnitCriteriaTest {

    @Test
    void newUnitCriteriaHasAllFiltersNullTest() {
        var unitCriteria = new UnitCriteria();
        assertThat(unitCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void unitCriteriaFluentMethodsCreatesFiltersTest() {
        var unitCriteria = new UnitCriteria();

        setAllFilters(unitCriteria);

        assertThat(unitCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void unitCriteriaCopyCreatesNullFilterTest() {
        var unitCriteria = new UnitCriteria();
        var copy = unitCriteria.copy();

        assertThat(unitCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(unitCriteria)
        );
    }

    @Test
    void unitCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var unitCriteria = new UnitCriteria();
        setAllFilters(unitCriteria);

        var copy = unitCriteria.copy();

        assertThat(unitCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(unitCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var unitCriteria = new UnitCriteria();

        assertThat(unitCriteria).hasToString("UnitCriteria{}");
    }

    private static void setAllFilters(UnitCriteria unitCriteria) {
        unitCriteria.id();
        unitCriteria.name();
        unitCriteria.nameEn();
        unitCriteria.nameAr();
        unitCriteria.nameFr();
        unitCriteria.nameDe();
        unitCriteria.nameUrdu();
        unitCriteria.distinct();
    }

    private static Condition<UnitCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNameEn()) &&
                condition.apply(criteria.getNameAr()) &&
                condition.apply(criteria.getNameFr()) &&
                condition.apply(criteria.getNameDe()) &&
                condition.apply(criteria.getNameUrdu()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UnitCriteria> copyFiltersAre(UnitCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNameEn(), copy.getNameEn()) &&
                condition.apply(criteria.getNameAr(), copy.getNameAr()) &&
                condition.apply(criteria.getNameFr(), copy.getNameFr()) &&
                condition.apply(criteria.getNameDe(), copy.getNameDe()) &&
                condition.apply(criteria.getNameUrdu(), copy.getNameUrdu()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

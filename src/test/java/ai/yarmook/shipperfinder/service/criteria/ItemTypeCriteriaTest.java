package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ItemTypeCriteriaTest {

    @Test
    void newItemTypeCriteriaHasAllFiltersNullTest() {
        var itemTypeCriteria = new ItemTypeCriteria();
        assertThat(itemTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void itemTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var itemTypeCriteria = new ItemTypeCriteria();

        setAllFilters(itemTypeCriteria);

        assertThat(itemTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void itemTypeCriteriaCopyCreatesNullFilterTest() {
        var itemTypeCriteria = new ItemTypeCriteria();
        var copy = itemTypeCriteria.copy();

        assertThat(itemTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(itemTypeCriteria)
        );
    }

    @Test
    void itemTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var itemTypeCriteria = new ItemTypeCriteria();
        setAllFilters(itemTypeCriteria);

        var copy = itemTypeCriteria.copy();

        assertThat(itemTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(itemTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var itemTypeCriteria = new ItemTypeCriteria();

        assertThat(itemTypeCriteria).hasToString("ItemTypeCriteria{}");
    }

    private static void setAllFilters(ItemTypeCriteria itemTypeCriteria) {
        itemTypeCriteria.id();
        itemTypeCriteria.name();
        itemTypeCriteria.nameEn();
        itemTypeCriteria.nameAr();
        itemTypeCriteria.nameFr();
        itemTypeCriteria.nameDe();
        itemTypeCriteria.nameUrdu();
        itemTypeCriteria.isActive();
        itemTypeCriteria.distinct();
    }

    private static Condition<ItemTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNameEn()) &&
                condition.apply(criteria.getNameAr()) &&
                condition.apply(criteria.getNameFr()) &&
                condition.apply(criteria.getNameDe()) &&
                condition.apply(criteria.getNameUrdu()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ItemTypeCriteria> copyFiltersAre(ItemTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNameEn(), copy.getNameEn()) &&
                condition.apply(criteria.getNameAr(), copy.getNameAr()) &&
                condition.apply(criteria.getNameFr(), copy.getNameFr()) &&
                condition.apply(criteria.getNameDe(), copy.getNameDe()) &&
                condition.apply(criteria.getNameUrdu(), copy.getNameUrdu()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

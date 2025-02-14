package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ItemCriteriaTest {

    @Test
    void newItemCriteriaHasAllFiltersNullTest() {
        var itemCriteria = new ItemCriteria();
        assertThat(itemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void itemCriteriaFluentMethodsCreatesFiltersTest() {
        var itemCriteria = new ItemCriteria();

        setAllFilters(itemCriteria);

        assertThat(itemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void itemCriteriaCopyCreatesNullFilterTest() {
        var itemCriteria = new ItemCriteria();
        var copy = itemCriteria.copy();

        assertThat(itemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(itemCriteria)
        );
    }

    @Test
    void itemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var itemCriteria = new ItemCriteria();
        setAllFilters(itemCriteria);

        var copy = itemCriteria.copy();

        assertThat(itemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(itemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var itemCriteria = new ItemCriteria();

        assertThat(itemCriteria).hasToString("ItemCriteria{}");
    }

    private static void setAllFilters(ItemCriteria itemCriteria) {
        itemCriteria.id();
        itemCriteria.name();
        itemCriteria.nameEn();
        itemCriteria.nameAr();
        itemCriteria.nameFr();
        itemCriteria.nameDe();
        itemCriteria.nameUrdu();
        itemCriteria.isActive();
        itemCriteria.defaultUOM();
        itemCriteria.itemTypeId();
        itemCriteria.distinct();
    }

    private static Condition<ItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getDefaultUOM()) &&
                condition.apply(criteria.getItemTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ItemCriteria> copyFiltersAre(ItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
                condition.apply(criteria.getDefaultUOM(), copy.getDefaultUOM()) &&
                condition.apply(criteria.getItemTypeId(), copy.getItemTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

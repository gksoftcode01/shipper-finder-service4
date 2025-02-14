package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CargoMsgCriteriaTest {

    @Test
    void newCargoMsgCriteriaHasAllFiltersNullTest() {
        var cargoMsgCriteria = new CargoMsgCriteria();
        assertThat(cargoMsgCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cargoMsgCriteriaFluentMethodsCreatesFiltersTest() {
        var cargoMsgCriteria = new CargoMsgCriteria();

        setAllFilters(cargoMsgCriteria);

        assertThat(cargoMsgCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cargoMsgCriteriaCopyCreatesNullFilterTest() {
        var cargoMsgCriteria = new CargoMsgCriteria();
        var copy = cargoMsgCriteria.copy();

        assertThat(cargoMsgCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cargoMsgCriteria)
        );
    }

    @Test
    void cargoMsgCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cargoMsgCriteria = new CargoMsgCriteria();
        setAllFilters(cargoMsgCriteria);

        var copy = cargoMsgCriteria.copy();

        assertThat(cargoMsgCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cargoMsgCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cargoMsgCriteria = new CargoMsgCriteria();

        assertThat(cargoMsgCriteria).hasToString("CargoMsgCriteria{}");
    }

    private static void setAllFilters(CargoMsgCriteria cargoMsgCriteria) {
        cargoMsgCriteria.id();
        cargoMsgCriteria.msg();
        cargoMsgCriteria.fromUserEncId();
        cargoMsgCriteria.toUserEncId();
        cargoMsgCriteria.cargoRequestId();
        cargoMsgCriteria.distinct();
    }

    private static Condition<CargoMsgCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMsg()) &&
                condition.apply(criteria.getFromUserEncId()) &&
                condition.apply(criteria.getToUserEncId()) &&
                condition.apply(criteria.getCargoRequestId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CargoMsgCriteria> copyFiltersAre(CargoMsgCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMsg(), copy.getMsg()) &&
                condition.apply(criteria.getFromUserEncId(), copy.getFromUserEncId()) &&
                condition.apply(criteria.getToUserEncId(), copy.getToUserEncId()) &&
                condition.apply(criteria.getCargoRequestId(), copy.getCargoRequestId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

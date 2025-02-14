package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AppUserDeviceCriteriaTest {

    @Test
    void newAppUserDeviceCriteriaHasAllFiltersNullTest() {
        var appUserDeviceCriteria = new AppUserDeviceCriteria();
        assertThat(appUserDeviceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void appUserDeviceCriteriaFluentMethodsCreatesFiltersTest() {
        var appUserDeviceCriteria = new AppUserDeviceCriteria();

        setAllFilters(appUserDeviceCriteria);

        assertThat(appUserDeviceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void appUserDeviceCriteriaCopyCreatesNullFilterTest() {
        var appUserDeviceCriteria = new AppUserDeviceCriteria();
        var copy = appUserDeviceCriteria.copy();

        assertThat(appUserDeviceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(appUserDeviceCriteria)
        );
    }

    @Test
    void appUserDeviceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var appUserDeviceCriteria = new AppUserDeviceCriteria();
        setAllFilters(appUserDeviceCriteria);

        var copy = appUserDeviceCriteria.copy();

        assertThat(appUserDeviceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(appUserDeviceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var appUserDeviceCriteria = new AppUserDeviceCriteria();

        assertThat(appUserDeviceCriteria).hasToString("AppUserDeviceCriteria{}");
    }

    private static void setAllFilters(AppUserDeviceCriteria appUserDeviceCriteria) {
        appUserDeviceCriteria.id();
        appUserDeviceCriteria.deviceCode();
        appUserDeviceCriteria.notificationToken();
        appUserDeviceCriteria.lastLogin();
        appUserDeviceCriteria.active();
        appUserDeviceCriteria.userEncId();
        appUserDeviceCriteria.distinct();
    }

    private static Condition<AppUserDeviceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDeviceCode()) &&
                condition.apply(criteria.getNotificationToken()) &&
                condition.apply(criteria.getLastLogin()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getUserEncId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AppUserDeviceCriteria> copyFiltersAre(
        AppUserDeviceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDeviceCode(), copy.getDeviceCode()) &&
                condition.apply(criteria.getNotificationToken(), copy.getNotificationToken()) &&
                condition.apply(criteria.getLastLogin(), copy.getLastLogin()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getUserEncId(), copy.getUserEncId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

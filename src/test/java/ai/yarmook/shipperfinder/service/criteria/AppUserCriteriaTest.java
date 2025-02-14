package ai.yarmook.shipperfinder.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AppUserCriteriaTest {

    @Test
    void newAppUserCriteriaHasAllFiltersNullTest() {
        var appUserCriteria = new AppUserCriteria();
        assertThat(appUserCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void appUserCriteriaFluentMethodsCreatesFiltersTest() {
        var appUserCriteria = new AppUserCriteria();

        setAllFilters(appUserCriteria);

        assertThat(appUserCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void appUserCriteriaCopyCreatesNullFilterTest() {
        var appUserCriteria = new AppUserCriteria();
        var copy = appUserCriteria.copy();

        assertThat(appUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(appUserCriteria)
        );
    }

    @Test
    void appUserCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var appUserCriteria = new AppUserCriteria();
        setAllFilters(appUserCriteria);

        var copy = appUserCriteria.copy();

        assertThat(appUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(appUserCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var appUserCriteria = new AppUserCriteria();

        assertThat(appUserCriteria).hasToString("AppUserCriteria{}");
    }

    private static void setAllFilters(AppUserCriteria appUserCriteria) {
        appUserCriteria.id();
        appUserCriteria.birthDate();
        appUserCriteria.gender();
        appUserCriteria.registerDate();
        appUserCriteria.phoneNumber();
        appUserCriteria.mobileNumber();
        appUserCriteria.fullName();
        appUserCriteria.isVerified();
        appUserCriteria.userId();
        appUserCriteria.firstName();
        appUserCriteria.lastName();
        appUserCriteria.encId();
        appUserCriteria.preferdLanguageId();
        appUserCriteria.locationId();
        appUserCriteria.distinct();
    }

    private static Condition<AppUserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getRegisterDate()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getMobileNumber()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getIsVerified()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getEncId()) &&
                condition.apply(criteria.getPreferdLanguageId()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AppUserCriteria> copyFiltersAre(AppUserCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getRegisterDate(), copy.getRegisterDate()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getMobileNumber(), copy.getMobileNumber()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getIsVerified(), copy.getIsVerified()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getEncId(), copy.getEncId()) &&
                condition.apply(criteria.getPreferdLanguageId(), copy.getPreferdLanguageId()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

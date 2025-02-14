package ai.yarmook.shipperfinder.service.criteria;

import ai.yarmook.shipperfinder.domain.enumeration.GenderType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.AppUser} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.AppUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GenderType
     */
    public static class GenderTypeFilter extends Filter<GenderType> {

        public GenderTypeFilter() {}

        public GenderTypeFilter(GenderTypeFilter filter) {
            super(filter);
        }

        @Override
        public GenderTypeFilter copy() {
            return new GenderTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter birthDate;

    private GenderTypeFilter gender;

    private InstantFilter registerDate;

    private StringFilter phoneNumber;

    private StringFilter mobileNumber;

    private StringFilter fullName;

    private BooleanFilter isVerified;

    private LongFilter userId;

    private StringFilter firstName;

    private StringFilter lastName;

    private UUIDFilter encId;

    private LongFilter preferdLanguageId;

    private LongFilter locationId;

    private Boolean distinct;

    public AppUserCriteria() {}

    public AppUserCriteria(AppUserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(InstantFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderTypeFilter::copy).orElse(null);
        this.registerDate = other.optionalRegisterDate().map(InstantFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.mobileNumber = other.optionalMobileNumber().map(StringFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.isVerified = other.optionalIsVerified().map(BooleanFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.encId = other.optionalEncId().map(UUIDFilter::copy).orElse(null);
        this.preferdLanguageId = other.optionalPreferdLanguageId().map(LongFilter::copy).orElse(null);
        this.locationId = other.optionalLocationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppUserCriteria copy() {
        return new AppUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public Optional<InstantFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public InstantFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new InstantFilter());
        }
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
    }

    public GenderTypeFilter getGender() {
        return gender;
    }

    public Optional<GenderTypeFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public GenderTypeFilter gender() {
        if (gender == null) {
            setGender(new GenderTypeFilter());
        }
        return gender;
    }

    public void setGender(GenderTypeFilter gender) {
        this.gender = gender;
    }

    public InstantFilter getRegisterDate() {
        return registerDate;
    }

    public Optional<InstantFilter> optionalRegisterDate() {
        return Optional.ofNullable(registerDate);
    }

    public InstantFilter registerDate() {
        if (registerDate == null) {
            setRegisterDate(new InstantFilter());
        }
        return registerDate;
    }

    public void setRegisterDate(InstantFilter registerDate) {
        this.registerDate = registerDate;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getMobileNumber() {
        return mobileNumber;
    }

    public Optional<StringFilter> optionalMobileNumber() {
        return Optional.ofNullable(mobileNumber);
    }

    public StringFilter mobileNumber() {
        if (mobileNumber == null) {
            setMobileNumber(new StringFilter());
        }
        return mobileNumber;
    }

    public void setMobileNumber(StringFilter mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public BooleanFilter getIsVerified() {
        return isVerified;
    }

    public Optional<BooleanFilter> optionalIsVerified() {
        return Optional.ofNullable(isVerified);
    }

    public BooleanFilter isVerified() {
        if (isVerified == null) {
            setIsVerified(new BooleanFilter());
        }
        return isVerified;
    }

    public void setIsVerified(BooleanFilter isVerified) {
        this.isVerified = isVerified;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public UUIDFilter getEncId() {
        return encId;
    }

    public Optional<UUIDFilter> optionalEncId() {
        return Optional.ofNullable(encId);
    }

    public UUIDFilter encId() {
        if (encId == null) {
            setEncId(new UUIDFilter());
        }
        return encId;
    }

    public void setEncId(UUIDFilter encId) {
        this.encId = encId;
    }

    public LongFilter getPreferdLanguageId() {
        return preferdLanguageId;
    }

    public Optional<LongFilter> optionalPreferdLanguageId() {
        return Optional.ofNullable(preferdLanguageId);
    }

    public LongFilter preferdLanguageId() {
        if (preferdLanguageId == null) {
            setPreferdLanguageId(new LongFilter());
        }
        return preferdLanguageId;
    }

    public void setPreferdLanguageId(LongFilter preferdLanguageId) {
        this.preferdLanguageId = preferdLanguageId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public Optional<LongFilter> optionalLocationId() {
        return Optional.ofNullable(locationId);
    }

    public LongFilter locationId() {
        if (locationId == null) {
            setLocationId(new LongFilter());
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppUserCriteria that = (AppUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(registerDate, that.registerDate) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(mobileNumber, that.mobileNumber) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(isVerified, that.isVerified) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(encId, that.encId) &&
            Objects.equals(preferdLanguageId, that.preferdLanguageId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            birthDate,
            gender,
            registerDate,
            phoneNumber,
            mobileNumber,
            fullName,
            isVerified,
            userId,
            firstName,
            lastName,
            encId,
            preferdLanguageId,
            locationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalRegisterDate().map(f -> "registerDate=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalMobileNumber().map(f -> "mobileNumber=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalIsVerified().map(f -> "isVerified=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalEncId().map(f -> "encId=" + f + ", ").orElse("") +
            optionalPreferdLanguageId().map(f -> "preferdLanguageId=" + f + ", ").orElse("") +
            optionalLocationId().map(f -> "locationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

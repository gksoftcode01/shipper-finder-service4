package ai.yarmook.shipperfinder.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.UserSubscribe} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.UserSubscribeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-subscribes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscribeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter toDate;

    private BooleanFilter isActive;

    private UUIDFilter subscribedUserEncId;

    private LongFilter subscribeTypeId;

    private Boolean distinct;

    public UserSubscribeCriteria() {}

    public UserSubscribeCriteria(UserSubscribeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.toDate = other.optionalToDate().map(InstantFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.subscribedUserEncId = other.optionalSubscribedUserEncId().map(UUIDFilter::copy).orElse(null);
        this.subscribeTypeId = other.optionalSubscribeTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserSubscribeCriteria copy() {
        return new UserSubscribeCriteria(this);
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

    public InstantFilter getFromDate() {
        return fromDate;
    }

    public Optional<InstantFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public InstantFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new InstantFilter());
        }
        return fromDate;
    }

    public void setFromDate(InstantFilter fromDate) {
        this.fromDate = fromDate;
    }

    public InstantFilter getToDate() {
        return toDate;
    }

    public Optional<InstantFilter> optionalToDate() {
        return Optional.ofNullable(toDate);
    }

    public InstantFilter toDate() {
        if (toDate == null) {
            setToDate(new InstantFilter());
        }
        return toDate;
    }

    public void setToDate(InstantFilter toDate) {
        this.toDate = toDate;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public UUIDFilter getSubscribedUserEncId() {
        return subscribedUserEncId;
    }

    public Optional<UUIDFilter> optionalSubscribedUserEncId() {
        return Optional.ofNullable(subscribedUserEncId);
    }

    public UUIDFilter subscribedUserEncId() {
        if (subscribedUserEncId == null) {
            setSubscribedUserEncId(new UUIDFilter());
        }
        return subscribedUserEncId;
    }

    public void setSubscribedUserEncId(UUIDFilter subscribedUserEncId) {
        this.subscribedUserEncId = subscribedUserEncId;
    }

    public LongFilter getSubscribeTypeId() {
        return subscribeTypeId;
    }

    public Optional<LongFilter> optionalSubscribeTypeId() {
        return Optional.ofNullable(subscribeTypeId);
    }

    public LongFilter subscribeTypeId() {
        if (subscribeTypeId == null) {
            setSubscribeTypeId(new LongFilter());
        }
        return subscribeTypeId;
    }

    public void setSubscribeTypeId(LongFilter subscribeTypeId) {
        this.subscribeTypeId = subscribeTypeId;
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
        final UserSubscribeCriteria that = (UserSubscribeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(toDate, that.toDate) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(subscribedUserEncId, that.subscribedUserEncId) &&
            Objects.equals(subscribeTypeId, that.subscribeTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromDate, toDate, isActive, subscribedUserEncId, subscribeTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSubscribeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalToDate().map(f -> "toDate=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalSubscribedUserEncId().map(f -> "subscribedUserEncId=" + f + ", ").orElse("") +
            optionalSubscribeTypeId().map(f -> "subscribeTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

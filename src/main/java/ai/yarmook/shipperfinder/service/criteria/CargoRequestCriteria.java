package ai.yarmook.shipperfinder.service.criteria;

import ai.yarmook.shipperfinder.domain.enumeration.CargoRequestStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.CargoRequest} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.CargoRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cargo-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CargoRequestStatus
     */
    public static class CargoRequestStatusFilter extends Filter<CargoRequestStatus> {

        public CargoRequestStatusFilter() {}

        public CargoRequestStatusFilter(CargoRequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public CargoRequestStatusFilter copy() {
            return new CargoRequestStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createDate;

    private InstantFilter validUntil;

    private CargoRequestStatusFilter status;

    private BooleanFilter isNegotiable;

    private FloatFilter budget;

    private UUIDFilter createdByEncId;

    private UUIDFilter takenByEncId;

    private UUIDFilter encId;

    private LongFilter itemsId;

    private LongFilter fromCountryId;

    private LongFilter toCountryId;

    private LongFilter fromStateId;

    private LongFilter toStateId;

    private Boolean distinct;

    public CargoRequestCriteria() {}

    public CargoRequestCriteria(CargoRequestCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.createDate = other.optionalCreateDate().map(InstantFilter::copy).orElse(null);
        this.validUntil = other.optionalValidUntil().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(CargoRequestStatusFilter::copy).orElse(null);
        this.isNegotiable = other.optionalIsNegotiable().map(BooleanFilter::copy).orElse(null);
        this.budget = other.optionalBudget().map(FloatFilter::copy).orElse(null);
        this.createdByEncId = other.optionalCreatedByEncId().map(UUIDFilter::copy).orElse(null);
        this.takenByEncId = other.optionalTakenByEncId().map(UUIDFilter::copy).orElse(null);
        this.encId = other.optionalEncId().map(UUIDFilter::copy).orElse(null);
        this.itemsId = other.optionalItemsId().map(LongFilter::copy).orElse(null);
        this.fromCountryId = other.optionalFromCountryId().map(LongFilter::copy).orElse(null);
        this.toCountryId = other.optionalToCountryId().map(LongFilter::copy).orElse(null);
        this.fromStateId = other.optionalFromStateId().map(LongFilter::copy).orElse(null);
        this.toStateId = other.optionalToStateId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CargoRequestCriteria copy() {
        return new CargoRequestCriteria(this);
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

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public Optional<InstantFilter> optionalCreateDate() {
        return Optional.ofNullable(createDate);
    }

    public InstantFilter createDate() {
        if (createDate == null) {
            setCreateDate(new InstantFilter());
        }
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
    }

    public InstantFilter getValidUntil() {
        return validUntil;
    }

    public Optional<InstantFilter> optionalValidUntil() {
        return Optional.ofNullable(validUntil);
    }

    public InstantFilter validUntil() {
        if (validUntil == null) {
            setValidUntil(new InstantFilter());
        }
        return validUntil;
    }

    public void setValidUntil(InstantFilter validUntil) {
        this.validUntil = validUntil;
    }

    public CargoRequestStatusFilter getStatus() {
        return status;
    }

    public Optional<CargoRequestStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public CargoRequestStatusFilter status() {
        if (status == null) {
            setStatus(new CargoRequestStatusFilter());
        }
        return status;
    }

    public void setStatus(CargoRequestStatusFilter status) {
        this.status = status;
    }

    public BooleanFilter getIsNegotiable() {
        return isNegotiable;
    }

    public Optional<BooleanFilter> optionalIsNegotiable() {
        return Optional.ofNullable(isNegotiable);
    }

    public BooleanFilter isNegotiable() {
        if (isNegotiable == null) {
            setIsNegotiable(new BooleanFilter());
        }
        return isNegotiable;
    }

    public void setIsNegotiable(BooleanFilter isNegotiable) {
        this.isNegotiable = isNegotiable;
    }

    public FloatFilter getBudget() {
        return budget;
    }

    public Optional<FloatFilter> optionalBudget() {
        return Optional.ofNullable(budget);
    }

    public FloatFilter budget() {
        if (budget == null) {
            setBudget(new FloatFilter());
        }
        return budget;
    }

    public void setBudget(FloatFilter budget) {
        this.budget = budget;
    }

    public UUIDFilter getCreatedByEncId() {
        return createdByEncId;
    }

    public Optional<UUIDFilter> optionalCreatedByEncId() {
        return Optional.ofNullable(createdByEncId);
    }

    public UUIDFilter createdByEncId() {
        if (createdByEncId == null) {
            setCreatedByEncId(new UUIDFilter());
        }
        return createdByEncId;
    }

    public void setCreatedByEncId(UUIDFilter createdByEncId) {
        this.createdByEncId = createdByEncId;
    }

    public UUIDFilter getTakenByEncId() {
        return takenByEncId;
    }

    public Optional<UUIDFilter> optionalTakenByEncId() {
        return Optional.ofNullable(takenByEncId);
    }

    public UUIDFilter takenByEncId() {
        if (takenByEncId == null) {
            setTakenByEncId(new UUIDFilter());
        }
        return takenByEncId;
    }

    public void setTakenByEncId(UUIDFilter takenByEncId) {
        this.takenByEncId = takenByEncId;
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

    public LongFilter getItemsId() {
        return itemsId;
    }

    public Optional<LongFilter> optionalItemsId() {
        return Optional.ofNullable(itemsId);
    }

    public LongFilter itemsId() {
        if (itemsId == null) {
            setItemsId(new LongFilter());
        }
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
    }

    public LongFilter getFromCountryId() {
        return fromCountryId;
    }

    public Optional<LongFilter> optionalFromCountryId() {
        return Optional.ofNullable(fromCountryId);
    }

    public LongFilter fromCountryId() {
        if (fromCountryId == null) {
            setFromCountryId(new LongFilter());
        }
        return fromCountryId;
    }

    public void setFromCountryId(LongFilter fromCountryId) {
        this.fromCountryId = fromCountryId;
    }

    public LongFilter getToCountryId() {
        return toCountryId;
    }

    public Optional<LongFilter> optionalToCountryId() {
        return Optional.ofNullable(toCountryId);
    }

    public LongFilter toCountryId() {
        if (toCountryId == null) {
            setToCountryId(new LongFilter());
        }
        return toCountryId;
    }

    public void setToCountryId(LongFilter toCountryId) {
        this.toCountryId = toCountryId;
    }

    public LongFilter getFromStateId() {
        return fromStateId;
    }

    public Optional<LongFilter> optionalFromStateId() {
        return Optional.ofNullable(fromStateId);
    }

    public LongFilter fromStateId() {
        if (fromStateId == null) {
            setFromStateId(new LongFilter());
        }
        return fromStateId;
    }

    public void setFromStateId(LongFilter fromStateId) {
        this.fromStateId = fromStateId;
    }

    public LongFilter getToStateId() {
        return toStateId;
    }

    public Optional<LongFilter> optionalToStateId() {
        return Optional.ofNullable(toStateId);
    }

    public LongFilter toStateId() {
        if (toStateId == null) {
            setToStateId(new LongFilter());
        }
        return toStateId;
    }

    public void setToStateId(LongFilter toStateId) {
        this.toStateId = toStateId;
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
        final CargoRequestCriteria that = (CargoRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(validUntil, that.validUntil) &&
            Objects.equals(status, that.status) &&
            Objects.equals(isNegotiable, that.isNegotiable) &&
            Objects.equals(budget, that.budget) &&
            Objects.equals(createdByEncId, that.createdByEncId) &&
            Objects.equals(takenByEncId, that.takenByEncId) &&
            Objects.equals(encId, that.encId) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(fromCountryId, that.fromCountryId) &&
            Objects.equals(toCountryId, that.toCountryId) &&
            Objects.equals(fromStateId, that.fromStateId) &&
            Objects.equals(toStateId, that.toStateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createDate,
            validUntil,
            status,
            isNegotiable,
            budget,
            createdByEncId,
            takenByEncId,
            encId,
            itemsId,
            fromCountryId,
            toCountryId,
            fromStateId,
            toStateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequestCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreateDate().map(f -> "createDate=" + f + ", ").orElse("") +
            optionalValidUntil().map(f -> "validUntil=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalIsNegotiable().map(f -> "isNegotiable=" + f + ", ").orElse("") +
            optionalBudget().map(f -> "budget=" + f + ", ").orElse("") +
            optionalCreatedByEncId().map(f -> "createdByEncId=" + f + ", ").orElse("") +
            optionalTakenByEncId().map(f -> "takenByEncId=" + f + ", ").orElse("") +
            optionalEncId().map(f -> "encId=" + f + ", ").orElse("") +
            optionalItemsId().map(f -> "itemsId=" + f + ", ").orElse("") +
            optionalFromCountryId().map(f -> "fromCountryId=" + f + ", ").orElse("") +
            optionalToCountryId().map(f -> "toCountryId=" + f + ", ").orElse("") +
            optionalFromStateId().map(f -> "fromStateId=" + f + ", ").orElse("") +
            optionalToStateId().map(f -> "toStateId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

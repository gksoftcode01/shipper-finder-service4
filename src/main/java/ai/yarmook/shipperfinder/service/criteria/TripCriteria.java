package ai.yarmook.shipperfinder.service.criteria;

import ai.yarmook.shipperfinder.domain.enumeration.TripStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.Trip} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.TripResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trips?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TripStatus
     */
    public static class TripStatusFilter extends Filter<TripStatus> {

        public TripStatusFilter() {}

        public TripStatusFilter(TripStatusFilter filter) {
            super(filter);
        }

        @Override
        public TripStatusFilter copy() {
            return new TripStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter tripDate;

    private InstantFilter arriveDate;

    private LongFilter maxWeight;

    private StringFilter notes;

    private InstantFilter createDate;

    private BooleanFilter isNegotiate;

    private TripStatusFilter status;

    private UUIDFilter createdByEncId;

    private UUIDFilter encId;

    private LongFilter itemsId;

    private LongFilter fromCountryId;

    private LongFilter toCountryId;

    private LongFilter fromStateId;

    private LongFilter toStateId;

    private Boolean distinct;

    public TripCriteria() {}

    public TripCriteria(TripCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tripDate = other.optionalTripDate().map(InstantFilter::copy).orElse(null);
        this.arriveDate = other.optionalArriveDate().map(InstantFilter::copy).orElse(null);
        this.maxWeight = other.optionalMaxWeight().map(LongFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.createDate = other.optionalCreateDate().map(InstantFilter::copy).orElse(null);
        this.isNegotiate = other.optionalIsNegotiate().map(BooleanFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TripStatusFilter::copy).orElse(null);
        this.createdByEncId = other.optionalCreatedByEncId().map(UUIDFilter::copy).orElse(null);
        this.encId = other.optionalEncId().map(UUIDFilter::copy).orElse(null);
        this.itemsId = other.optionalItemsId().map(LongFilter::copy).orElse(null);
        this.fromCountryId = other.optionalFromCountryId().map(LongFilter::copy).orElse(null);
        this.toCountryId = other.optionalToCountryId().map(LongFilter::copy).orElse(null);
        this.fromStateId = other.optionalFromStateId().map(LongFilter::copy).orElse(null);
        this.toStateId = other.optionalToStateId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TripCriteria copy() {
        return new TripCriteria(this);
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

    public InstantFilter getTripDate() {
        return tripDate;
    }

    public Optional<InstantFilter> optionalTripDate() {
        return Optional.ofNullable(tripDate);
    }

    public InstantFilter tripDate() {
        if (tripDate == null) {
            setTripDate(new InstantFilter());
        }
        return tripDate;
    }

    public void setTripDate(InstantFilter tripDate) {
        this.tripDate = tripDate;
    }

    public InstantFilter getArriveDate() {
        return arriveDate;
    }

    public Optional<InstantFilter> optionalArriveDate() {
        return Optional.ofNullable(arriveDate);
    }

    public InstantFilter arriveDate() {
        if (arriveDate == null) {
            setArriveDate(new InstantFilter());
        }
        return arriveDate;
    }

    public void setArriveDate(InstantFilter arriveDate) {
        this.arriveDate = arriveDate;
    }

    public LongFilter getMaxWeight() {
        return maxWeight;
    }

    public Optional<LongFilter> optionalMaxWeight() {
        return Optional.ofNullable(maxWeight);
    }

    public LongFilter maxWeight() {
        if (maxWeight == null) {
            setMaxWeight(new LongFilter());
        }
        return maxWeight;
    }

    public void setMaxWeight(LongFilter maxWeight) {
        this.maxWeight = maxWeight;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
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

    public BooleanFilter getIsNegotiate() {
        return isNegotiate;
    }

    public Optional<BooleanFilter> optionalIsNegotiate() {
        return Optional.ofNullable(isNegotiate);
    }

    public BooleanFilter isNegotiate() {
        if (isNegotiate == null) {
            setIsNegotiate(new BooleanFilter());
        }
        return isNegotiate;
    }

    public void setIsNegotiate(BooleanFilter isNegotiate) {
        this.isNegotiate = isNegotiate;
    }

    public TripStatusFilter getStatus() {
        return status;
    }

    public Optional<TripStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TripStatusFilter status() {
        if (status == null) {
            setStatus(new TripStatusFilter());
        }
        return status;
    }

    public void setStatus(TripStatusFilter status) {
        this.status = status;
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
        final TripCriteria that = (TripCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tripDate, that.tripDate) &&
            Objects.equals(arriveDate, that.arriveDate) &&
            Objects.equals(maxWeight, that.maxWeight) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(isNegotiate, that.isNegotiate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdByEncId, that.createdByEncId) &&
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
            tripDate,
            arriveDate,
            maxWeight,
            notes,
            createDate,
            isNegotiate,
            status,
            createdByEncId,
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
        return "TripCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTripDate().map(f -> "tripDate=" + f + ", ").orElse("") +
            optionalArriveDate().map(f -> "arriveDate=" + f + ", ").orElse("") +
            optionalMaxWeight().map(f -> "maxWeight=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalCreateDate().map(f -> "createDate=" + f + ", ").orElse("") +
            optionalIsNegotiate().map(f -> "isNegotiate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedByEncId().map(f -> "createdByEncId=" + f + ", ").orElse("") +
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

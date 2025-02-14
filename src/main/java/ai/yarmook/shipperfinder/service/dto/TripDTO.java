package ai.yarmook.shipperfinder.service.dto;

import ai.yarmook.shipperfinder.domain.enumeration.TripStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.Trip} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripDTO implements Serializable {

    private Long id;

    private Instant tripDate;

    private Instant arriveDate;

    private Long maxWeight;

    private String notes;

    private Instant createDate;

    private Boolean isNegotiate;

    private TripStatus status;

    private UUID createdByEncId;

    private UUID encId;

    private CountryDTO fromCountry;

    private CountryDTO toCountry;

    private StateProvinceDTO fromState;

    private StateProvinceDTO toState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTripDate() {
        return tripDate;
    }

    public void setTripDate(Instant tripDate) {
        this.tripDate = tripDate;
    }

    public Instant getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Instant arriveDate) {
        this.arriveDate = arriveDate;
    }

    public Long getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Long maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Boolean getIsNegotiate() {
        return isNegotiate;
    }

    public void setIsNegotiate(Boolean isNegotiate) {
        this.isNegotiate = isNegotiate;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public UUID getCreatedByEncId() {
        return createdByEncId;
    }

    public void setCreatedByEncId(UUID createdByEncId) {
        this.createdByEncId = createdByEncId;
    }

    public UUID getEncId() {
        return encId;
    }

    public void setEncId(UUID encId) {
        this.encId = encId;
    }

    public CountryDTO getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(CountryDTO fromCountry) {
        this.fromCountry = fromCountry;
    }

    public CountryDTO getToCountry() {
        return toCountry;
    }

    public void setToCountry(CountryDTO toCountry) {
        this.toCountry = toCountry;
    }

    public StateProvinceDTO getFromState() {
        return fromState;
    }

    public void setFromState(StateProvinceDTO fromState) {
        this.fromState = fromState;
    }

    public StateProvinceDTO getToState() {
        return toState;
    }

    public void setToState(StateProvinceDTO toState) {
        this.toState = toState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripDTO)) {
            return false;
        }

        TripDTO tripDTO = (TripDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tripDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripDTO{" +
            "id=" + getId() +
            ", tripDate='" + getTripDate() + "'" +
            ", arriveDate='" + getArriveDate() + "'" +
            ", maxWeight=" + getMaxWeight() +
            ", notes='" + getNotes() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", isNegotiate='" + getIsNegotiate() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdByEncId='" + getCreatedByEncId() + "'" +
            ", encId='" + getEncId() + "'" +
            ", fromCountry=" + getFromCountry() +
            ", toCountry=" + getToCountry() +
            ", fromState=" + getFromState() +
            ", toState=" + getToState() +
            "}";
    }
}

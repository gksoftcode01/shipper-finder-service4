package ai.yarmook.shipperfinder.service.dto;

import ai.yarmook.shipperfinder.domain.enumeration.CargoRequestStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.CargoRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoRequestDTO implements Serializable {

    private Long id;

    private Instant createDate;

    private Instant validUntil;

    private CargoRequestStatus status;

    private Boolean isNegotiable;

    private Float budget;

    private UUID createdByEncId;

    private UUID takenByEncId;

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

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public CargoRequestStatus getStatus() {
        return status;
    }

    public void setStatus(CargoRequestStatus status) {
        this.status = status;
    }

    public Boolean getIsNegotiable() {
        return isNegotiable;
    }

    public void setIsNegotiable(Boolean isNegotiable) {
        this.isNegotiable = isNegotiable;
    }

    public Float getBudget() {
        return budget;
    }

    public void setBudget(Float budget) {
        this.budget = budget;
    }

    public UUID getCreatedByEncId() {
        return createdByEncId;
    }

    public void setCreatedByEncId(UUID createdByEncId) {
        this.createdByEncId = createdByEncId;
    }

    public UUID getTakenByEncId() {
        return takenByEncId;
    }

    public void setTakenByEncId(UUID takenByEncId) {
        this.takenByEncId = takenByEncId;
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
        if (!(o instanceof CargoRequestDTO)) {
            return false;
        }

        CargoRequestDTO cargoRequestDTO = (CargoRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cargoRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequestDTO{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", status='" + getStatus() + "'" +
            ", isNegotiable='" + getIsNegotiable() + "'" +
            ", budget=" + getBudget() +
            ", createdByEncId='" + getCreatedByEncId() + "'" +
            ", takenByEncId='" + getTakenByEncId() + "'" +
            ", encId='" + getEncId() + "'" +
            ", fromCountry=" + getFromCountry() +
            ", toCountry=" + getToCountry() +
            ", fromState=" + getFromState() +
            ", toState=" + getToState() +
            "}";
    }
}

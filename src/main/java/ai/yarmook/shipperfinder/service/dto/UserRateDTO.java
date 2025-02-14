package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.UserRate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRateDTO implements Serializable {

    private Long id;

    private Long rate;

    private String note;

    private Instant rateDate;

    private UUID ratedByEncId;

    private UUID ratedEncId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getRateDate() {
        return rateDate;
    }

    public void setRateDate(Instant rateDate) {
        this.rateDate = rateDate;
    }

    public UUID getRatedByEncId() {
        return ratedByEncId;
    }

    public void setRatedByEncId(UUID ratedByEncId) {
        this.ratedByEncId = ratedByEncId;
    }

    public UUID getRatedEncId() {
        return ratedEncId;
    }

    public void setRatedEncId(UUID ratedEncId) {
        this.ratedEncId = ratedEncId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRateDTO)) {
            return false;
        }

        UserRateDTO userRateDTO = (UserRateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userRateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRateDTO{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", note='" + getNote() + "'" +
            ", rateDate='" + getRateDate() + "'" +
            ", ratedByEncId='" + getRatedByEncId() + "'" +
            ", ratedEncId='" + getRatedEncId() + "'" +
            "}";
    }
}

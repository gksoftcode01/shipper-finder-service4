package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.UserSubscribe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscribeDTO implements Serializable {

    private Long id;

    private Instant fromDate;

    private Instant toDate;

    private Boolean isActive;

    private UUID subscribedUserEncId;

    private SubscribeTypeDTO subscribeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UUID getSubscribedUserEncId() {
        return subscribedUserEncId;
    }

    public void setSubscribedUserEncId(UUID subscribedUserEncId) {
        this.subscribedUserEncId = subscribedUserEncId;
    }

    public SubscribeTypeDTO getSubscribeType() {
        return subscribeType;
    }

    public void setSubscribeType(SubscribeTypeDTO subscribeType) {
        this.subscribeType = subscribeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSubscribeDTO)) {
            return false;
        }

        UserSubscribeDTO userSubscribeDTO = (UserSubscribeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userSubscribeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSubscribeDTO{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", subscribedUserEncId='" + getSubscribedUserEncId() + "'" +
            ", subscribeType=" + getSubscribeType() +
            "}";
    }
}

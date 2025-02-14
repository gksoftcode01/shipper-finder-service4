package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.TripMsg} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripMsgDTO implements Serializable {

    private Long id;

    private String msg;

    private UUID fromUserEncId;

    private UUID toUserEncId;

    private Long tripId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UUID getFromUserEncId() {
        return fromUserEncId;
    }

    public void setFromUserEncId(UUID fromUserEncId) {
        this.fromUserEncId = fromUserEncId;
    }

    public UUID getToUserEncId() {
        return toUserEncId;
    }

    public void setToUserEncId(UUID toUserEncId) {
        this.toUserEncId = toUserEncId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripMsgDTO)) {
            return false;
        }

        TripMsgDTO tripMsgDTO = (TripMsgDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tripMsgDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripMsgDTO{" +
            "id=" + getId() +
            ", msg='" + getMsg() + "'" +
            ", fromUserEncId='" + getFromUserEncId() + "'" +
            ", toUserEncId='" + getToUserEncId() + "'" +
            ", tripId=" + getTripId() +
            "}";
    }
}

package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.CargoMsg} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoMsgDTO implements Serializable {

    private Long id;

    private String msg;

    private UUID fromUserEncId;

    private UUID toUserEncId;

    private Long cargoRequestId;

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

    public Long getCargoRequestId() {
        return cargoRequestId;
    }

    public void setCargoRequestId(Long cargoRequestId) {
        this.cargoRequestId = cargoRequestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoMsgDTO)) {
            return false;
        }

        CargoMsgDTO cargoMsgDTO = (CargoMsgDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cargoMsgDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoMsgDTO{" +
            "id=" + getId() +
            ", msg='" + getMsg() + "'" +
            ", fromUserEncId='" + getFromUserEncId() + "'" +
            ", toUserEncId='" + getToUserEncId() + "'" +
            ", cargoRequestId=" + getCargoRequestId() +
            "}";
    }
}

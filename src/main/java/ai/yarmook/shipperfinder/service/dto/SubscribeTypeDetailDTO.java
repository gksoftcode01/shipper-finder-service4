package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.SubscribeTypeDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribeTypeDetailDTO implements Serializable {

    private Long id;

    private Float price;

    private Integer maxTrip;

    private Integer maxItems;

    private Integer maxRequest;

    private Integer maxNumberView;

    private SubscribeTypeDTO subscribeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getMaxTrip() {
        return maxTrip;
    }

    public void setMaxTrip(Integer maxTrip) {
        this.maxTrip = maxTrip;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMaxRequest() {
        return maxRequest;
    }

    public void setMaxRequest(Integer maxRequest) {
        this.maxRequest = maxRequest;
    }

    public Integer getMaxNumberView() {
        return maxNumberView;
    }

    public void setMaxNumberView(Integer maxNumberView) {
        this.maxNumberView = maxNumberView;
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
        if (!(o instanceof SubscribeTypeDetailDTO)) {
            return false;
        }

        SubscribeTypeDetailDTO subscribeTypeDetailDTO = (SubscribeTypeDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscribeTypeDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribeTypeDetailDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", maxTrip=" + getMaxTrip() +
            ", maxItems=" + getMaxItems() +
            ", maxRequest=" + getMaxRequest() +
            ", maxNumberView=" + getMaxNumberView() +
            ", subscribeType=" + getSubscribeType() +
            "}";
    }
}

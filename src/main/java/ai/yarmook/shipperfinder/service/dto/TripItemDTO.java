package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.TripItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripItemDTO implements Serializable {

    private Long id;

    private Float itemPrice;

    private Long maxQty;

    private ItemDTO item;

    private UnitDTO unit;

    private Set<TagDTO> tags = new HashSet<>();

    private TripDTO trip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Long getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Long maxQty) {
        this.maxQty = maxQty;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public UnitDTO getUnit() {
        return unit;
    }

    public void setUnit(UnitDTO unit) {
        this.unit = unit;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripItemDTO)) {
            return false;
        }

        TripItemDTO tripItemDTO = (TripItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tripItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripItemDTO{" +
            "id=" + getId() +
            ", itemPrice=" + getItemPrice() +
            ", maxQty=" + getMaxQty() +
            ", item=" + getItem() +
            ", unit=" + getUnit() +
            ", tags=" + getTags() +
            ", trip=" + getTrip() +
            "}";
    }
}

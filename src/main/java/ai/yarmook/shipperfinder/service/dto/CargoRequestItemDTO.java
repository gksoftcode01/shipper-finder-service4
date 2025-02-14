package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.CargoRequestItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoRequestItemDTO implements Serializable {

    private Long id;

    private Long maxQty;

    private String photoUrl;

    private ItemDTO item;

    private UnitDTO unit;

    private Set<TagDTO> tags = new HashSet<>();

    private CargoRequestDTO cargoRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Long maxQty) {
        this.maxQty = maxQty;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public CargoRequestDTO getCargoRequest() {
        return cargoRequest;
    }

    public void setCargoRequest(CargoRequestDTO cargoRequest) {
        this.cargoRequest = cargoRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoRequestItemDTO)) {
            return false;
        }

        CargoRequestItemDTO cargoRequestItemDTO = (CargoRequestItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cargoRequestItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequestItemDTO{" +
            "id=" + getId() +
            ", maxQty=" + getMaxQty() +
            ", photoUrl='" + getPhotoUrl() + "'" +
            ", item=" + getItem() +
            ", unit=" + getUnit() +
            ", tags=" + getTags() +
            ", cargoRequest=" + getCargoRequest() +
            "}";
    }
}

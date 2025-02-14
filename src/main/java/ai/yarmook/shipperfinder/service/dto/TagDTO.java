package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.Tag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagDTO implements Serializable {

    private Long id;

    private String name;

    private String nameEn;

    private String nameAr;

    private String nameFr;

    private String nameDe;

    private String nameUrdu;

    private String iconUrl;

    private Set<TripItemDTO> tripItems = new HashSet<>();

    private Set<CargoRequestItemDTO> cargoRequestItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameUrdu() {
        return nameUrdu;
    }

    public void setNameUrdu(String nameUrdu) {
        this.nameUrdu = nameUrdu;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Set<TripItemDTO> getTripItems() {
        return tripItems;
    }

    public void setTripItems(Set<TripItemDTO> tripItems) {
        this.tripItems = tripItems;
    }

    public Set<CargoRequestItemDTO> getCargoRequestItems() {
        return cargoRequestItems;
    }

    public void setCargoRequestItems(Set<CargoRequestItemDTO> cargoRequestItems) {
        this.cargoRequestItems = cargoRequestItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagDTO)) {
            return false;
        }

        TagDTO tagDTO = (TagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", nameFr='" + getNameFr() + "'" +
            ", nameDe='" + getNameDe() + "'" +
            ", nameUrdu='" + getNameUrdu() + "'" +
            ", iconUrl='" + getIconUrl() + "'" +
            ", tripItems=" + getTripItems() +
            ", cargoRequestItems=" + getCargoRequestItems() +
            "}";
    }
}

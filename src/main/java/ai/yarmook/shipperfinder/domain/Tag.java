package ai.yarmook.shipperfinder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Column(name = "name_en")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameEn;

    @Column(name = "name_ar")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameAr;

    @Column(name = "name_fr")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameFr;

    @Column(name = "name_de")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameDe;

    @Column(name = "name_urdu")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameUrdu;

    @Column(name = "icon_url")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iconUrl;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "item", "unit", "tags", "trip" }, allowSetters = true)
    private Set<TripItem> tripItems = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "item", "unit", "tags", "cargoRequest" }, allowSetters = true)
    private Set<CargoRequestItem> cargoRequestItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tag name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public Tag nameEn(String nameEn) {
        this.setNameEn(nameEn);
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public Tag nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameFr() {
        return this.nameFr;
    }

    public Tag nameFr(String nameFr) {
        this.setNameFr(nameFr);
        return this;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameDe() {
        return this.nameDe;
    }

    public Tag nameDe(String nameDe) {
        this.setNameDe(nameDe);
        return this;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameUrdu() {
        return this.nameUrdu;
    }

    public Tag nameUrdu(String nameUrdu) {
        this.setNameUrdu(nameUrdu);
        return this;
    }

    public void setNameUrdu(String nameUrdu) {
        this.nameUrdu = nameUrdu;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public Tag iconUrl(String iconUrl) {
        this.setIconUrl(iconUrl);
        return this;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Set<TripItem> getTripItems() {
        return this.tripItems;
    }

    public void setTripItems(Set<TripItem> tripItems) {
        if (this.tripItems != null) {
            this.tripItems.forEach(i -> i.removeTag(this));
        }
        if (tripItems != null) {
            tripItems.forEach(i -> i.addTag(this));
        }
        this.tripItems = tripItems;
    }

    public Tag tripItems(Set<TripItem> tripItems) {
        this.setTripItems(tripItems);
        return this;
    }

    public Tag addTripItem(TripItem tripItem) {
        this.tripItems.add(tripItem);
        tripItem.getTags().add(this);
        return this;
    }

    public Tag removeTripItem(TripItem tripItem) {
        this.tripItems.remove(tripItem);
        tripItem.getTags().remove(this);
        return this;
    }

    public Set<CargoRequestItem> getCargoRequestItems() {
        return this.cargoRequestItems;
    }

    public void setCargoRequestItems(Set<CargoRequestItem> cargoRequestItems) {
        if (this.cargoRequestItems != null) {
            this.cargoRequestItems.forEach(i -> i.removeTag(this));
        }
        if (cargoRequestItems != null) {
            cargoRequestItems.forEach(i -> i.addTag(this));
        }
        this.cargoRequestItems = cargoRequestItems;
    }

    public Tag cargoRequestItems(Set<CargoRequestItem> cargoRequestItems) {
        this.setCargoRequestItems(cargoRequestItems);
        return this;
    }

    public Tag addCargoRequestItem(CargoRequestItem cargoRequestItem) {
        this.cargoRequestItems.add(cargoRequestItem);
        cargoRequestItem.getTags().add(this);
        return this;
    }

    public Tag removeCargoRequestItem(CargoRequestItem cargoRequestItem) {
        this.cargoRequestItems.remove(cargoRequestItem);
        cargoRequestItem.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return getId() != null && getId().equals(((Tag) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", nameFr='" + getNameFr() + "'" +
            ", nameDe='" + getNameDe() + "'" +
            ", nameUrdu='" + getNameUrdu() + "'" +
            ", iconUrl='" + getIconUrl() + "'" +
            "}";
    }
}

package ai.yarmook.shipperfinder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CargoRequestItem.
 */
@Entity
@Table(name = "cargo_request_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cargorequestitem")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoRequestItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "max_qty")
    private Long maxQty;

    @Column(name = "photo_url")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "itemType" }, allowSetters = true)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    private Unit unit;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_cargo_request_item__tag",
        joinColumns = @JoinColumn(name = "cargo_request_item_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tripItems", "cargoRequestItems" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "items", "fromCountry", "toCountry", "fromState", "toState" }, allowSetters = true)
    private CargoRequest cargoRequest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CargoRequestItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaxQty() {
        return this.maxQty;
    }

    public CargoRequestItem maxQty(Long maxQty) {
        this.setMaxQty(maxQty);
        return this;
    }

    public void setMaxQty(Long maxQty) {
        this.maxQty = maxQty;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public CargoRequestItem photoUrl(String photoUrl) {
        this.setPhotoUrl(photoUrl);
        return this;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public CargoRequestItem item(Item item) {
        this.setItem(item);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public CargoRequestItem unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public CargoRequestItem tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public CargoRequestItem addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public CargoRequestItem removeTag(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    public CargoRequest getCargoRequest() {
        return this.cargoRequest;
    }

    public void setCargoRequest(CargoRequest cargoRequest) {
        this.cargoRequest = cargoRequest;
    }

    public CargoRequestItem cargoRequest(CargoRequest cargoRequest) {
        this.setCargoRequest(cargoRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoRequestItem)) {
            return false;
        }
        return getId() != null && getId().equals(((CargoRequestItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequestItem{" +
            "id=" + getId() +
            ", maxQty=" + getMaxQty() +
            ", photoUrl='" + getPhotoUrl() + "'" +
            "}";
    }
}

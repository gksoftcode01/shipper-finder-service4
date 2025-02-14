package ai.yarmook.shipperfinder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TripItem.
 */
@Entity
@Table(name = "trip_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tripitem")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "item_price")
    private Float itemPrice;

    @Column(name = "max_qty")
    private Long maxQty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "itemType" }, allowSetters = true)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    private Unit unit;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_trip_item__tag",
        joinColumns = @JoinColumn(name = "trip_item_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tripItems", "cargoRequestItems" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "items", "fromCountry", "toCountry", "fromState", "toState" }, allowSetters = true)
    private Trip trip;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TripItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getItemPrice() {
        return this.itemPrice;
    }

    public TripItem itemPrice(Float itemPrice) {
        this.setItemPrice(itemPrice);
        return this;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Long getMaxQty() {
        return this.maxQty;
    }

    public TripItem maxQty(Long maxQty) {
        this.setMaxQty(maxQty);
        return this;
    }

    public void setMaxQty(Long maxQty) {
        this.maxQty = maxQty;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public TripItem item(Item item) {
        this.setItem(item);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public TripItem unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public TripItem tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public TripItem addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public TripItem removeTag(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public TripItem trip(Trip trip) {
        this.setTrip(trip);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripItem)) {
            return false;
        }
        return getId() != null && getId().equals(((TripItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripItem{" +
            "id=" + getId() +
            ", itemPrice=" + getItemPrice() +
            ", maxQty=" + getMaxQty() +
            "}";
    }
}

package ai.yarmook.shipperfinder.domain;

import ai.yarmook.shipperfinder.domain.enumeration.TripStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "trip")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "trip_date")
    private Instant tripDate;

    @Column(name = "arrive_date")
    private Instant arriveDate;

    @Column(name = "max_weight")
    private Long maxWeight;

    @Column(name = "notes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String notes;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "is_negotiate")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isNegotiate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TripStatus status;

    @Column(name = "created_by_enc_id")
    private UUID createdByEncId;

    @Column(name = "enc_id")
    private UUID encId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "item", "unit", "tags", "trip" }, allowSetters = true)
    private Set<TripItem> items = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "stateProvinces" }, allowSetters = true)
    private Country fromCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "stateProvinces" }, allowSetters = true)
    private Country toCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private StateProvince fromState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private StateProvince toState;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTripDate() {
        return this.tripDate;
    }

    public Trip tripDate(Instant tripDate) {
        this.setTripDate(tripDate);
        return this;
    }

    public void setTripDate(Instant tripDate) {
        this.tripDate = tripDate;
    }

    public Instant getArriveDate() {
        return this.arriveDate;
    }

    public Trip arriveDate(Instant arriveDate) {
        this.setArriveDate(arriveDate);
        return this;
    }

    public void setArriveDate(Instant arriveDate) {
        this.arriveDate = arriveDate;
    }

    public Long getMaxWeight() {
        return this.maxWeight;
    }

    public Trip maxWeight(Long maxWeight) {
        this.setMaxWeight(maxWeight);
        return this;
    }

    public void setMaxWeight(Long maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getNotes() {
        return this.notes;
    }

    public Trip notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Trip createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Boolean getIsNegotiate() {
        return this.isNegotiate;
    }

    public Trip isNegotiate(Boolean isNegotiate) {
        this.setIsNegotiate(isNegotiate);
        return this;
    }

    public void setIsNegotiate(Boolean isNegotiate) {
        this.isNegotiate = isNegotiate;
    }

    public TripStatus getStatus() {
        return this.status;
    }

    public Trip status(TripStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public UUID getCreatedByEncId() {
        return this.createdByEncId;
    }

    public Trip createdByEncId(UUID createdByEncId) {
        this.setCreatedByEncId(createdByEncId);
        return this;
    }

    public void setCreatedByEncId(UUID createdByEncId) {
        this.createdByEncId = createdByEncId;
    }

    public UUID getEncId() {
        return this.encId;
    }

    public Trip encId(UUID encId) {
        this.setEncId(encId);
        return this;
    }

    public void setEncId(UUID encId) {
        this.encId = encId;
    }

    public Set<TripItem> getItems() {
        return this.items;
    }

    public void setItems(Set<TripItem> tripItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setTrip(null));
        }
        if (tripItems != null) {
            tripItems.forEach(i -> i.setTrip(this));
        }
        this.items = tripItems;
    }

    public Trip items(Set<TripItem> tripItems) {
        this.setItems(tripItems);
        return this;
    }

    public Trip addItems(TripItem tripItem) {
        this.items.add(tripItem);
        tripItem.setTrip(this);
        return this;
    }

    public Trip removeItems(TripItem tripItem) {
        this.items.remove(tripItem);
        tripItem.setTrip(null);
        return this;
    }

    public Country getFromCountry() {
        return this.fromCountry;
    }

    public void setFromCountry(Country country) {
        this.fromCountry = country;
    }

    public Trip fromCountry(Country country) {
        this.setFromCountry(country);
        return this;
    }

    public Country getToCountry() {
        return this.toCountry;
    }

    public void setToCountry(Country country) {
        this.toCountry = country;
    }

    public Trip toCountry(Country country) {
        this.setToCountry(country);
        return this;
    }

    public StateProvince getFromState() {
        return this.fromState;
    }

    public void setFromState(StateProvince stateProvince) {
        this.fromState = stateProvince;
    }

    public Trip fromState(StateProvince stateProvince) {
        this.setFromState(stateProvince);
        return this;
    }

    public StateProvince getToState() {
        return this.toState;
    }

    public void setToState(StateProvince stateProvince) {
        this.toState = stateProvince;
    }

    public Trip toState(StateProvince stateProvince) {
        this.setToState(stateProvince);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return getId() != null && getId().equals(((Trip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", tripDate='" + getTripDate() + "'" +
            ", arriveDate='" + getArriveDate() + "'" +
            ", maxWeight=" + getMaxWeight() +
            ", notes='" + getNotes() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", isNegotiate='" + getIsNegotiate() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdByEncId='" + getCreatedByEncId() + "'" +
            ", encId='" + getEncId() + "'" +
            "}";
    }
}

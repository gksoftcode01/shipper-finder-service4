package ai.yarmook.shipperfinder.domain;

import ai.yarmook.shipperfinder.domain.enumeration.CargoRequestStatus;
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
 * A CargoRequest.
 */
@Entity
@Table(name = "cargo_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cargorequest")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "valid_until")
    private Instant validUntil;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private CargoRequestStatus status;

    @Column(name = "is_negotiable")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isNegotiable;

    @Column(name = "budget")
    private Float budget;

    @Column(name = "created_by_enc_id")
    private UUID createdByEncId;

    @Column(name = "taken_by_enc_id")
    private UUID takenByEncId;

    @Column(name = "enc_id")
    private UUID encId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cargoRequest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "item", "unit", "tags", "cargoRequest" }, allowSetters = true)
    private Set<CargoRequestItem> items = new HashSet<>();

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

    public CargoRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public CargoRequest createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getValidUntil() {
        return this.validUntil;
    }

    public CargoRequest validUntil(Instant validUntil) {
        this.setValidUntil(validUntil);
        return this;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public CargoRequestStatus getStatus() {
        return this.status;
    }

    public CargoRequest status(CargoRequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CargoRequestStatus status) {
        this.status = status;
    }

    public Boolean getIsNegotiable() {
        return this.isNegotiable;
    }

    public CargoRequest isNegotiable(Boolean isNegotiable) {
        this.setIsNegotiable(isNegotiable);
        return this;
    }

    public void setIsNegotiable(Boolean isNegotiable) {
        this.isNegotiable = isNegotiable;
    }

    public Float getBudget() {
        return this.budget;
    }

    public CargoRequest budget(Float budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(Float budget) {
        this.budget = budget;
    }

    public UUID getCreatedByEncId() {
        return this.createdByEncId;
    }

    public CargoRequest createdByEncId(UUID createdByEncId) {
        this.setCreatedByEncId(createdByEncId);
        return this;
    }

    public void setCreatedByEncId(UUID createdByEncId) {
        this.createdByEncId = createdByEncId;
    }

    public UUID getTakenByEncId() {
        return this.takenByEncId;
    }

    public CargoRequest takenByEncId(UUID takenByEncId) {
        this.setTakenByEncId(takenByEncId);
        return this;
    }

    public void setTakenByEncId(UUID takenByEncId) {
        this.takenByEncId = takenByEncId;
    }

    public UUID getEncId() {
        return this.encId;
    }

    public CargoRequest encId(UUID encId) {
        this.setEncId(encId);
        return this;
    }

    public void setEncId(UUID encId) {
        this.encId = encId;
    }

    public Set<CargoRequestItem> getItems() {
        return this.items;
    }

    public void setItems(Set<CargoRequestItem> cargoRequestItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCargoRequest(null));
        }
        if (cargoRequestItems != null) {
            cargoRequestItems.forEach(i -> i.setCargoRequest(this));
        }
        this.items = cargoRequestItems;
    }

    public CargoRequest items(Set<CargoRequestItem> cargoRequestItems) {
        this.setItems(cargoRequestItems);
        return this;
    }

    public CargoRequest addItems(CargoRequestItem cargoRequestItem) {
        this.items.add(cargoRequestItem);
        cargoRequestItem.setCargoRequest(this);
        return this;
    }

    public CargoRequest removeItems(CargoRequestItem cargoRequestItem) {
        this.items.remove(cargoRequestItem);
        cargoRequestItem.setCargoRequest(null);
        return this;
    }

    public Country getFromCountry() {
        return this.fromCountry;
    }

    public void setFromCountry(Country country) {
        this.fromCountry = country;
    }

    public CargoRequest fromCountry(Country country) {
        this.setFromCountry(country);
        return this;
    }

    public Country getToCountry() {
        return this.toCountry;
    }

    public void setToCountry(Country country) {
        this.toCountry = country;
    }

    public CargoRequest toCountry(Country country) {
        this.setToCountry(country);
        return this;
    }

    public StateProvince getFromState() {
        return this.fromState;
    }

    public void setFromState(StateProvince stateProvince) {
        this.fromState = stateProvince;
    }

    public CargoRequest fromState(StateProvince stateProvince) {
        this.setFromState(stateProvince);
        return this;
    }

    public StateProvince getToState() {
        return this.toState;
    }

    public void setToState(StateProvince stateProvince) {
        this.toState = stateProvince;
    }

    public CargoRequest toState(StateProvince stateProvince) {
        this.setToState(stateProvince);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((CargoRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequest{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", status='" + getStatus() + "'" +
            ", isNegotiable='" + getIsNegotiable() + "'" +
            ", budget=" + getBudget() +
            ", createdByEncId='" + getCreatedByEncId() + "'" +
            ", takenByEncId='" + getTakenByEncId() + "'" +
            ", encId='" + getEncId() + "'" +
            "}";
    }
}

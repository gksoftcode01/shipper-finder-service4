package ai.yarmook.shipperfinder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscribeTypeDetail.
 */
@Entity
@Table(name = "subscribe_type_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "subscribetypedetail")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribeTypeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Float price;

    @Column(name = "max_trip")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer maxTrip;

    @Column(name = "max_items")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer maxItems;

    @Column(name = "max_request")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer maxRequest;

    @Column(name = "max_number_view")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer maxNumberView;

    @JsonIgnoreProperties(value = { "subscribeTypeDetail" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private SubscribeType subscribeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscribeTypeDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return this.price;
    }

    public SubscribeTypeDetail price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getMaxTrip() {
        return this.maxTrip;
    }

    public SubscribeTypeDetail maxTrip(Integer maxTrip) {
        this.setMaxTrip(maxTrip);
        return this;
    }

    public void setMaxTrip(Integer maxTrip) {
        this.maxTrip = maxTrip;
    }

    public Integer getMaxItems() {
        return this.maxItems;
    }

    public SubscribeTypeDetail maxItems(Integer maxItems) {
        this.setMaxItems(maxItems);
        return this;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMaxRequest() {
        return this.maxRequest;
    }

    public SubscribeTypeDetail maxRequest(Integer maxRequest) {
        this.setMaxRequest(maxRequest);
        return this;
    }

    public void setMaxRequest(Integer maxRequest) {
        this.maxRequest = maxRequest;
    }

    public Integer getMaxNumberView() {
        return this.maxNumberView;
    }

    public SubscribeTypeDetail maxNumberView(Integer maxNumberView) {
        this.setMaxNumberView(maxNumberView);
        return this;
    }

    public void setMaxNumberView(Integer maxNumberView) {
        this.maxNumberView = maxNumberView;
    }

    public SubscribeType getSubscribeType() {
        return this.subscribeType;
    }

    public void setSubscribeType(SubscribeType subscribeType) {
        this.subscribeType = subscribeType;
    }

    public SubscribeTypeDetail subscribeType(SubscribeType subscribeType) {
        this.setSubscribeType(subscribeType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscribeTypeDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscribeTypeDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribeTypeDetail{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", maxTrip=" + getMaxTrip() +
            ", maxItems=" + getMaxItems() +
            ", maxRequest=" + getMaxRequest() +
            ", maxNumberView=" + getMaxNumberView() +
            "}";
    }
}

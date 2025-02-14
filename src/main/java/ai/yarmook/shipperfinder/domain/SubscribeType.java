package ai.yarmook.shipperfinder.domain;

import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscribeType.
 */
@Entity
@Table(name = "subscribe_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "subscribetype")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SubscribeTypeEnum type;

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

    @Lob
    @Column(name = "details")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String details;

    @Column(name = "details_en")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String detailsEn;

    @Column(name = "details_ar")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String detailsAr;

    @Column(name = "details_fr")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String detailsFr;

    @Column(name = "details_de")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String detailsDe;

    @Column(name = "details_urdu")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String detailsUrdu;

    @JsonIgnoreProperties(value = { "subscribeType" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "subscribeType")
    @org.springframework.data.annotation.Transient
    private SubscribeTypeDetail subscribeTypeDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscribeType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscribeTypeEnum getType() {
        return this.type;
    }

    public SubscribeType type(SubscribeTypeEnum type) {
        this.setType(type);
        return this;
    }

    public void setType(SubscribeTypeEnum type) {
        this.type = type;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public SubscribeType nameEn(String nameEn) {
        this.setNameEn(nameEn);
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public SubscribeType nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameFr() {
        return this.nameFr;
    }

    public SubscribeType nameFr(String nameFr) {
        this.setNameFr(nameFr);
        return this;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameDe() {
        return this.nameDe;
    }

    public SubscribeType nameDe(String nameDe) {
        this.setNameDe(nameDe);
        return this;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameUrdu() {
        return this.nameUrdu;
    }

    public SubscribeType nameUrdu(String nameUrdu) {
        this.setNameUrdu(nameUrdu);
        return this;
    }

    public void setNameUrdu(String nameUrdu) {
        this.nameUrdu = nameUrdu;
    }

    public String getDetails() {
        return this.details;
    }

    public SubscribeType details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetailsEn() {
        return this.detailsEn;
    }

    public SubscribeType detailsEn(String detailsEn) {
        this.setDetailsEn(detailsEn);
        return this;
    }

    public void setDetailsEn(String detailsEn) {
        this.detailsEn = detailsEn;
    }

    public String getDetailsAr() {
        return this.detailsAr;
    }

    public SubscribeType detailsAr(String detailsAr) {
        this.setDetailsAr(detailsAr);
        return this;
    }

    public void setDetailsAr(String detailsAr) {
        this.detailsAr = detailsAr;
    }

    public String getDetailsFr() {
        return this.detailsFr;
    }

    public SubscribeType detailsFr(String detailsFr) {
        this.setDetailsFr(detailsFr);
        return this;
    }

    public void setDetailsFr(String detailsFr) {
        this.detailsFr = detailsFr;
    }

    public String getDetailsDe() {
        return this.detailsDe;
    }

    public SubscribeType detailsDe(String detailsDe) {
        this.setDetailsDe(detailsDe);
        return this;
    }

    public void setDetailsDe(String detailsDe) {
        this.detailsDe = detailsDe;
    }

    public String getDetailsUrdu() {
        return this.detailsUrdu;
    }

    public SubscribeType detailsUrdu(String detailsUrdu) {
        this.setDetailsUrdu(detailsUrdu);
        return this;
    }

    public void setDetailsUrdu(String detailsUrdu) {
        this.detailsUrdu = detailsUrdu;
    }

    public SubscribeTypeDetail getSubscribeTypeDetail() {
        return this.subscribeTypeDetail;
    }

    public void setSubscribeTypeDetail(SubscribeTypeDetail subscribeTypeDetail) {
        if (this.subscribeTypeDetail != null) {
            this.subscribeTypeDetail.setSubscribeType(null);
        }
        if (subscribeTypeDetail != null) {
            subscribeTypeDetail.setSubscribeType(this);
        }
        this.subscribeTypeDetail = subscribeTypeDetail;
    }

    public SubscribeType subscribeTypeDetail(SubscribeTypeDetail subscribeTypeDetail) {
        this.setSubscribeTypeDetail(subscribeTypeDetail);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscribeType)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscribeType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribeType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", nameFr='" + getNameFr() + "'" +
            ", nameDe='" + getNameDe() + "'" +
            ", nameUrdu='" + getNameUrdu() + "'" +
            ", details='" + getDetails() + "'" +
            ", detailsEn='" + getDetailsEn() + "'" +
            ", detailsAr='" + getDetailsAr() + "'" +
            ", detailsFr='" + getDetailsFr() + "'" +
            ", detailsDe='" + getDetailsDe() + "'" +
            ", detailsUrdu='" + getDetailsUrdu() + "'" +
            "}";
    }
}

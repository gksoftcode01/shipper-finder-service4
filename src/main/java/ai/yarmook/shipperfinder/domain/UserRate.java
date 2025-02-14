package ai.yarmook.shipperfinder.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserRate.
 */
@Entity
@Table(name = "user_rate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userrate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rate")
    private Long rate;

    @Column(name = "note")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String note;

    @Column(name = "rate_date")
    private Instant rateDate;

    @Column(name = "rated_by_enc_id")
    private UUID ratedByEncId;

    @Column(name = "rated_enc_id")
    private UUID ratedEncId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserRate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRate() {
        return this.rate;
    }

    public UserRate rate(Long rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public String getNote() {
        return this.note;
    }

    public UserRate note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getRateDate() {
        return this.rateDate;
    }

    public UserRate rateDate(Instant rateDate) {
        this.setRateDate(rateDate);
        return this;
    }

    public void setRateDate(Instant rateDate) {
        this.rateDate = rateDate;
    }

    public UUID getRatedByEncId() {
        return this.ratedByEncId;
    }

    public UserRate ratedByEncId(UUID ratedByEncId) {
        this.setRatedByEncId(ratedByEncId);
        return this;
    }

    public void setRatedByEncId(UUID ratedByEncId) {
        this.ratedByEncId = ratedByEncId;
    }

    public UUID getRatedEncId() {
        return this.ratedEncId;
    }

    public UserRate ratedEncId(UUID ratedEncId) {
        this.setRatedEncId(ratedEncId);
        return this;
    }

    public void setRatedEncId(UUID ratedEncId) {
        this.ratedEncId = ratedEncId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRate)) {
            return false;
        }
        return getId() != null && getId().equals(((UserRate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRate{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", note='" + getNote() + "'" +
            ", rateDate='" + getRateDate() + "'" +
            ", ratedByEncId='" + getRatedByEncId() + "'" +
            ", ratedEncId='" + getRatedEncId() + "'" +
            "}";
    }
}

package ai.yarmook.shipperfinder.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShowNumberHistory.
 */
@Entity
@Table(name = "show_number_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shownumberhistory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShowNumberHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "action_by_enc_id")
    private UUID actionByEncId;

    @Column(name = "entity_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer entityType;

    @Column(name = "entity_enc_id")
    private UUID entityEncId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShowNumberHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ShowNumberHistory createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public UUID getActionByEncId() {
        return this.actionByEncId;
    }

    public ShowNumberHistory actionByEncId(UUID actionByEncId) {
        this.setActionByEncId(actionByEncId);
        return this;
    }

    public void setActionByEncId(UUID actionByEncId) {
        this.actionByEncId = actionByEncId;
    }

    public Integer getEntityType() {
        return this.entityType;
    }

    public ShowNumberHistory entityType(Integer entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public UUID getEntityEncId() {
        return this.entityEncId;
    }

    public ShowNumberHistory entityEncId(UUID entityEncId) {
        this.setEntityEncId(entityEncId);
        return this;
    }

    public void setEntityEncId(UUID entityEncId) {
        this.entityEncId = entityEncId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShowNumberHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ShowNumberHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowNumberHistory{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", actionByEncId='" + getActionByEncId() + "'" +
            ", entityType=" + getEntityType() +
            ", entityEncId='" + getEntityEncId() + "'" +
            "}";
    }
}

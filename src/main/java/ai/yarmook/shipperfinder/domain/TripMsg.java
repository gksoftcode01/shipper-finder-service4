package ai.yarmook.shipperfinder.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TripMsg.
 */
@Entity
@Table(name = "trip_msg")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tripmsg")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "msg")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String msg;

    @Column(name = "from_user_enc_id")
    private UUID fromUserEncId;

    @Column(name = "to_user_enc_id")
    private UUID toUserEncId;

    @Column(name = "trip_id")
    private Long tripId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TripMsg id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return this.msg;
    }

    public TripMsg msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UUID getFromUserEncId() {
        return this.fromUserEncId;
    }

    public TripMsg fromUserEncId(UUID fromUserEncId) {
        this.setFromUserEncId(fromUserEncId);
        return this;
    }

    public void setFromUserEncId(UUID fromUserEncId) {
        this.fromUserEncId = fromUserEncId;
    }

    public UUID getToUserEncId() {
        return this.toUserEncId;
    }

    public TripMsg toUserEncId(UUID toUserEncId) {
        this.setToUserEncId(toUserEncId);
        return this;
    }

    public void setToUserEncId(UUID toUserEncId) {
        this.toUserEncId = toUserEncId;
    }

    public Long getTripId() {
        return this.tripId;
    }

    public TripMsg tripId(Long tripId) {
        this.setTripId(tripId);
        return this;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripMsg)) {
            return false;
        }
        return getId() != null && getId().equals(((TripMsg) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripMsg{" +
            "id=" + getId() +
            ", msg='" + getMsg() + "'" +
            ", fromUserEncId='" + getFromUserEncId() + "'" +
            ", toUserEncId='" + getToUserEncId() + "'" +
            ", tripId=" + getTripId() +
            "}";
    }
}

package ai.yarmook.shipperfinder.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CargoMsg.
 */
@Entity
@Table(name = "cargo_msg")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cargomsg")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoMsg implements Serializable {

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

    @Column(name = "cargo_request_id")
    private Long cargoRequestId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CargoMsg id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return this.msg;
    }

    public CargoMsg msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UUID getFromUserEncId() {
        return this.fromUserEncId;
    }

    public CargoMsg fromUserEncId(UUID fromUserEncId) {
        this.setFromUserEncId(fromUserEncId);
        return this;
    }

    public void setFromUserEncId(UUID fromUserEncId) {
        this.fromUserEncId = fromUserEncId;
    }

    public UUID getToUserEncId() {
        return this.toUserEncId;
    }

    public CargoMsg toUserEncId(UUID toUserEncId) {
        this.setToUserEncId(toUserEncId);
        return this;
    }

    public void setToUserEncId(UUID toUserEncId) {
        this.toUserEncId = toUserEncId;
    }

    public Long getCargoRequestId() {
        return this.cargoRequestId;
    }

    public CargoMsg cargoRequestId(Long cargoRequestId) {
        this.setCargoRequestId(cargoRequestId);
        return this;
    }

    public void setCargoRequestId(Long cargoRequestId) {
        this.cargoRequestId = cargoRequestId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoMsg)) {
            return false;
        }
        return getId() != null && getId().equals(((CargoMsg) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoMsg{" +
            "id=" + getId() +
            ", msg='" + getMsg() + "'" +
            ", fromUserEncId='" + getFromUserEncId() + "'" +
            ", toUserEncId='" + getToUserEncId() + "'" +
            ", cargoRequestId=" + getCargoRequestId() +
            "}";
    }
}

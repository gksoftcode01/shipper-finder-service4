package ai.yarmook.shipperfinder.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUserDevice.
 */
@Entity
@Table(name = "app_user_device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appuserdevice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "device_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String deviceCode;

    @Column(name = "notification_token")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String notificationToken;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean active;

    @Column(name = "user_enc_id")
    private UUID userEncId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUserDevice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return this.deviceCode;
    }

    public AppUserDevice deviceCode(String deviceCode) {
        this.setDeviceCode(deviceCode);
        return this;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getNotificationToken() {
        return this.notificationToken;
    }

    public AppUserDevice notificationToken(String notificationToken) {
        this.setNotificationToken(notificationToken);
        return this;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public Instant getLastLogin() {
        return this.lastLogin;
    }

    public AppUserDevice lastLogin(Instant lastLogin) {
        this.setLastLogin(lastLogin);
        return this;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getActive() {
        return this.active;
    }

    public AppUserDevice active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UUID getUserEncId() {
        return this.userEncId;
    }

    public AppUserDevice userEncId(UUID userEncId) {
        this.setUserEncId(userEncId);
        return this;
    }

    public void setUserEncId(UUID userEncId) {
        this.userEncId = userEncId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDevice)) {
            return false;
        }
        return getId() != null && getId().equals(((AppUserDevice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDevice{" +
            "id=" + getId() +
            ", deviceCode='" + getDeviceCode() + "'" +
            ", notificationToken='" + getNotificationToken() + "'" +
            ", lastLogin='" + getLastLogin() + "'" +
            ", active='" + getActive() + "'" +
            ", userEncId='" + getUserEncId() + "'" +
            "}";
    }
}

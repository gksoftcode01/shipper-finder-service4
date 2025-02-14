package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.AppUserDevice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserDeviceDTO implements Serializable {

    private Long id;

    private String deviceCode;

    private String notificationToken;

    private Instant lastLogin;

    private Boolean active;

    private UUID userEncId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UUID getUserEncId() {
        return userEncId;
    }

    public void setUserEncId(UUID userEncId) {
        this.userEncId = userEncId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDeviceDTO)) {
            return false;
        }

        AppUserDeviceDTO appUserDeviceDTO = (AppUserDeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appUserDeviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDeviceDTO{" +
            "id=" + getId() +
            ", deviceCode='" + getDeviceCode() + "'" +
            ", notificationToken='" + getNotificationToken() + "'" +
            ", lastLogin='" + getLastLogin() + "'" +
            ", active='" + getActive() + "'" +
            ", userEncId='" + getUserEncId() + "'" +
            "}";
    }
}

package ai.yarmook.shipperfinder.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.AppUserDevice} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.AppUserDeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-user-devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserDeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deviceCode;

    private StringFilter notificationToken;

    private InstantFilter lastLogin;

    private BooleanFilter active;

    private UUIDFilter userEncId;

    private Boolean distinct;

    public AppUserDeviceCriteria() {}

    public AppUserDeviceCriteria(AppUserDeviceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.deviceCode = other.optionalDeviceCode().map(StringFilter::copy).orElse(null);
        this.notificationToken = other.optionalNotificationToken().map(StringFilter::copy).orElse(null);
        this.lastLogin = other.optionalLastLogin().map(InstantFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.userEncId = other.optionalUserEncId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppUserDeviceCriteria copy() {
        return new AppUserDeviceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDeviceCode() {
        return deviceCode;
    }

    public Optional<StringFilter> optionalDeviceCode() {
        return Optional.ofNullable(deviceCode);
    }

    public StringFilter deviceCode() {
        if (deviceCode == null) {
            setDeviceCode(new StringFilter());
        }
        return deviceCode;
    }

    public void setDeviceCode(StringFilter deviceCode) {
        this.deviceCode = deviceCode;
    }

    public StringFilter getNotificationToken() {
        return notificationToken;
    }

    public Optional<StringFilter> optionalNotificationToken() {
        return Optional.ofNullable(notificationToken);
    }

    public StringFilter notificationToken() {
        if (notificationToken == null) {
            setNotificationToken(new StringFilter());
        }
        return notificationToken;
    }

    public void setNotificationToken(StringFilter notificationToken) {
        this.notificationToken = notificationToken;
    }

    public InstantFilter getLastLogin() {
        return lastLogin;
    }

    public Optional<InstantFilter> optionalLastLogin() {
        return Optional.ofNullable(lastLogin);
    }

    public InstantFilter lastLogin() {
        if (lastLogin == null) {
            setLastLogin(new InstantFilter());
        }
        return lastLogin;
    }

    public void setLastLogin(InstantFilter lastLogin) {
        this.lastLogin = lastLogin;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public UUIDFilter getUserEncId() {
        return userEncId;
    }

    public Optional<UUIDFilter> optionalUserEncId() {
        return Optional.ofNullable(userEncId);
    }

    public UUIDFilter userEncId() {
        if (userEncId == null) {
            setUserEncId(new UUIDFilter());
        }
        return userEncId;
    }

    public void setUserEncId(UUIDFilter userEncId) {
        this.userEncId = userEncId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppUserDeviceCriteria that = (AppUserDeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deviceCode, that.deviceCode) &&
            Objects.equals(notificationToken, that.notificationToken) &&
            Objects.equals(lastLogin, that.lastLogin) &&
            Objects.equals(active, that.active) &&
            Objects.equals(userEncId, that.userEncId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deviceCode, notificationToken, lastLogin, active, userEncId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDeviceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDeviceCode().map(f -> "deviceCode=" + f + ", ").orElse("") +
            optionalNotificationToken().map(f -> "notificationToken=" + f + ", ").orElse("") +
            optionalLastLogin().map(f -> "lastLogin=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalUserEncId().map(f -> "userEncId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

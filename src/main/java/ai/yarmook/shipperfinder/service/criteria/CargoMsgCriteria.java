package ai.yarmook.shipperfinder.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.CargoMsg} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.CargoMsgResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cargo-msgs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoMsgCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter msg;

    private UUIDFilter fromUserEncId;

    private UUIDFilter toUserEncId;

    private LongFilter cargoRequestId;

    private Boolean distinct;

    public CargoMsgCriteria() {}

    public CargoMsgCriteria(CargoMsgCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.msg = other.optionalMsg().map(StringFilter::copy).orElse(null);
        this.fromUserEncId = other.optionalFromUserEncId().map(UUIDFilter::copy).orElse(null);
        this.toUserEncId = other.optionalToUserEncId().map(UUIDFilter::copy).orElse(null);
        this.cargoRequestId = other.optionalCargoRequestId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CargoMsgCriteria copy() {
        return new CargoMsgCriteria(this);
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

    public StringFilter getMsg() {
        return msg;
    }

    public Optional<StringFilter> optionalMsg() {
        return Optional.ofNullable(msg);
    }

    public StringFilter msg() {
        if (msg == null) {
            setMsg(new StringFilter());
        }
        return msg;
    }

    public void setMsg(StringFilter msg) {
        this.msg = msg;
    }

    public UUIDFilter getFromUserEncId() {
        return fromUserEncId;
    }

    public Optional<UUIDFilter> optionalFromUserEncId() {
        return Optional.ofNullable(fromUserEncId);
    }

    public UUIDFilter fromUserEncId() {
        if (fromUserEncId == null) {
            setFromUserEncId(new UUIDFilter());
        }
        return fromUserEncId;
    }

    public void setFromUserEncId(UUIDFilter fromUserEncId) {
        this.fromUserEncId = fromUserEncId;
    }

    public UUIDFilter getToUserEncId() {
        return toUserEncId;
    }

    public Optional<UUIDFilter> optionalToUserEncId() {
        return Optional.ofNullable(toUserEncId);
    }

    public UUIDFilter toUserEncId() {
        if (toUserEncId == null) {
            setToUserEncId(new UUIDFilter());
        }
        return toUserEncId;
    }

    public void setToUserEncId(UUIDFilter toUserEncId) {
        this.toUserEncId = toUserEncId;
    }

    public LongFilter getCargoRequestId() {
        return cargoRequestId;
    }

    public Optional<LongFilter> optionalCargoRequestId() {
        return Optional.ofNullable(cargoRequestId);
    }

    public LongFilter cargoRequestId() {
        if (cargoRequestId == null) {
            setCargoRequestId(new LongFilter());
        }
        return cargoRequestId;
    }

    public void setCargoRequestId(LongFilter cargoRequestId) {
        this.cargoRequestId = cargoRequestId;
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
        final CargoMsgCriteria that = (CargoMsgCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(msg, that.msg) &&
            Objects.equals(fromUserEncId, that.fromUserEncId) &&
            Objects.equals(toUserEncId, that.toUserEncId) &&
            Objects.equals(cargoRequestId, that.cargoRequestId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, msg, fromUserEncId, toUserEncId, cargoRequestId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoMsgCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMsg().map(f -> "msg=" + f + ", ").orElse("") +
            optionalFromUserEncId().map(f -> "fromUserEncId=" + f + ", ").orElse("") +
            optionalToUserEncId().map(f -> "toUserEncId=" + f + ", ").orElse("") +
            optionalCargoRequestId().map(f -> "cargoRequestId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

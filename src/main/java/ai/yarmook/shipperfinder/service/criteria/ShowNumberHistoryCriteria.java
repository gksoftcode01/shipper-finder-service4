package ai.yarmook.shipperfinder.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.ShowNumberHistory} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.ShowNumberHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /show-number-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShowNumberHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdDate;

    private UUIDFilter actionByEncId;

    private IntegerFilter entityType;

    private UUIDFilter entityEncId;

    private Boolean distinct;

    public ShowNumberHistoryCriteria() {}

    public ShowNumberHistoryCriteria(ShowNumberHistoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.actionByEncId = other.optionalActionByEncId().map(UUIDFilter::copy).orElse(null);
        this.entityType = other.optionalEntityType().map(IntegerFilter::copy).orElse(null);
        this.entityEncId = other.optionalEntityEncId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ShowNumberHistoryCriteria copy() {
        return new ShowNumberHistoryCriteria(this);
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

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public UUIDFilter getActionByEncId() {
        return actionByEncId;
    }

    public Optional<UUIDFilter> optionalActionByEncId() {
        return Optional.ofNullable(actionByEncId);
    }

    public UUIDFilter actionByEncId() {
        if (actionByEncId == null) {
            setActionByEncId(new UUIDFilter());
        }
        return actionByEncId;
    }

    public void setActionByEncId(UUIDFilter actionByEncId) {
        this.actionByEncId = actionByEncId;
    }

    public IntegerFilter getEntityType() {
        return entityType;
    }

    public Optional<IntegerFilter> optionalEntityType() {
        return Optional.ofNullable(entityType);
    }

    public IntegerFilter entityType() {
        if (entityType == null) {
            setEntityType(new IntegerFilter());
        }
        return entityType;
    }

    public void setEntityType(IntegerFilter entityType) {
        this.entityType = entityType;
    }

    public UUIDFilter getEntityEncId() {
        return entityEncId;
    }

    public Optional<UUIDFilter> optionalEntityEncId() {
        return Optional.ofNullable(entityEncId);
    }

    public UUIDFilter entityEncId() {
        if (entityEncId == null) {
            setEntityEncId(new UUIDFilter());
        }
        return entityEncId;
    }

    public void setEntityEncId(UUIDFilter entityEncId) {
        this.entityEncId = entityEncId;
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
        final ShowNumberHistoryCriteria that = (ShowNumberHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(actionByEncId, that.actionByEncId) &&
            Objects.equals(entityType, that.entityType) &&
            Objects.equals(entityEncId, that.entityEncId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, actionByEncId, entityType, entityEncId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowNumberHistoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalActionByEncId().map(f -> "actionByEncId=" + f + ", ").orElse("") +
            optionalEntityType().map(f -> "entityType=" + f + ", ").orElse("") +
            optionalEntityEncId().map(f -> "entityEncId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

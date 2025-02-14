package ai.yarmook.shipperfinder.service.criteria;

import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.SubscribeType} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.SubscribeTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscribe-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribeTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SubscribeTypeEnum
     */
    public static class SubscribeTypeEnumFilter extends Filter<SubscribeTypeEnum> {

        public SubscribeTypeEnumFilter() {}

        public SubscribeTypeEnumFilter(SubscribeTypeEnumFilter filter) {
            super(filter);
        }

        @Override
        public SubscribeTypeEnumFilter copy() {
            return new SubscribeTypeEnumFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private SubscribeTypeEnumFilter type;

    private StringFilter nameEn;

    private StringFilter nameAr;

    private StringFilter nameFr;

    private StringFilter nameDe;

    private StringFilter nameUrdu;

    private StringFilter detailsEn;

    private StringFilter detailsAr;

    private StringFilter detailsFr;

    private StringFilter detailsDe;

    private StringFilter detailsUrdu;

    private LongFilter subscribeTypeDetailId;

    private Boolean distinct;

    public SubscribeTypeCriteria() {}

    public SubscribeTypeCriteria(SubscribeTypeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(SubscribeTypeEnumFilter::copy).orElse(null);
        this.nameEn = other.optionalNameEn().map(StringFilter::copy).orElse(null);
        this.nameAr = other.optionalNameAr().map(StringFilter::copy).orElse(null);
        this.nameFr = other.optionalNameFr().map(StringFilter::copy).orElse(null);
        this.nameDe = other.optionalNameDe().map(StringFilter::copy).orElse(null);
        this.nameUrdu = other.optionalNameUrdu().map(StringFilter::copy).orElse(null);
        this.detailsEn = other.optionalDetailsEn().map(StringFilter::copy).orElse(null);
        this.detailsAr = other.optionalDetailsAr().map(StringFilter::copy).orElse(null);
        this.detailsFr = other.optionalDetailsFr().map(StringFilter::copy).orElse(null);
        this.detailsDe = other.optionalDetailsDe().map(StringFilter::copy).orElse(null);
        this.detailsUrdu = other.optionalDetailsUrdu().map(StringFilter::copy).orElse(null);
        this.subscribeTypeDetailId = other.optionalSubscribeTypeDetailId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SubscribeTypeCriteria copy() {
        return new SubscribeTypeCriteria(this);
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

    public SubscribeTypeEnumFilter getType() {
        return type;
    }

    public Optional<SubscribeTypeEnumFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public SubscribeTypeEnumFilter type() {
        if (type == null) {
            setType(new SubscribeTypeEnumFilter());
        }
        return type;
    }

    public void setType(SubscribeTypeEnumFilter type) {
        this.type = type;
    }

    public StringFilter getNameEn() {
        return nameEn;
    }

    public Optional<StringFilter> optionalNameEn() {
        return Optional.ofNullable(nameEn);
    }

    public StringFilter nameEn() {
        if (nameEn == null) {
            setNameEn(new StringFilter());
        }
        return nameEn;
    }

    public void setNameEn(StringFilter nameEn) {
        this.nameEn = nameEn;
    }

    public StringFilter getNameAr() {
        return nameAr;
    }

    public Optional<StringFilter> optionalNameAr() {
        return Optional.ofNullable(nameAr);
    }

    public StringFilter nameAr() {
        if (nameAr == null) {
            setNameAr(new StringFilter());
        }
        return nameAr;
    }

    public void setNameAr(StringFilter nameAr) {
        this.nameAr = nameAr;
    }

    public StringFilter getNameFr() {
        return nameFr;
    }

    public Optional<StringFilter> optionalNameFr() {
        return Optional.ofNullable(nameFr);
    }

    public StringFilter nameFr() {
        if (nameFr == null) {
            setNameFr(new StringFilter());
        }
        return nameFr;
    }

    public void setNameFr(StringFilter nameFr) {
        this.nameFr = nameFr;
    }

    public StringFilter getNameDe() {
        return nameDe;
    }

    public Optional<StringFilter> optionalNameDe() {
        return Optional.ofNullable(nameDe);
    }

    public StringFilter nameDe() {
        if (nameDe == null) {
            setNameDe(new StringFilter());
        }
        return nameDe;
    }

    public void setNameDe(StringFilter nameDe) {
        this.nameDe = nameDe;
    }

    public StringFilter getNameUrdu() {
        return nameUrdu;
    }

    public Optional<StringFilter> optionalNameUrdu() {
        return Optional.ofNullable(nameUrdu);
    }

    public StringFilter nameUrdu() {
        if (nameUrdu == null) {
            setNameUrdu(new StringFilter());
        }
        return nameUrdu;
    }

    public void setNameUrdu(StringFilter nameUrdu) {
        this.nameUrdu = nameUrdu;
    }

    public StringFilter getDetailsEn() {
        return detailsEn;
    }

    public Optional<StringFilter> optionalDetailsEn() {
        return Optional.ofNullable(detailsEn);
    }

    public StringFilter detailsEn() {
        if (detailsEn == null) {
            setDetailsEn(new StringFilter());
        }
        return detailsEn;
    }

    public void setDetailsEn(StringFilter detailsEn) {
        this.detailsEn = detailsEn;
    }

    public StringFilter getDetailsAr() {
        return detailsAr;
    }

    public Optional<StringFilter> optionalDetailsAr() {
        return Optional.ofNullable(detailsAr);
    }

    public StringFilter detailsAr() {
        if (detailsAr == null) {
            setDetailsAr(new StringFilter());
        }
        return detailsAr;
    }

    public void setDetailsAr(StringFilter detailsAr) {
        this.detailsAr = detailsAr;
    }

    public StringFilter getDetailsFr() {
        return detailsFr;
    }

    public Optional<StringFilter> optionalDetailsFr() {
        return Optional.ofNullable(detailsFr);
    }

    public StringFilter detailsFr() {
        if (detailsFr == null) {
            setDetailsFr(new StringFilter());
        }
        return detailsFr;
    }

    public void setDetailsFr(StringFilter detailsFr) {
        this.detailsFr = detailsFr;
    }

    public StringFilter getDetailsDe() {
        return detailsDe;
    }

    public Optional<StringFilter> optionalDetailsDe() {
        return Optional.ofNullable(detailsDe);
    }

    public StringFilter detailsDe() {
        if (detailsDe == null) {
            setDetailsDe(new StringFilter());
        }
        return detailsDe;
    }

    public void setDetailsDe(StringFilter detailsDe) {
        this.detailsDe = detailsDe;
    }

    public StringFilter getDetailsUrdu() {
        return detailsUrdu;
    }

    public Optional<StringFilter> optionalDetailsUrdu() {
        return Optional.ofNullable(detailsUrdu);
    }

    public StringFilter detailsUrdu() {
        if (detailsUrdu == null) {
            setDetailsUrdu(new StringFilter());
        }
        return detailsUrdu;
    }

    public void setDetailsUrdu(StringFilter detailsUrdu) {
        this.detailsUrdu = detailsUrdu;
    }

    public LongFilter getSubscribeTypeDetailId() {
        return subscribeTypeDetailId;
    }

    public Optional<LongFilter> optionalSubscribeTypeDetailId() {
        return Optional.ofNullable(subscribeTypeDetailId);
    }

    public LongFilter subscribeTypeDetailId() {
        if (subscribeTypeDetailId == null) {
            setSubscribeTypeDetailId(new LongFilter());
        }
        return subscribeTypeDetailId;
    }

    public void setSubscribeTypeDetailId(LongFilter subscribeTypeDetailId) {
        this.subscribeTypeDetailId = subscribeTypeDetailId;
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
        final SubscribeTypeCriteria that = (SubscribeTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(nameEn, that.nameEn) &&
            Objects.equals(nameAr, that.nameAr) &&
            Objects.equals(nameFr, that.nameFr) &&
            Objects.equals(nameDe, that.nameDe) &&
            Objects.equals(nameUrdu, that.nameUrdu) &&
            Objects.equals(detailsEn, that.detailsEn) &&
            Objects.equals(detailsAr, that.detailsAr) &&
            Objects.equals(detailsFr, that.detailsFr) &&
            Objects.equals(detailsDe, that.detailsDe) &&
            Objects.equals(detailsUrdu, that.detailsUrdu) &&
            Objects.equals(subscribeTypeDetailId, that.subscribeTypeDetailId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            type,
            nameEn,
            nameAr,
            nameFr,
            nameDe,
            nameUrdu,
            detailsEn,
            detailsAr,
            detailsFr,
            detailsDe,
            detailsUrdu,
            subscribeTypeDetailId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribeTypeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalNameEn().map(f -> "nameEn=" + f + ", ").orElse("") +
            optionalNameAr().map(f -> "nameAr=" + f + ", ").orElse("") +
            optionalNameFr().map(f -> "nameFr=" + f + ", ").orElse("") +
            optionalNameDe().map(f -> "nameDe=" + f + ", ").orElse("") +
            optionalNameUrdu().map(f -> "nameUrdu=" + f + ", ").orElse("") +
            optionalDetailsEn().map(f -> "detailsEn=" + f + ", ").orElse("") +
            optionalDetailsAr().map(f -> "detailsAr=" + f + ", ").orElse("") +
            optionalDetailsFr().map(f -> "detailsFr=" + f + ", ").orElse("") +
            optionalDetailsDe().map(f -> "detailsDe=" + f + ", ").orElse("") +
            optionalDetailsUrdu().map(f -> "detailsUrdu=" + f + ", ").orElse("") +
            optionalSubscribeTypeDetailId().map(f -> "subscribeTypeDetailId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

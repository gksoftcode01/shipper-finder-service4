package ai.yarmook.shipperfinder.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.Item} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.ItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter nameEn;

    private StringFilter nameAr;

    private StringFilter nameFr;

    private StringFilter nameDe;

    private StringFilter nameUrdu;

    private BooleanFilter isActive;

    private StringFilter defaultUOM;

    private LongFilter itemTypeId;

    private Boolean distinct;

    public ItemCriteria() {}

    public ItemCriteria(ItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.nameEn = other.optionalNameEn().map(StringFilter::copy).orElse(null);
        this.nameAr = other.optionalNameAr().map(StringFilter::copy).orElse(null);
        this.nameFr = other.optionalNameFr().map(StringFilter::copy).orElse(null);
        this.nameDe = other.optionalNameDe().map(StringFilter::copy).orElse(null);
        this.nameUrdu = other.optionalNameUrdu().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.defaultUOM = other.optionalDefaultUOM().map(StringFilter::copy).orElse(null);
        this.itemTypeId = other.optionalItemTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ItemCriteria copy() {
        return new ItemCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public StringFilter getDefaultUOM() {
        return defaultUOM;
    }

    public Optional<StringFilter> optionalDefaultUOM() {
        return Optional.ofNullable(defaultUOM);
    }

    public StringFilter defaultUOM() {
        if (defaultUOM == null) {
            setDefaultUOM(new StringFilter());
        }
        return defaultUOM;
    }

    public void setDefaultUOM(StringFilter defaultUOM) {
        this.defaultUOM = defaultUOM;
    }

    public LongFilter getItemTypeId() {
        return itemTypeId;
    }

    public Optional<LongFilter> optionalItemTypeId() {
        return Optional.ofNullable(itemTypeId);
    }

    public LongFilter itemTypeId() {
        if (itemTypeId == null) {
            setItemTypeId(new LongFilter());
        }
        return itemTypeId;
    }

    public void setItemTypeId(LongFilter itemTypeId) {
        this.itemTypeId = itemTypeId;
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
        final ItemCriteria that = (ItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nameEn, that.nameEn) &&
            Objects.equals(nameAr, that.nameAr) &&
            Objects.equals(nameFr, that.nameFr) &&
            Objects.equals(nameDe, that.nameDe) &&
            Objects.equals(nameUrdu, that.nameUrdu) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(defaultUOM, that.defaultUOM) &&
            Objects.equals(itemTypeId, that.itemTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameEn, nameAr, nameFr, nameDe, nameUrdu, isActive, defaultUOM, itemTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalNameEn().map(f -> "nameEn=" + f + ", ").orElse("") +
            optionalNameAr().map(f -> "nameAr=" + f + ", ").orElse("") +
            optionalNameFr().map(f -> "nameFr=" + f + ", ").orElse("") +
            optionalNameDe().map(f -> "nameDe=" + f + ", ").orElse("") +
            optionalNameUrdu().map(f -> "nameUrdu=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalDefaultUOM().map(f -> "defaultUOM=" + f + ", ").orElse("") +
            optionalItemTypeId().map(f -> "itemTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

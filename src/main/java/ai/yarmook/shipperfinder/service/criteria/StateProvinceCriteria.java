package ai.yarmook.shipperfinder.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.StateProvince} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.StateProvinceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /state-provinces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StateProvinceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter localName;

    private StringFilter isoCode;

    private LongFilter countryId;

    private Boolean distinct;

    public StateProvinceCriteria() {}

    public StateProvinceCriteria(StateProvinceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.localName = other.optionalLocalName().map(StringFilter::copy).orElse(null);
        this.isoCode = other.optionalIsoCode().map(StringFilter::copy).orElse(null);
        this.countryId = other.optionalCountryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StateProvinceCriteria copy() {
        return new StateProvinceCriteria(this);
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

    public StringFilter getLocalName() {
        return localName;
    }

    public Optional<StringFilter> optionalLocalName() {
        return Optional.ofNullable(localName);
    }

    public StringFilter localName() {
        if (localName == null) {
            setLocalName(new StringFilter());
        }
        return localName;
    }

    public void setLocalName(StringFilter localName) {
        this.localName = localName;
    }

    public StringFilter getIsoCode() {
        return isoCode;
    }

    public Optional<StringFilter> optionalIsoCode() {
        return Optional.ofNullable(isoCode);
    }

    public StringFilter isoCode() {
        if (isoCode == null) {
            setIsoCode(new StringFilter());
        }
        return isoCode;
    }

    public void setIsoCode(StringFilter isoCode) {
        this.isoCode = isoCode;
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public Optional<LongFilter> optionalCountryId() {
        return Optional.ofNullable(countryId);
    }

    public LongFilter countryId() {
        if (countryId == null) {
            setCountryId(new LongFilter());
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
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
        final StateProvinceCriteria that = (StateProvinceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(localName, that.localName) &&
            Objects.equals(isoCode, that.isoCode) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, localName, isoCode, countryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StateProvinceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalLocalName().map(f -> "localName=" + f + ", ").orElse("") +
            optionalIsoCode().map(f -> "isoCode=" + f + ", ").orElse("") +
            optionalCountryId().map(f -> "countryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

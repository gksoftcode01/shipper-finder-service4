package ai.yarmook.shipperfinder.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.Country} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.CountryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /countries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter localName;

    private StringFilter iso2;

    private StringFilter iso3;

    private StringFilter numericCode;

    private StringFilter phoneCode;

    private StringFilter currency;

    private StringFilter currencyName;

    private StringFilter currencySymbol;

    private StringFilter emoji;

    private StringFilter emojiU;

    private LongFilter stateProvinceId;

    private Boolean distinct;

    public CountryCriteria() {}

    public CountryCriteria(CountryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.localName = other.optionalLocalName().map(StringFilter::copy).orElse(null);
        this.iso2 = other.optionalIso2().map(StringFilter::copy).orElse(null);
        this.iso3 = other.optionalIso3().map(StringFilter::copy).orElse(null);
        this.numericCode = other.optionalNumericCode().map(StringFilter::copy).orElse(null);
        this.phoneCode = other.optionalPhoneCode().map(StringFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.currencyName = other.optionalCurrencyName().map(StringFilter::copy).orElse(null);
        this.currencySymbol = other.optionalCurrencySymbol().map(StringFilter::copy).orElse(null);
        this.emoji = other.optionalEmoji().map(StringFilter::copy).orElse(null);
        this.emojiU = other.optionalEmojiU().map(StringFilter::copy).orElse(null);
        this.stateProvinceId = other.optionalStateProvinceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CountryCriteria copy() {
        return new CountryCriteria(this);
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

    public StringFilter getIso2() {
        return iso2;
    }

    public Optional<StringFilter> optionalIso2() {
        return Optional.ofNullable(iso2);
    }

    public StringFilter iso2() {
        if (iso2 == null) {
            setIso2(new StringFilter());
        }
        return iso2;
    }

    public void setIso2(StringFilter iso2) {
        this.iso2 = iso2;
    }

    public StringFilter getIso3() {
        return iso3;
    }

    public Optional<StringFilter> optionalIso3() {
        return Optional.ofNullable(iso3);
    }

    public StringFilter iso3() {
        if (iso3 == null) {
            setIso3(new StringFilter());
        }
        return iso3;
    }

    public void setIso3(StringFilter iso3) {
        this.iso3 = iso3;
    }

    public StringFilter getNumericCode() {
        return numericCode;
    }

    public Optional<StringFilter> optionalNumericCode() {
        return Optional.ofNullable(numericCode);
    }

    public StringFilter numericCode() {
        if (numericCode == null) {
            setNumericCode(new StringFilter());
        }
        return numericCode;
    }

    public void setNumericCode(StringFilter numericCode) {
        this.numericCode = numericCode;
    }

    public StringFilter getPhoneCode() {
        return phoneCode;
    }

    public Optional<StringFilter> optionalPhoneCode() {
        return Optional.ofNullable(phoneCode);
    }

    public StringFilter phoneCode() {
        if (phoneCode == null) {
            setPhoneCode(new StringFilter());
        }
        return phoneCode;
    }

    public void setPhoneCode(StringFilter phoneCode) {
        this.phoneCode = phoneCode;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public Optional<StringFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public StringFilter currency() {
        if (currency == null) {
            setCurrency(new StringFilter());
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getCurrencyName() {
        return currencyName;
    }

    public Optional<StringFilter> optionalCurrencyName() {
        return Optional.ofNullable(currencyName);
    }

    public StringFilter currencyName() {
        if (currencyName == null) {
            setCurrencyName(new StringFilter());
        }
        return currencyName;
    }

    public void setCurrencyName(StringFilter currencyName) {
        this.currencyName = currencyName;
    }

    public StringFilter getCurrencySymbol() {
        return currencySymbol;
    }

    public Optional<StringFilter> optionalCurrencySymbol() {
        return Optional.ofNullable(currencySymbol);
    }

    public StringFilter currencySymbol() {
        if (currencySymbol == null) {
            setCurrencySymbol(new StringFilter());
        }
        return currencySymbol;
    }

    public void setCurrencySymbol(StringFilter currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public StringFilter getEmoji() {
        return emoji;
    }

    public Optional<StringFilter> optionalEmoji() {
        return Optional.ofNullable(emoji);
    }

    public StringFilter emoji() {
        if (emoji == null) {
            setEmoji(new StringFilter());
        }
        return emoji;
    }

    public void setEmoji(StringFilter emoji) {
        this.emoji = emoji;
    }

    public StringFilter getEmojiU() {
        return emojiU;
    }

    public Optional<StringFilter> optionalEmojiU() {
        return Optional.ofNullable(emojiU);
    }

    public StringFilter emojiU() {
        if (emojiU == null) {
            setEmojiU(new StringFilter());
        }
        return emojiU;
    }

    public void setEmojiU(StringFilter emojiU) {
        this.emojiU = emojiU;
    }

    public LongFilter getStateProvinceId() {
        return stateProvinceId;
    }

    public Optional<LongFilter> optionalStateProvinceId() {
        return Optional.ofNullable(stateProvinceId);
    }

    public LongFilter stateProvinceId() {
        if (stateProvinceId == null) {
            setStateProvinceId(new LongFilter());
        }
        return stateProvinceId;
    }

    public void setStateProvinceId(LongFilter stateProvinceId) {
        this.stateProvinceId = stateProvinceId;
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
        final CountryCriteria that = (CountryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(localName, that.localName) &&
            Objects.equals(iso2, that.iso2) &&
            Objects.equals(iso3, that.iso3) &&
            Objects.equals(numericCode, that.numericCode) &&
            Objects.equals(phoneCode, that.phoneCode) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(currencyName, that.currencyName) &&
            Objects.equals(currencySymbol, that.currencySymbol) &&
            Objects.equals(emoji, that.emoji) &&
            Objects.equals(emojiU, that.emojiU) &&
            Objects.equals(stateProvinceId, that.stateProvinceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            localName,
            iso2,
            iso3,
            numericCode,
            phoneCode,
            currency,
            currencyName,
            currencySymbol,
            emoji,
            emojiU,
            stateProvinceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalLocalName().map(f -> "localName=" + f + ", ").orElse("") +
            optionalIso2().map(f -> "iso2=" + f + ", ").orElse("") +
            optionalIso3().map(f -> "iso3=" + f + ", ").orElse("") +
            optionalNumericCode().map(f -> "numericCode=" + f + ", ").orElse("") +
            optionalPhoneCode().map(f -> "phoneCode=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalCurrencyName().map(f -> "currencyName=" + f + ", ").orElse("") +
            optionalCurrencySymbol().map(f -> "currencySymbol=" + f + ", ").orElse("") +
            optionalEmoji().map(f -> "emoji=" + f + ", ").orElse("") +
            optionalEmojiU().map(f -> "emojiU=" + f + ", ").orElse("") +
            optionalStateProvinceId().map(f -> "stateProvinceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

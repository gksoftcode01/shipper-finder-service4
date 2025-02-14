package ai.yarmook.shipperfinder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "country")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Column(name = "local_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String localName;

    @Column(name = "iso_2")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iso2;

    @Column(name = "iso_3")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iso3;

    @Column(name = "numeric_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String numericCode;

    @Column(name = "phone_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String phoneCode;

    @Column(name = "currency")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String currency;

    @Column(name = "currency_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String currencyName;

    @Column(name = "currency_symbol")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String currencySymbol;

    @Column(name = "emoji")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String emoji;

    @Column(name = "emoji_u")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String emojiU;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private Set<StateProvince> stateProvinces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalName() {
        return this.localName;
    }

    public Country localName(String localName) {
        this.setLocalName(localName);
        return this;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getIso2() {
        return this.iso2;
    }

    public Country iso2(String iso2) {
        this.setIso2(iso2);
        return this;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return this.iso3;
    }

    public Country iso3(String iso3) {
        this.setIso3(iso3);
        return this;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumericCode() {
        return this.numericCode;
    }

    public Country numericCode(String numericCode) {
        this.setNumericCode(numericCode);
        return this;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }

    public String getPhoneCode() {
        return this.phoneCode;
    }

    public Country phoneCode(String phoneCode) {
        this.setPhoneCode(phoneCode);
        return this;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Country currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public Country currencyName(String currencyName) {
        this.setCurrencyName(currencyName);
        return this;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public Country currencySymbol(String currencySymbol) {
        this.setCurrencySymbol(currencySymbol);
        return this;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public Country emoji(String emoji) {
        this.setEmoji(emoji);
        return this;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getEmojiU() {
        return this.emojiU;
    }

    public Country emojiU(String emojiU) {
        this.setEmojiU(emojiU);
        return this;
    }

    public void setEmojiU(String emojiU) {
        this.emojiU = emojiU;
    }

    public Set<StateProvince> getStateProvinces() {
        return this.stateProvinces;
    }

    public void setStateProvinces(Set<StateProvince> stateProvinces) {
        if (this.stateProvinces != null) {
            this.stateProvinces.forEach(i -> i.setCountry(null));
        }
        if (stateProvinces != null) {
            stateProvinces.forEach(i -> i.setCountry(this));
        }
        this.stateProvinces = stateProvinces;
    }

    public Country stateProvinces(Set<StateProvince> stateProvinces) {
        this.setStateProvinces(stateProvinces);
        return this;
    }

    public Country addStateProvince(StateProvince stateProvince) {
        this.stateProvinces.add(stateProvince);
        stateProvince.setCountry(this);
        return this;
    }

    public Country removeStateProvince(StateProvince stateProvince) {
        this.stateProvinces.remove(stateProvince);
        stateProvince.setCountry(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return getId() != null && getId().equals(((Country) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", localName='" + getLocalName() + "'" +
            ", iso2='" + getIso2() + "'" +
            ", iso3='" + getIso3() + "'" +
            ", numericCode='" + getNumericCode() + "'" +
            ", phoneCode='" + getPhoneCode() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", currencyName='" + getCurrencyName() + "'" +
            ", currencySymbol='" + getCurrencySymbol() + "'" +
            ", emoji='" + getEmoji() + "'" +
            ", emojiU='" + getEmojiU() + "'" +
            "}";
    }
}

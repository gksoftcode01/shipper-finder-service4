package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.StateProvince} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StateProvinceDTO implements Serializable {

    private Long id;

    private String name;

    private String localName;

    private String isoCode;

    private CountryDTO country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StateProvinceDTO)) {
            return false;
        }

        StateProvinceDTO stateProvinceDTO = (StateProvinceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stateProvinceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StateProvinceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", localName='" + getLocalName() + "'" +
            ", isoCode='" + getIsoCode() + "'" +
            ", country=" + getCountry() +
            "}";
    }
}

package ai.yarmook.shipperfinder.service.dto;

import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.SubscribeType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribeTypeDTO implements Serializable {

    private Long id;

    private SubscribeTypeEnum type;

    private String nameEn;

    private String nameAr;

    private String nameFr;

    private String nameDe;

    private String nameUrdu;

    @Lob
    private String details;

    private String detailsEn;

    private String detailsAr;

    private String detailsFr;

    private String detailsDe;

    private String detailsUrdu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscribeTypeEnum getType() {
        return type;
    }

    public void setType(SubscribeTypeEnum type) {
        this.type = type;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameUrdu() {
        return nameUrdu;
    }

    public void setNameUrdu(String nameUrdu) {
        this.nameUrdu = nameUrdu;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetailsEn() {
        return detailsEn;
    }

    public void setDetailsEn(String detailsEn) {
        this.detailsEn = detailsEn;
    }

    public String getDetailsAr() {
        return detailsAr;
    }

    public void setDetailsAr(String detailsAr) {
        this.detailsAr = detailsAr;
    }

    public String getDetailsFr() {
        return detailsFr;
    }

    public void setDetailsFr(String detailsFr) {
        this.detailsFr = detailsFr;
    }

    public String getDetailsDe() {
        return detailsDe;
    }

    public void setDetailsDe(String detailsDe) {
        this.detailsDe = detailsDe;
    }

    public String getDetailsUrdu() {
        return detailsUrdu;
    }

    public void setDetailsUrdu(String detailsUrdu) {
        this.detailsUrdu = detailsUrdu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscribeTypeDTO)) {
            return false;
        }

        SubscribeTypeDTO subscribeTypeDTO = (SubscribeTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscribeTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribeTypeDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", nameFr='" + getNameFr() + "'" +
            ", nameDe='" + getNameDe() + "'" +
            ", nameUrdu='" + getNameUrdu() + "'" +
            ", details='" + getDetails() + "'" +
            ", detailsEn='" + getDetailsEn() + "'" +
            ", detailsAr='" + getDetailsAr() + "'" +
            ", detailsFr='" + getDetailsFr() + "'" +
            ", detailsDe='" + getDetailsDe() + "'" +
            ", detailsUrdu='" + getDetailsUrdu() + "'" +
            "}";
    }
}

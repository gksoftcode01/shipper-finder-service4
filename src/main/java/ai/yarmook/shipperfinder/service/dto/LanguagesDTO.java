package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.Languages} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LanguagesDTO implements Serializable {

    private Long id;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LanguagesDTO)) {
            return false;
        }

        LanguagesDTO languagesDTO = (LanguagesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, languagesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguagesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

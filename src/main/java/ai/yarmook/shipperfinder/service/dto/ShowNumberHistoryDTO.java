package ai.yarmook.shipperfinder.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.ShowNumberHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShowNumberHistoryDTO implements Serializable {

    private Long id;

    private Instant createdDate;

    private UUID actionByEncId;

    private Integer entityType;

    private UUID entityEncId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public UUID getActionByEncId() {
        return actionByEncId;
    }

    public void setActionByEncId(UUID actionByEncId) {
        this.actionByEncId = actionByEncId;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public UUID getEntityEncId() {
        return entityEncId;
    }

    public void setEntityEncId(UUID entityEncId) {
        this.entityEncId = entityEncId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShowNumberHistoryDTO)) {
            return false;
        }

        ShowNumberHistoryDTO showNumberHistoryDTO = (ShowNumberHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, showNumberHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowNumberHistoryDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", actionByEncId='" + getActionByEncId() + "'" +
            ", entityType=" + getEntityType() +
            ", entityEncId='" + getEntityEncId() + "'" +
            "}";
    }
}

package ai.yarmook.shipperfinder.service.dto;

import ai.yarmook.shipperfinder.domain.enumeration.ReportStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.ReportAbuse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportAbuseDTO implements Serializable {

    private Long id;

    private UUID reportByEncId;

    private UUID reportedAgainstEncId;

    private Instant reportDate;

    private String reportData;

    private ReportStatus reportStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getReportByEncId() {
        return reportByEncId;
    }

    public void setReportByEncId(UUID reportByEncId) {
        this.reportByEncId = reportByEncId;
    }

    public UUID getReportedAgainstEncId() {
        return reportedAgainstEncId;
    }

    public void setReportedAgainstEncId(UUID reportedAgainstEncId) {
        this.reportedAgainstEncId = reportedAgainstEncId;
    }

    public Instant getReportDate() {
        return reportDate;
    }

    public void setReportDate(Instant reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportAbuseDTO)) {
            return false;
        }

        ReportAbuseDTO reportAbuseDTO = (ReportAbuseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportAbuseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportAbuseDTO{" +
            "id=" + getId() +
            ", reportByEncId='" + getReportByEncId() + "'" +
            ", reportedAgainstEncId='" + getReportedAgainstEncId() + "'" +
            ", reportDate='" + getReportDate() + "'" +
            ", reportData='" + getReportData() + "'" +
            ", reportStatus='" + getReportStatus() + "'" +
            "}";
    }
}

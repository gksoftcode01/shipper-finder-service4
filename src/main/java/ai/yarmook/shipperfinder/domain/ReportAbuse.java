package ai.yarmook.shipperfinder.domain;

import ai.yarmook.shipperfinder.domain.enumeration.ReportStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportAbuse.
 */
@Entity
@Table(name = "report_abuse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reportabuse")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportAbuse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "report_by_enc_id")
    private UUID reportByEncId;

    @Column(name = "reported_against_enc_id")
    private UUID reportedAgainstEncId;

    @Column(name = "report_date")
    private Instant reportDate;

    @Column(name = "report_data")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String reportData;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ReportStatus reportStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportAbuse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getReportByEncId() {
        return this.reportByEncId;
    }

    public ReportAbuse reportByEncId(UUID reportByEncId) {
        this.setReportByEncId(reportByEncId);
        return this;
    }

    public void setReportByEncId(UUID reportByEncId) {
        this.reportByEncId = reportByEncId;
    }

    public UUID getReportedAgainstEncId() {
        return this.reportedAgainstEncId;
    }

    public ReportAbuse reportedAgainstEncId(UUID reportedAgainstEncId) {
        this.setReportedAgainstEncId(reportedAgainstEncId);
        return this;
    }

    public void setReportedAgainstEncId(UUID reportedAgainstEncId) {
        this.reportedAgainstEncId = reportedAgainstEncId;
    }

    public Instant getReportDate() {
        return this.reportDate;
    }

    public ReportAbuse reportDate(Instant reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(Instant reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportData() {
        return this.reportData;
    }

    public ReportAbuse reportData(String reportData) {
        this.setReportData(reportData);
        return this;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public ReportStatus getReportStatus() {
        return this.reportStatus;
    }

    public ReportAbuse reportStatus(ReportStatus reportStatus) {
        this.setReportStatus(reportStatus);
        return this;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportAbuse)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportAbuse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportAbuse{" +
            "id=" + getId() +
            ", reportByEncId='" + getReportByEncId() + "'" +
            ", reportedAgainstEncId='" + getReportedAgainstEncId() + "'" +
            ", reportDate='" + getReportDate() + "'" +
            ", reportData='" + getReportData() + "'" +
            ", reportStatus='" + getReportStatus() + "'" +
            "}";
    }
}

package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportAbuseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportAbuseDTO.class);
        ReportAbuseDTO reportAbuseDTO1 = new ReportAbuseDTO();
        reportAbuseDTO1.setId(1L);
        ReportAbuseDTO reportAbuseDTO2 = new ReportAbuseDTO();
        assertThat(reportAbuseDTO1).isNotEqualTo(reportAbuseDTO2);
        reportAbuseDTO2.setId(reportAbuseDTO1.getId());
        assertThat(reportAbuseDTO1).isEqualTo(reportAbuseDTO2);
        reportAbuseDTO2.setId(2L);
        assertThat(reportAbuseDTO1).isNotEqualTo(reportAbuseDTO2);
        reportAbuseDTO1.setId(null);
        assertThat(reportAbuseDTO1).isNotEqualTo(reportAbuseDTO2);
    }
}

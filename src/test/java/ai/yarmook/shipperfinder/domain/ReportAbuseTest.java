package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.ReportAbuseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportAbuseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportAbuse.class);
        ReportAbuse reportAbuse1 = getReportAbuseSample1();
        ReportAbuse reportAbuse2 = new ReportAbuse();
        assertThat(reportAbuse1).isNotEqualTo(reportAbuse2);

        reportAbuse2.setId(reportAbuse1.getId());
        assertThat(reportAbuse1).isEqualTo(reportAbuse2);

        reportAbuse2 = getReportAbuseSample2();
        assertThat(reportAbuse1).isNotEqualTo(reportAbuse2);
    }
}

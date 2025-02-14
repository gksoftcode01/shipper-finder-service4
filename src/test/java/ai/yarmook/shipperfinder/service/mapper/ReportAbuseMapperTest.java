package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.ReportAbuseAsserts.*;
import static ai.yarmook.shipperfinder.domain.ReportAbuseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportAbuseMapperTest {

    private ReportAbuseMapper reportAbuseMapper;

    @BeforeEach
    void setUp() {
        reportAbuseMapper = new ReportAbuseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportAbuseSample1();
        var actual = reportAbuseMapper.toEntity(reportAbuseMapper.toDto(expected));
        assertReportAbuseAllPropertiesEquals(expected, actual);
    }
}

package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportAbuseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportAbuse getReportAbuseSample1() {
        return new ReportAbuse()
            .id(1L)
            .reportByEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .reportedAgainstEncId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .reportData("reportData1");
    }

    public static ReportAbuse getReportAbuseSample2() {
        return new ReportAbuse()
            .id(2L)
            .reportByEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .reportedAgainstEncId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .reportData("reportData2");
    }

    public static ReportAbuse getReportAbuseRandomSampleGenerator() {
        return new ReportAbuse()
            .id(longCount.incrementAndGet())
            .reportByEncId(UUID.randomUUID())
            .reportedAgainstEncId(UUID.randomUUID())
            .reportData(UUID.randomUUID().toString());
    }
}

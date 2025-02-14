package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.ReportAbuse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportAbuse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportAbuseRepository extends JpaRepository<ReportAbuse, Long> {}

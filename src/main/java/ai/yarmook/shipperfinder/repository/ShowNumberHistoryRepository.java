package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.ShowNumberHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShowNumberHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShowNumberHistoryRepository extends JpaRepository<ShowNumberHistory, Long>, JpaSpecificationExecutor<ShowNumberHistory> {}

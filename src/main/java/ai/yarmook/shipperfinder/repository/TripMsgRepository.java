package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.TripMsg;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TripMsg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripMsgRepository extends JpaRepository<TripMsg, Long>, JpaSpecificationExecutor<TripMsg> {}

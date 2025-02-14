package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.SubscribeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubscribeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscribeTypeRepository extends JpaRepository<SubscribeType, Long>, JpaSpecificationExecutor<SubscribeType> {}

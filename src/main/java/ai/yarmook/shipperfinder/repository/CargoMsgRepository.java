package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.CargoMsg;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CargoMsg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CargoMsgRepository extends JpaRepository<CargoMsg, Long>, JpaSpecificationExecutor<CargoMsg> {}

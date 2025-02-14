package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.ItemType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ItemType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemTypeRepository extends JpaRepository<ItemType, Long>, JpaSpecificationExecutor<ItemType> {}

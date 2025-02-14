package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.AppUserDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppUserDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUserDeviceRepository extends JpaRepository<AppUserDevice, Long>, JpaSpecificationExecutor<AppUserDevice> {}

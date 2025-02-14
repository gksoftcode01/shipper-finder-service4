package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.AppUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppUser entity.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {
    default Optional<AppUser> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AppUser> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AppUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select appUser from AppUser appUser left join fetch appUser.preferdLanguage left join fetch appUser.location",
        countQuery = "select count(appUser) from AppUser appUser"
    )
    Page<AppUser> findAllWithToOneRelationships(Pageable pageable);

    @Query("select appUser from AppUser appUser left join fetch appUser.preferdLanguage left join fetch appUser.location")
    List<AppUser> findAllWithToOneRelationships();

    @Query(
        "select appUser from AppUser appUser left join fetch appUser.preferdLanguage left join fetch appUser.location where appUser.id =:id"
    )
    Optional<AppUser> findOneWithToOneRelationships(@Param("id") Long id);
}

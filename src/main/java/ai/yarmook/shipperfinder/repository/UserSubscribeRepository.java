package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.UserSubscribe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSubscribe entity.
 */
@Repository
public interface UserSubscribeRepository extends JpaRepository<UserSubscribe, Long>, JpaSpecificationExecutor<UserSubscribe> {
    default Optional<UserSubscribe> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserSubscribe> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserSubscribe> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userSubscribe from UserSubscribe userSubscribe left join fetch userSubscribe.subscribeType",
        countQuery = "select count(userSubscribe) from UserSubscribe userSubscribe"
    )
    Page<UserSubscribe> findAllWithToOneRelationships(Pageable pageable);

    @Query("select userSubscribe from UserSubscribe userSubscribe left join fetch userSubscribe.subscribeType")
    List<UserSubscribe> findAllWithToOneRelationships();

    @Query("select userSubscribe from UserSubscribe userSubscribe left join fetch userSubscribe.subscribeType where userSubscribe.id =:id")
    Optional<UserSubscribe> findOneWithToOneRelationships(@Param("id") Long id);
}

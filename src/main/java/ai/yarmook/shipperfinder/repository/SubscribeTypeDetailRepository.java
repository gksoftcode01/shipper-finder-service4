package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.SubscribeTypeDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubscribeTypeDetail entity.
 */
@Repository
public interface SubscribeTypeDetailRepository extends JpaRepository<SubscribeTypeDetail, Long> {
    default Optional<SubscribeTypeDetail> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SubscribeTypeDetail> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SubscribeTypeDetail> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select subscribeTypeDetail from SubscribeTypeDetail subscribeTypeDetail left join fetch subscribeTypeDetail.subscribeType",
        countQuery = "select count(subscribeTypeDetail) from SubscribeTypeDetail subscribeTypeDetail"
    )
    Page<SubscribeTypeDetail> findAllWithToOneRelationships(Pageable pageable);

    @Query("select subscribeTypeDetail from SubscribeTypeDetail subscribeTypeDetail left join fetch subscribeTypeDetail.subscribeType")
    List<SubscribeTypeDetail> findAllWithToOneRelationships();

    @Query(
        "select subscribeTypeDetail from SubscribeTypeDetail subscribeTypeDetail left join fetch subscribeTypeDetail.subscribeType where subscribeTypeDetail.id =:id"
    )
    Optional<SubscribeTypeDetail> findOneWithToOneRelationships(@Param("id") Long id);
}

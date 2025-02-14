package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.TripItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TripItem entity.
 *
 * When extending this class, extend TripItemRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TripItemRepository
    extends TripItemRepositoryWithBagRelationships, JpaRepository<TripItem, Long>, JpaSpecificationExecutor<TripItem> {
    default Optional<TripItem> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<TripItem> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<TripItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select tripItem from TripItem tripItem left join fetch tripItem.item left join fetch tripItem.unit",
        countQuery = "select count(tripItem) from TripItem tripItem"
    )
    Page<TripItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tripItem from TripItem tripItem left join fetch tripItem.item left join fetch tripItem.unit")
    List<TripItem> findAllWithToOneRelationships();

    @Query("select tripItem from TripItem tripItem left join fetch tripItem.item left join fetch tripItem.unit where tripItem.id =:id")
    Optional<TripItem> findOneWithToOneRelationships(@Param("id") Long id);
}

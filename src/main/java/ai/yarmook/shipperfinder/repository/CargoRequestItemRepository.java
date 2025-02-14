package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CargoRequestItem entity.
 *
 * When extending this class, extend CargoRequestItemRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CargoRequestItemRepository
    extends
        CargoRequestItemRepositoryWithBagRelationships, JpaRepository<CargoRequestItem, Long>, JpaSpecificationExecutor<CargoRequestItem> {
    default Optional<CargoRequestItem> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<CargoRequestItem> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<CargoRequestItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select cargoRequestItem from CargoRequestItem cargoRequestItem left join fetch cargoRequestItem.item left join fetch cargoRequestItem.unit",
        countQuery = "select count(cargoRequestItem) from CargoRequestItem cargoRequestItem"
    )
    Page<CargoRequestItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cargoRequestItem from CargoRequestItem cargoRequestItem left join fetch cargoRequestItem.item left join fetch cargoRequestItem.unit"
    )
    List<CargoRequestItem> findAllWithToOneRelationships();

    @Query(
        "select cargoRequestItem from CargoRequestItem cargoRequestItem left join fetch cargoRequestItem.item left join fetch cargoRequestItem.unit where cargoRequestItem.id =:id"
    )
    Optional<CargoRequestItem> findOneWithToOneRelationships(@Param("id") Long id);
}

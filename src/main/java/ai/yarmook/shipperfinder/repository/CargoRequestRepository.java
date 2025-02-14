package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.CargoRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CargoRequest entity.
 */
@Repository
public interface CargoRequestRepository extends JpaRepository<CargoRequest, Long>, JpaSpecificationExecutor<CargoRequest> {
    default Optional<CargoRequest> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CargoRequest> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CargoRequest> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cargoRequest from CargoRequest cargoRequest left join fetch cargoRequest.fromCountry left join fetch cargoRequest.toCountry left join fetch cargoRequest.fromState left join fetch cargoRequest.toState",
        countQuery = "select count(cargoRequest) from CargoRequest cargoRequest"
    )
    Page<CargoRequest> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cargoRequest from CargoRequest cargoRequest left join fetch cargoRequest.fromCountry left join fetch cargoRequest.toCountry left join fetch cargoRequest.fromState left join fetch cargoRequest.toState"
    )
    List<CargoRequest> findAllWithToOneRelationships();

    @Query(
        "select cargoRequest from CargoRequest cargoRequest left join fetch cargoRequest.fromCountry left join fetch cargoRequest.toCountry left join fetch cargoRequest.fromState left join fetch cargoRequest.toState where cargoRequest.id =:id"
    )
    Optional<CargoRequest> findOneWithToOneRelationships(@Param("id") Long id);
}

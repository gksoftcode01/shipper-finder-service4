package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.StateProvince;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StateProvince entity.
 */
@Repository
public interface StateProvinceRepository extends JpaRepository<StateProvince, Long>, JpaSpecificationExecutor<StateProvince> {
    default Optional<StateProvince> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StateProvince> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StateProvince> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select stateProvince from StateProvince stateProvince left join fetch stateProvince.country",
        countQuery = "select count(stateProvince) from StateProvince stateProvince"
    )
    Page<StateProvince> findAllWithToOneRelationships(Pageable pageable);

    @Query("select stateProvince from StateProvince stateProvince left join fetch stateProvince.country")
    List<StateProvince> findAllWithToOneRelationships();

    @Query("select stateProvince from StateProvince stateProvince left join fetch stateProvince.country where stateProvince.id =:id")
    Optional<StateProvince> findOneWithToOneRelationships(@Param("id") Long id);
}

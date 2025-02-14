package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.Trip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Trip entity.
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
    default Optional<Trip> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Trip> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Trip> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select trip from Trip trip left join fetch trip.fromCountry left join fetch trip.toCountry left join fetch trip.fromState left join fetch trip.toState",
        countQuery = "select count(trip) from Trip trip"
    )
    Page<Trip> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select trip from Trip trip left join fetch trip.fromCountry left join fetch trip.toCountry left join fetch trip.fromState left join fetch trip.toState"
    )
    List<Trip> findAllWithToOneRelationships();

    @Query(
        "select trip from Trip trip left join fetch trip.fromCountry left join fetch trip.toCountry left join fetch trip.fromState left join fetch trip.toState where trip.id =:id"
    )
    Optional<Trip> findOneWithToOneRelationships(@Param("id") Long id);
}

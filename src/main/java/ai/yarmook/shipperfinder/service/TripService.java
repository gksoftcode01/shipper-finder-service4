package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.TripDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.Trip}.
 */
public interface TripService {
    /**
     * Save a trip.
     *
     * @param tripDTO the entity to save.
     * @return the persisted entity.
     */
    TripDTO save(TripDTO tripDTO);

    /**
     * Updates a trip.
     *
     * @param tripDTO the entity to update.
     * @return the persisted entity.
     */
    TripDTO update(TripDTO tripDTO);

    /**
     * Partially updates a trip.
     *
     * @param tripDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TripDTO> partialUpdate(TripDTO tripDTO);

    /**
     * Get all the trips with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TripDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" trip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TripDTO> findOne(Long id);

    /**
     * Delete the "id" trip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the trip corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TripDTO> search(String query, Pageable pageable);
}

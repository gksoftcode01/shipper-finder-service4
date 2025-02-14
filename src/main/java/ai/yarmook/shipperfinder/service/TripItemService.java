package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.TripItem}.
 */
public interface TripItemService {
    /**
     * Save a tripItem.
     *
     * @param tripItemDTO the entity to save.
     * @return the persisted entity.
     */
    TripItemDTO save(TripItemDTO tripItemDTO);

    /**
     * Updates a tripItem.
     *
     * @param tripItemDTO the entity to update.
     * @return the persisted entity.
     */
    TripItemDTO update(TripItemDTO tripItemDTO);

    /**
     * Partially updates a tripItem.
     *
     * @param tripItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TripItemDTO> partialUpdate(TripItemDTO tripItemDTO);

    /**
     * Get all the tripItems with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TripItemDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" tripItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TripItemDTO> findOne(Long id);

    /**
     * Delete the "id" tripItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tripItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TripItemDTO> search(String query, Pageable pageable);
}

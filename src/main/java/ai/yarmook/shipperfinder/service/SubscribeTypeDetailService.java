package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDetailDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.SubscribeTypeDetail}.
 */
public interface SubscribeTypeDetailService {
    /**
     * Save a subscribeTypeDetail.
     *
     * @param subscribeTypeDetailDTO the entity to save.
     * @return the persisted entity.
     */
    SubscribeTypeDetailDTO save(SubscribeTypeDetailDTO subscribeTypeDetailDTO);

    /**
     * Updates a subscribeTypeDetail.
     *
     * @param subscribeTypeDetailDTO the entity to update.
     * @return the persisted entity.
     */
    SubscribeTypeDetailDTO update(SubscribeTypeDetailDTO subscribeTypeDetailDTO);

    /**
     * Partially updates a subscribeTypeDetail.
     *
     * @param subscribeTypeDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubscribeTypeDetailDTO> partialUpdate(SubscribeTypeDetailDTO subscribeTypeDetailDTO);

    /**
     * Get all the subscribeTypeDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscribeTypeDetailDTO> findAll(Pageable pageable);

    /**
     * Get all the subscribeTypeDetails with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscribeTypeDetailDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" subscribeTypeDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscribeTypeDetailDTO> findOne(Long id);

    /**
     * Delete the "id" subscribeTypeDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the subscribeTypeDetail corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscribeTypeDetailDTO> search(String query, Pageable pageable);
}

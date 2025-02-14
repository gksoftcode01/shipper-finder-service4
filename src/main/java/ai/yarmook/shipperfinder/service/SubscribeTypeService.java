package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.SubscribeType}.
 */
public interface SubscribeTypeService {
    /**
     * Save a subscribeType.
     *
     * @param subscribeTypeDTO the entity to save.
     * @return the persisted entity.
     */
    SubscribeTypeDTO save(SubscribeTypeDTO subscribeTypeDTO);

    /**
     * Updates a subscribeType.
     *
     * @param subscribeTypeDTO the entity to update.
     * @return the persisted entity.
     */
    SubscribeTypeDTO update(SubscribeTypeDTO subscribeTypeDTO);

    /**
     * Partially updates a subscribeType.
     *
     * @param subscribeTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubscribeTypeDTO> partialUpdate(SubscribeTypeDTO subscribeTypeDTO);

    /**
     * Get all the SubscribeTypeDTO where SubscribeTypeDetail is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<SubscribeTypeDTO> findAllWhereSubscribeTypeDetailIsNull();

    /**
     * Get the "id" subscribeType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscribeTypeDTO> findOne(Long id);

    /**
     * Delete the "id" subscribeType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the subscribeType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscribeTypeDTO> search(String query, Pageable pageable);
}

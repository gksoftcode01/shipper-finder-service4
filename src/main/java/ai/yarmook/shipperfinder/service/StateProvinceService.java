package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.StateProvinceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.StateProvince}.
 */
public interface StateProvinceService {
    /**
     * Save a stateProvince.
     *
     * @param stateProvinceDTO the entity to save.
     * @return the persisted entity.
     */
    StateProvinceDTO save(StateProvinceDTO stateProvinceDTO);

    /**
     * Updates a stateProvince.
     *
     * @param stateProvinceDTO the entity to update.
     * @return the persisted entity.
     */
    StateProvinceDTO update(StateProvinceDTO stateProvinceDTO);

    /**
     * Partially updates a stateProvince.
     *
     * @param stateProvinceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StateProvinceDTO> partialUpdate(StateProvinceDTO stateProvinceDTO);

    /**
     * Get all the stateProvinces with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StateProvinceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" stateProvince.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StateProvinceDTO> findOne(Long id);

    /**
     * Delete the "id" stateProvince.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the stateProvince corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StateProvinceDTO> search(String query, Pageable pageable);
}

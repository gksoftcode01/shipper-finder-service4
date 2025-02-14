package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.CargoRequestItem}.
 */
public interface CargoRequestItemService {
    /**
     * Save a cargoRequestItem.
     *
     * @param cargoRequestItemDTO the entity to save.
     * @return the persisted entity.
     */
    CargoRequestItemDTO save(CargoRequestItemDTO cargoRequestItemDTO);

    /**
     * Updates a cargoRequestItem.
     *
     * @param cargoRequestItemDTO the entity to update.
     * @return the persisted entity.
     */
    CargoRequestItemDTO update(CargoRequestItemDTO cargoRequestItemDTO);

    /**
     * Partially updates a cargoRequestItem.
     *
     * @param cargoRequestItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CargoRequestItemDTO> partialUpdate(CargoRequestItemDTO cargoRequestItemDTO);

    /**
     * Get all the cargoRequestItems with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CargoRequestItemDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cargoRequestItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CargoRequestItemDTO> findOne(Long id);

    /**
     * Delete the "id" cargoRequestItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cargoRequestItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CargoRequestItemDTO> search(String query, Pageable pageable);
}

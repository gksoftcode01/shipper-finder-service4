package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.CargoRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.CargoRequest}.
 */
public interface CargoRequestService {
    /**
     * Save a cargoRequest.
     *
     * @param cargoRequestDTO the entity to save.
     * @return the persisted entity.
     */
    CargoRequestDTO save(CargoRequestDTO cargoRequestDTO);

    /**
     * Updates a cargoRequest.
     *
     * @param cargoRequestDTO the entity to update.
     * @return the persisted entity.
     */
    CargoRequestDTO update(CargoRequestDTO cargoRequestDTO);

    /**
     * Partially updates a cargoRequest.
     *
     * @param cargoRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CargoRequestDTO> partialUpdate(CargoRequestDTO cargoRequestDTO);

    /**
     * Get all the cargoRequests with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CargoRequestDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cargoRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CargoRequestDTO> findOne(Long id);

    /**
     * Delete the "id" cargoRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cargoRequest corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CargoRequestDTO> search(String query, Pageable pageable);
}

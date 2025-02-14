package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.CargoMsgDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.CargoMsg}.
 */
public interface CargoMsgService {
    /**
     * Save a cargoMsg.
     *
     * @param cargoMsgDTO the entity to save.
     * @return the persisted entity.
     */
    CargoMsgDTO save(CargoMsgDTO cargoMsgDTO);

    /**
     * Updates a cargoMsg.
     *
     * @param cargoMsgDTO the entity to update.
     * @return the persisted entity.
     */
    CargoMsgDTO update(CargoMsgDTO cargoMsgDTO);

    /**
     * Partially updates a cargoMsg.
     *
     * @param cargoMsgDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CargoMsgDTO> partialUpdate(CargoMsgDTO cargoMsgDTO);

    /**
     * Get the "id" cargoMsg.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CargoMsgDTO> findOne(Long id);

    /**
     * Delete the "id" cargoMsg.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cargoMsg corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CargoMsgDTO> search(String query, Pageable pageable);
}

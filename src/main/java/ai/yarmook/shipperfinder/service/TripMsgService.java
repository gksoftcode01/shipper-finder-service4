package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.TripMsgDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.TripMsg}.
 */
public interface TripMsgService {
    /**
     * Save a tripMsg.
     *
     * @param tripMsgDTO the entity to save.
     * @return the persisted entity.
     */
    TripMsgDTO save(TripMsgDTO tripMsgDTO);

    /**
     * Updates a tripMsg.
     *
     * @param tripMsgDTO the entity to update.
     * @return the persisted entity.
     */
    TripMsgDTO update(TripMsgDTO tripMsgDTO);

    /**
     * Partially updates a tripMsg.
     *
     * @param tripMsgDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TripMsgDTO> partialUpdate(TripMsgDTO tripMsgDTO);

    /**
     * Get the "id" tripMsg.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TripMsgDTO> findOne(Long id);

    /**
     * Delete the "id" tripMsg.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tripMsg corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TripMsgDTO> search(String query, Pageable pageable);
}

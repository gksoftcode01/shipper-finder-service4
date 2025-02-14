package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.ShowNumberHistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.ShowNumberHistory}.
 */
public interface ShowNumberHistoryService {
    /**
     * Save a showNumberHistory.
     *
     * @param showNumberHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    ShowNumberHistoryDTO save(ShowNumberHistoryDTO showNumberHistoryDTO);

    /**
     * Updates a showNumberHistory.
     *
     * @param showNumberHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    ShowNumberHistoryDTO update(ShowNumberHistoryDTO showNumberHistoryDTO);

    /**
     * Partially updates a showNumberHistory.
     *
     * @param showNumberHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShowNumberHistoryDTO> partialUpdate(ShowNumberHistoryDTO showNumberHistoryDTO);

    /**
     * Get the "id" showNumberHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShowNumberHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" showNumberHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the showNumberHistory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShowNumberHistoryDTO> search(String query, Pageable pageable);
}

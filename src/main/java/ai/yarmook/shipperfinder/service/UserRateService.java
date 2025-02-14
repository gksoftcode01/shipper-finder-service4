package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.UserRateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.UserRate}.
 */
public interface UserRateService {
    /**
     * Save a userRate.
     *
     * @param userRateDTO the entity to save.
     * @return the persisted entity.
     */
    UserRateDTO save(UserRateDTO userRateDTO);

    /**
     * Updates a userRate.
     *
     * @param userRateDTO the entity to update.
     * @return the persisted entity.
     */
    UserRateDTO update(UserRateDTO userRateDTO);

    /**
     * Partially updates a userRate.
     *
     * @param userRateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserRateDTO> partialUpdate(UserRateDTO userRateDTO);

    /**
     * Get all the userRates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserRateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userRate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserRateDTO> findOne(Long id);

    /**
     * Delete the "id" userRate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the userRate corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserRateDTO> search(String query, Pageable pageable);
}

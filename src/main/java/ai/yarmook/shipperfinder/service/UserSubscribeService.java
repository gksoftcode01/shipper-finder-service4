package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.UserSubscribe}.
 */
public interface UserSubscribeService {
    /**
     * Save a userSubscribe.
     *
     * @param userSubscribeDTO the entity to save.
     * @return the persisted entity.
     */
    UserSubscribeDTO save(UserSubscribeDTO userSubscribeDTO);

    /**
     * Updates a userSubscribe.
     *
     * @param userSubscribeDTO the entity to update.
     * @return the persisted entity.
     */
    UserSubscribeDTO update(UserSubscribeDTO userSubscribeDTO);

    /**
     * Partially updates a userSubscribe.
     *
     * @param userSubscribeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserSubscribeDTO> partialUpdate(UserSubscribeDTO userSubscribeDTO);

    /**
     * Get all the userSubscribes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserSubscribeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userSubscribe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserSubscribeDTO> findOne(Long id);

    /**
     * Delete the "id" userSubscribe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the userSubscribe corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserSubscribeDTO> search(String query, Pageable pageable);
}

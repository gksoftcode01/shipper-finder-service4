package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.LanguagesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.Languages}.
 */
public interface LanguagesService {
    /**
     * Save a languages.
     *
     * @param languagesDTO the entity to save.
     * @return the persisted entity.
     */
    LanguagesDTO save(LanguagesDTO languagesDTO);

    /**
     * Updates a languages.
     *
     * @param languagesDTO the entity to update.
     * @return the persisted entity.
     */
    LanguagesDTO update(LanguagesDTO languagesDTO);

    /**
     * Partially updates a languages.
     *
     * @param languagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LanguagesDTO> partialUpdate(LanguagesDTO languagesDTO);

    /**
     * Get all the languages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LanguagesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" languages.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LanguagesDTO> findOne(Long id);

    /**
     * Delete the "id" languages.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the languages corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LanguagesDTO> search(String query, Pageable pageable);
}

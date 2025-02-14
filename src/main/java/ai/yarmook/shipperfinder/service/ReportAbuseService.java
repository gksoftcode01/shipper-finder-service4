package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.ReportAbuseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.ReportAbuse}.
 */
public interface ReportAbuseService {
    /**
     * Save a reportAbuse.
     *
     * @param reportAbuseDTO the entity to save.
     * @return the persisted entity.
     */
    ReportAbuseDTO save(ReportAbuseDTO reportAbuseDTO);

    /**
     * Updates a reportAbuse.
     *
     * @param reportAbuseDTO the entity to update.
     * @return the persisted entity.
     */
    ReportAbuseDTO update(ReportAbuseDTO reportAbuseDTO);

    /**
     * Partially updates a reportAbuse.
     *
     * @param reportAbuseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportAbuseDTO> partialUpdate(ReportAbuseDTO reportAbuseDTO);

    /**
     * Get all the reportAbuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportAbuseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reportAbuse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportAbuseDTO> findOne(Long id);

    /**
     * Delete the "id" reportAbuse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reportAbuse corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportAbuseDTO> search(String query, Pageable pageable);
}

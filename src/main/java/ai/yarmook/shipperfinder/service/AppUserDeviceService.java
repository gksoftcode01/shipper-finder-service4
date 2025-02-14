package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.AppUserDevice}.
 */
public interface AppUserDeviceService {
    /**
     * Save a appUserDevice.
     *
     * @param appUserDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    AppUserDeviceDTO save(AppUserDeviceDTO appUserDeviceDTO);

    /**
     * Updates a appUserDevice.
     *
     * @param appUserDeviceDTO the entity to update.
     * @return the persisted entity.
     */
    AppUserDeviceDTO update(AppUserDeviceDTO appUserDeviceDTO);

    /**
     * Partially updates a appUserDevice.
     *
     * @param appUserDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppUserDeviceDTO> partialUpdate(AppUserDeviceDTO appUserDeviceDTO);

    /**
     * Get the "id" appUserDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUserDeviceDTO> findOne(Long id);

    /**
     * Delete the "id" appUserDevice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the appUserDevice corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppUserDeviceDTO> search(String query, Pageable pageable);
}

package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.AppUserDeviceRepository;
import ai.yarmook.shipperfinder.service.AppUserDeviceQueryService;
import ai.yarmook.shipperfinder.service.AppUserDeviceService;
import ai.yarmook.shipperfinder.service.criteria.AppUserDeviceCriteria;
import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import ai.yarmook.shipperfinder.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.AppUserDevice}.
 */
@RestController
@RequestMapping("/api/app-user-devices")
public class AppUserDeviceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserDeviceResource.class);

    private static final String ENTITY_NAME = "appUserDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppUserDeviceService appUserDeviceService;

    private final AppUserDeviceRepository appUserDeviceRepository;

    private final AppUserDeviceQueryService appUserDeviceQueryService;

    public AppUserDeviceResource(
        AppUserDeviceService appUserDeviceService,
        AppUserDeviceRepository appUserDeviceRepository,
        AppUserDeviceQueryService appUserDeviceQueryService
    ) {
        this.appUserDeviceService = appUserDeviceService;
        this.appUserDeviceRepository = appUserDeviceRepository;
        this.appUserDeviceQueryService = appUserDeviceQueryService;
    }

    /**
     * {@code POST  /app-user-devices} : Create a new appUserDevice.
     *
     * @param appUserDeviceDTO the appUserDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appUserDeviceDTO, or with status {@code 400 (Bad Request)} if the appUserDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppUserDeviceDTO> createAppUserDevice(@RequestBody AppUserDeviceDTO appUserDeviceDTO) throws URISyntaxException {
        LOG.debug("REST request to save AppUserDevice : {}", appUserDeviceDTO);
        if (appUserDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new appUserDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appUserDeviceDTO = appUserDeviceService.save(appUserDeviceDTO);
        return ResponseEntity.created(new URI("/api/app-user-devices/" + appUserDeviceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appUserDeviceDTO.getId().toString()))
            .body(appUserDeviceDTO);
    }

    /**
     * {@code PUT  /app-user-devices/:id} : Updates an existing appUserDevice.
     *
     * @param id the id of the appUserDeviceDTO to save.
     * @param appUserDeviceDTO the appUserDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appUserDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the appUserDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appUserDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppUserDeviceDTO> updateAppUserDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppUserDeviceDTO appUserDeviceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AppUserDevice : {}, {}", id, appUserDeviceDTO);
        if (appUserDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appUserDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appUserDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appUserDeviceDTO = appUserDeviceService.update(appUserDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appUserDeviceDTO.getId().toString()))
            .body(appUserDeviceDTO);
    }

    /**
     * {@code PATCH  /app-user-devices/:id} : Partial updates given fields of an existing appUserDevice, field will ignore if it is null
     *
     * @param id the id of the appUserDeviceDTO to save.
     * @param appUserDeviceDTO the appUserDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appUserDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the appUserDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appUserDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appUserDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppUserDeviceDTO> partialUpdateAppUserDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppUserDeviceDTO appUserDeviceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AppUserDevice partially : {}, {}", id, appUserDeviceDTO);
        if (appUserDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appUserDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appUserDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppUserDeviceDTO> result = appUserDeviceService.partialUpdate(appUserDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appUserDeviceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /app-user-devices} : get all the appUserDevices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appUserDevices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AppUserDeviceDTO>> getAllAppUserDevices(
        AppUserDeviceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AppUserDevices by criteria: {}", criteria);

        Page<AppUserDeviceDTO> page = appUserDeviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-user-devices/count} : count all the appUserDevices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAppUserDevices(AppUserDeviceCriteria criteria) {
        LOG.debug("REST request to count AppUserDevices by criteria: {}", criteria);
        return ResponseEntity.ok().body(appUserDeviceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /app-user-devices/:id} : get the "id" appUserDevice.
     *
     * @param id the id of the appUserDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appUserDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDeviceDTO> getAppUserDevice(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AppUserDevice : {}", id);
        Optional<AppUserDeviceDTO> appUserDeviceDTO = appUserDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appUserDeviceDTO);
    }

    /**
     * {@code DELETE  /app-user-devices/:id} : delete the "id" appUserDevice.
     *
     * @param id the id of the appUserDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppUserDevice(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AppUserDevice : {}", id);
        appUserDeviceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /app-user-devices/_search?query=:query} : search for the appUserDevice corresponding
     * to the query.
     *
     * @param query the query of the appUserDevice search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AppUserDeviceDTO>> searchAppUserDevices(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AppUserDevices for query {}", query);
        try {
            Page<AppUserDeviceDTO> page = appUserDeviceService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

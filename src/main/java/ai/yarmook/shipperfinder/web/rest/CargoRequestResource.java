package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.CargoRequestRepository;
import ai.yarmook.shipperfinder.service.CargoRequestQueryService;
import ai.yarmook.shipperfinder.service.CargoRequestService;
import ai.yarmook.shipperfinder.service.criteria.CargoRequestCriteria;
import ai.yarmook.shipperfinder.service.dto.CargoRequestDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.CargoRequest}.
 */
@RestController
@RequestMapping("/api/cargo-requests")
public class CargoRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(CargoRequestResource.class);

    private static final String ENTITY_NAME = "cargoRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CargoRequestService cargoRequestService;

    private final CargoRequestRepository cargoRequestRepository;

    private final CargoRequestQueryService cargoRequestQueryService;

    public CargoRequestResource(
        CargoRequestService cargoRequestService,
        CargoRequestRepository cargoRequestRepository,
        CargoRequestQueryService cargoRequestQueryService
    ) {
        this.cargoRequestService = cargoRequestService;
        this.cargoRequestRepository = cargoRequestRepository;
        this.cargoRequestQueryService = cargoRequestQueryService;
    }

    /**
     * {@code POST  /cargo-requests} : Create a new cargoRequest.
     *
     * @param cargoRequestDTO the cargoRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoRequestDTO, or with status {@code 400 (Bad Request)} if the cargoRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CargoRequestDTO> createCargoRequest(@RequestBody CargoRequestDTO cargoRequestDTO) throws URISyntaxException {
        LOG.debug("REST request to save CargoRequest : {}", cargoRequestDTO);
        if (cargoRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new cargoRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cargoRequestDTO = cargoRequestService.save(cargoRequestDTO);
        return ResponseEntity.created(new URI("/api/cargo-requests/" + cargoRequestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cargoRequestDTO.getId().toString()))
            .body(cargoRequestDTO);
    }

    /**
     * {@code PUT  /cargo-requests/:id} : Updates an existing cargoRequest.
     *
     * @param id the id of the cargoRequestDTO to save.
     * @param cargoRequestDTO the cargoRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequestDTO,
     * or with status {@code 400 (Bad Request)} if the cargoRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CargoRequestDTO> updateCargoRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequestDTO cargoRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CargoRequest : {}, {}", id, cargoRequestDTO);
        if (cargoRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cargoRequestDTO = cargoRequestService.update(cargoRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequestDTO.getId().toString()))
            .body(cargoRequestDTO);
    }

    /**
     * {@code PATCH  /cargo-requests/:id} : Partial updates given fields of an existing cargoRequest, field will ignore if it is null
     *
     * @param id the id of the cargoRequestDTO to save.
     * @param cargoRequestDTO the cargoRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequestDTO,
     * or with status {@code 400 (Bad Request)} if the cargoRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cargoRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CargoRequestDTO> partialUpdateCargoRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequestDTO cargoRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CargoRequest partially : {}, {}", id, cargoRequestDTO);
        if (cargoRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CargoRequestDTO> result = cargoRequestService.partialUpdate(cargoRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cargo-requests} : get all the cargoRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cargoRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CargoRequestDTO>> getAllCargoRequests(
        CargoRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CargoRequests by criteria: {}", criteria);

        Page<CargoRequestDTO> page = cargoRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cargo-requests/count} : count all the cargoRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCargoRequests(CargoRequestCriteria criteria) {
        LOG.debug("REST request to count CargoRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(cargoRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cargo-requests/:id} : get the "id" cargoRequest.
     *
     * @param id the id of the cargoRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CargoRequestDTO> getCargoRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CargoRequest : {}", id);
        Optional<CargoRequestDTO> cargoRequestDTO = cargoRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoRequestDTO);
    }

    /**
     * {@code DELETE  /cargo-requests/:id} : delete the "id" cargoRequest.
     *
     * @param id the id of the cargoRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargoRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CargoRequest : {}", id);
        cargoRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /cargo-requests/_search?query=:query} : search for the cargoRequest corresponding
     * to the query.
     *
     * @param query the query of the cargoRequest search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<CargoRequestDTO>> searchCargoRequests(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of CargoRequests for query {}", query);
        try {
            Page<CargoRequestDTO> page = cargoRequestService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

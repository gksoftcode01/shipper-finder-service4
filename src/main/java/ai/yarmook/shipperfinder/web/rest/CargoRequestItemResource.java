package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.CargoRequestItemRepository;
import ai.yarmook.shipperfinder.service.CargoRequestItemQueryService;
import ai.yarmook.shipperfinder.service.CargoRequestItemService;
import ai.yarmook.shipperfinder.service.criteria.CargoRequestItemCriteria;
import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.CargoRequestItem}.
 */
@RestController
@RequestMapping("/api/cargo-request-items")
public class CargoRequestItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(CargoRequestItemResource.class);

    private static final String ENTITY_NAME = "cargoRequestItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CargoRequestItemService cargoRequestItemService;

    private final CargoRequestItemRepository cargoRequestItemRepository;

    private final CargoRequestItemQueryService cargoRequestItemQueryService;

    public CargoRequestItemResource(
        CargoRequestItemService cargoRequestItemService,
        CargoRequestItemRepository cargoRequestItemRepository,
        CargoRequestItemQueryService cargoRequestItemQueryService
    ) {
        this.cargoRequestItemService = cargoRequestItemService;
        this.cargoRequestItemRepository = cargoRequestItemRepository;
        this.cargoRequestItemQueryService = cargoRequestItemQueryService;
    }

    /**
     * {@code POST  /cargo-request-items} : Create a new cargoRequestItem.
     *
     * @param cargoRequestItemDTO the cargoRequestItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoRequestItemDTO, or with status {@code 400 (Bad Request)} if the cargoRequestItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CargoRequestItemDTO> createCargoRequestItem(@RequestBody CargoRequestItemDTO cargoRequestItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CargoRequestItem : {}", cargoRequestItemDTO);
        if (cargoRequestItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new cargoRequestItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cargoRequestItemDTO = cargoRequestItemService.save(cargoRequestItemDTO);
        return ResponseEntity.created(new URI("/api/cargo-request-items/" + cargoRequestItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cargoRequestItemDTO.getId().toString()))
            .body(cargoRequestItemDTO);
    }

    /**
     * {@code PUT  /cargo-request-items/:id} : Updates an existing cargoRequestItem.
     *
     * @param id the id of the cargoRequestItemDTO to save.
     * @param cargoRequestItemDTO the cargoRequestItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequestItemDTO,
     * or with status {@code 400 (Bad Request)} if the cargoRequestItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequestItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CargoRequestItemDTO> updateCargoRequestItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequestItemDTO cargoRequestItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CargoRequestItem : {}, {}", id, cargoRequestItemDTO);
        if (cargoRequestItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequestItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cargoRequestItemDTO = cargoRequestItemService.update(cargoRequestItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequestItemDTO.getId().toString()))
            .body(cargoRequestItemDTO);
    }

    /**
     * {@code PATCH  /cargo-request-items/:id} : Partial updates given fields of an existing cargoRequestItem, field will ignore if it is null
     *
     * @param id the id of the cargoRequestItemDTO to save.
     * @param cargoRequestItemDTO the cargoRequestItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequestItemDTO,
     * or with status {@code 400 (Bad Request)} if the cargoRequestItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cargoRequestItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequestItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CargoRequestItemDTO> partialUpdateCargoRequestItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequestItemDTO cargoRequestItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CargoRequestItem partially : {}, {}", id, cargoRequestItemDTO);
        if (cargoRequestItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequestItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CargoRequestItemDTO> result = cargoRequestItemService.partialUpdate(cargoRequestItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequestItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cargo-request-items} : get all the cargoRequestItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cargoRequestItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CargoRequestItemDTO>> getAllCargoRequestItems(
        CargoRequestItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CargoRequestItems by criteria: {}", criteria);

        Page<CargoRequestItemDTO> page = cargoRequestItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cargo-request-items/count} : count all the cargoRequestItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCargoRequestItems(CargoRequestItemCriteria criteria) {
        LOG.debug("REST request to count CargoRequestItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(cargoRequestItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cargo-request-items/:id} : get the "id" cargoRequestItem.
     *
     * @param id the id of the cargoRequestItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoRequestItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CargoRequestItemDTO> getCargoRequestItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CargoRequestItem : {}", id);
        Optional<CargoRequestItemDTO> cargoRequestItemDTO = cargoRequestItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoRequestItemDTO);
    }

    /**
     * {@code DELETE  /cargo-request-items/:id} : delete the "id" cargoRequestItem.
     *
     * @param id the id of the cargoRequestItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargoRequestItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CargoRequestItem : {}", id);
        cargoRequestItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /cargo-request-items/_search?query=:query} : search for the cargoRequestItem corresponding
     * to the query.
     *
     * @param query the query of the cargoRequestItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<CargoRequestItemDTO>> searchCargoRequestItems(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of CargoRequestItems for query {}", query);
        try {
            Page<CargoRequestItemDTO> page = cargoRequestItemService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

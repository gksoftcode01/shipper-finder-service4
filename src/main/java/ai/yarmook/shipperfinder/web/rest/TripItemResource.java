package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.TripItemRepository;
import ai.yarmook.shipperfinder.service.TripItemQueryService;
import ai.yarmook.shipperfinder.service.TripItemService;
import ai.yarmook.shipperfinder.service.criteria.TripItemCriteria;
import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.TripItem}.
 */
@RestController
@RequestMapping("/api/trip-items")
public class TripItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(TripItemResource.class);

    private static final String ENTITY_NAME = "tripItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripItemService tripItemService;

    private final TripItemRepository tripItemRepository;

    private final TripItemQueryService tripItemQueryService;

    public TripItemResource(
        TripItemService tripItemService,
        TripItemRepository tripItemRepository,
        TripItemQueryService tripItemQueryService
    ) {
        this.tripItemService = tripItemService;
        this.tripItemRepository = tripItemRepository;
        this.tripItemQueryService = tripItemQueryService;
    }

    /**
     * {@code POST  /trip-items} : Create a new tripItem.
     *
     * @param tripItemDTO the tripItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripItemDTO, or with status {@code 400 (Bad Request)} if the tripItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TripItemDTO> createTripItem(@RequestBody TripItemDTO tripItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save TripItem : {}", tripItemDTO);
        if (tripItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new tripItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tripItemDTO = tripItemService.save(tripItemDTO);
        return ResponseEntity.created(new URI("/api/trip-items/" + tripItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tripItemDTO.getId().toString()))
            .body(tripItemDTO);
    }

    /**
     * {@code PUT  /trip-items/:id} : Updates an existing tripItem.
     *
     * @param id the id of the tripItemDTO to save.
     * @param tripItemDTO the tripItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripItemDTO,
     * or with status {@code 400 (Bad Request)} if the tripItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TripItemDTO> updateTripItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripItemDTO tripItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TripItem : {}, {}", id, tripItemDTO);
        if (tripItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tripItemDTO = tripItemService.update(tripItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripItemDTO.getId().toString()))
            .body(tripItemDTO);
    }

    /**
     * {@code PATCH  /trip-items/:id} : Partial updates given fields of an existing tripItem, field will ignore if it is null
     *
     * @param id the id of the tripItemDTO to save.
     * @param tripItemDTO the tripItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripItemDTO,
     * or with status {@code 400 (Bad Request)} if the tripItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tripItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripItemDTO> partialUpdateTripItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripItemDTO tripItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TripItem partially : {}, {}", id, tripItemDTO);
        if (tripItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripItemDTO> result = tripItemService.partialUpdate(tripItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trip-items} : get all the tripItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TripItemDTO>> getAllTripItems(
        TripItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TripItems by criteria: {}", criteria);

        Page<TripItemDTO> page = tripItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trip-items/count} : count all the tripItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTripItems(TripItemCriteria criteria) {
        LOG.debug("REST request to count TripItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(tripItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trip-items/:id} : get the "id" tripItem.
     *
     * @param id the id of the tripItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TripItemDTO> getTripItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TripItem : {}", id);
        Optional<TripItemDTO> tripItemDTO = tripItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripItemDTO);
    }

    /**
     * {@code DELETE  /trip-items/:id} : delete the "id" tripItem.
     *
     * @param id the id of the tripItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTripItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TripItem : {}", id);
        tripItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /trip-items/_search?query=:query} : search for the tripItem corresponding
     * to the query.
     *
     * @param query the query of the tripItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TripItemDTO>> searchTripItems(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of TripItems for query {}", query);
        try {
            Page<TripItemDTO> page = tripItemService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

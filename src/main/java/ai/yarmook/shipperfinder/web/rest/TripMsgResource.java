package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.TripMsgRepository;
import ai.yarmook.shipperfinder.service.TripMsgQueryService;
import ai.yarmook.shipperfinder.service.TripMsgService;
import ai.yarmook.shipperfinder.service.criteria.TripMsgCriteria;
import ai.yarmook.shipperfinder.service.dto.TripMsgDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.TripMsg}.
 */
@RestController
@RequestMapping("/api/trip-msgs")
public class TripMsgResource {

    private static final Logger LOG = LoggerFactory.getLogger(TripMsgResource.class);

    private static final String ENTITY_NAME = "tripMsg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripMsgService tripMsgService;

    private final TripMsgRepository tripMsgRepository;

    private final TripMsgQueryService tripMsgQueryService;

    public TripMsgResource(TripMsgService tripMsgService, TripMsgRepository tripMsgRepository, TripMsgQueryService tripMsgQueryService) {
        this.tripMsgService = tripMsgService;
        this.tripMsgRepository = tripMsgRepository;
        this.tripMsgQueryService = tripMsgQueryService;
    }

    /**
     * {@code POST  /trip-msgs} : Create a new tripMsg.
     *
     * @param tripMsgDTO the tripMsgDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripMsgDTO, or with status {@code 400 (Bad Request)} if the tripMsg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TripMsgDTO> createTripMsg(@RequestBody TripMsgDTO tripMsgDTO) throws URISyntaxException {
        LOG.debug("REST request to save TripMsg : {}", tripMsgDTO);
        if (tripMsgDTO.getId() != null) {
            throw new BadRequestAlertException("A new tripMsg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tripMsgDTO = tripMsgService.save(tripMsgDTO);
        return ResponseEntity.created(new URI("/api/trip-msgs/" + tripMsgDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tripMsgDTO.getId().toString()))
            .body(tripMsgDTO);
    }

    /**
     * {@code PUT  /trip-msgs/:id} : Updates an existing tripMsg.
     *
     * @param id the id of the tripMsgDTO to save.
     * @param tripMsgDTO the tripMsgDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripMsgDTO,
     * or with status {@code 400 (Bad Request)} if the tripMsgDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripMsgDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TripMsgDTO> updateTripMsg(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripMsgDTO tripMsgDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TripMsg : {}, {}", id, tripMsgDTO);
        if (tripMsgDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripMsgDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripMsgRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tripMsgDTO = tripMsgService.update(tripMsgDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripMsgDTO.getId().toString()))
            .body(tripMsgDTO);
    }

    /**
     * {@code PATCH  /trip-msgs/:id} : Partial updates given fields of an existing tripMsg, field will ignore if it is null
     *
     * @param id the id of the tripMsgDTO to save.
     * @param tripMsgDTO the tripMsgDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripMsgDTO,
     * or with status {@code 400 (Bad Request)} if the tripMsgDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tripMsgDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripMsgDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripMsgDTO> partialUpdateTripMsg(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripMsgDTO tripMsgDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TripMsg partially : {}, {}", id, tripMsgDTO);
        if (tripMsgDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripMsgDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripMsgRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripMsgDTO> result = tripMsgService.partialUpdate(tripMsgDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripMsgDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trip-msgs} : get all the tripMsgs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripMsgs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TripMsgDTO>> getAllTripMsgs(
        TripMsgCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TripMsgs by criteria: {}", criteria);

        Page<TripMsgDTO> page = tripMsgQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trip-msgs/count} : count all the tripMsgs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTripMsgs(TripMsgCriteria criteria) {
        LOG.debug("REST request to count TripMsgs by criteria: {}", criteria);
        return ResponseEntity.ok().body(tripMsgQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trip-msgs/:id} : get the "id" tripMsg.
     *
     * @param id the id of the tripMsgDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripMsgDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TripMsgDTO> getTripMsg(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TripMsg : {}", id);
        Optional<TripMsgDTO> tripMsgDTO = tripMsgService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripMsgDTO);
    }

    /**
     * {@code DELETE  /trip-msgs/:id} : delete the "id" tripMsg.
     *
     * @param id the id of the tripMsgDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTripMsg(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TripMsg : {}", id);
        tripMsgService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /trip-msgs/_search?query=:query} : search for the tripMsg corresponding
     * to the query.
     *
     * @param query the query of the tripMsg search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TripMsgDTO>> searchTripMsgs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of TripMsgs for query {}", query);
        try {
            Page<TripMsgDTO> page = tripMsgService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

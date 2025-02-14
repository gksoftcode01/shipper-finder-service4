package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.StateProvinceRepository;
import ai.yarmook.shipperfinder.service.StateProvinceQueryService;
import ai.yarmook.shipperfinder.service.StateProvinceService;
import ai.yarmook.shipperfinder.service.criteria.StateProvinceCriteria;
import ai.yarmook.shipperfinder.service.dto.StateProvinceDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.StateProvince}.
 */
@RestController
@RequestMapping("/api/state-provinces")
public class StateProvinceResource {

    private static final Logger LOG = LoggerFactory.getLogger(StateProvinceResource.class);

    private static final String ENTITY_NAME = "stateProvince";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StateProvinceService stateProvinceService;

    private final StateProvinceRepository stateProvinceRepository;

    private final StateProvinceQueryService stateProvinceQueryService;

    public StateProvinceResource(
        StateProvinceService stateProvinceService,
        StateProvinceRepository stateProvinceRepository,
        StateProvinceQueryService stateProvinceQueryService
    ) {
        this.stateProvinceService = stateProvinceService;
        this.stateProvinceRepository = stateProvinceRepository;
        this.stateProvinceQueryService = stateProvinceQueryService;
    }

    /**
     * {@code POST  /state-provinces} : Create a new stateProvince.
     *
     * @param stateProvinceDTO the stateProvinceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stateProvinceDTO, or with status {@code 400 (Bad Request)} if the stateProvince has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StateProvinceDTO> createStateProvince(@RequestBody StateProvinceDTO stateProvinceDTO) throws URISyntaxException {
        LOG.debug("REST request to save StateProvince : {}", stateProvinceDTO);
        if (stateProvinceDTO.getId() != null) {
            throw new BadRequestAlertException("A new stateProvince cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stateProvinceDTO = stateProvinceService.save(stateProvinceDTO);
        return ResponseEntity.created(new URI("/api/state-provinces/" + stateProvinceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, stateProvinceDTO.getId().toString()))
            .body(stateProvinceDTO);
    }

    /**
     * {@code PUT  /state-provinces/:id} : Updates an existing stateProvince.
     *
     * @param id the id of the stateProvinceDTO to save.
     * @param stateProvinceDTO the stateProvinceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateProvinceDTO,
     * or with status {@code 400 (Bad Request)} if the stateProvinceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stateProvinceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StateProvinceDTO> updateStateProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StateProvinceDTO stateProvinceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StateProvince : {}, {}", id, stateProvinceDTO);
        if (stateProvinceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateProvinceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateProvinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stateProvinceDTO = stateProvinceService.update(stateProvinceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateProvinceDTO.getId().toString()))
            .body(stateProvinceDTO);
    }

    /**
     * {@code PATCH  /state-provinces/:id} : Partial updates given fields of an existing stateProvince, field will ignore if it is null
     *
     * @param id the id of the stateProvinceDTO to save.
     * @param stateProvinceDTO the stateProvinceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateProvinceDTO,
     * or with status {@code 400 (Bad Request)} if the stateProvinceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stateProvinceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stateProvinceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StateProvinceDTO> partialUpdateStateProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StateProvinceDTO stateProvinceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StateProvince partially : {}, {}", id, stateProvinceDTO);
        if (stateProvinceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateProvinceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateProvinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StateProvinceDTO> result = stateProvinceService.partialUpdate(stateProvinceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateProvinceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /state-provinces} : get all the stateProvinces.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stateProvinces in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StateProvinceDTO>> getAllStateProvinces(
        StateProvinceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get StateProvinces by criteria: {}", criteria);

        Page<StateProvinceDTO> page = stateProvinceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /state-provinces/count} : count all the stateProvinces.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStateProvinces(StateProvinceCriteria criteria) {
        LOG.debug("REST request to count StateProvinces by criteria: {}", criteria);
        return ResponseEntity.ok().body(stateProvinceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /state-provinces/:id} : get the "id" stateProvince.
     *
     * @param id the id of the stateProvinceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stateProvinceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StateProvinceDTO> getStateProvince(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StateProvince : {}", id);
        Optional<StateProvinceDTO> stateProvinceDTO = stateProvinceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stateProvinceDTO);
    }

    /**
     * {@code DELETE  /state-provinces/:id} : delete the "id" stateProvince.
     *
     * @param id the id of the stateProvinceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStateProvince(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StateProvince : {}", id);
        stateProvinceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /state-provinces/_search?query=:query} : search for the stateProvince corresponding
     * to the query.
     *
     * @param query the query of the stateProvince search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<StateProvinceDTO>> searchStateProvinces(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of StateProvinces for query {}", query);
        try {
            Page<StateProvinceDTO> page = stateProvinceService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

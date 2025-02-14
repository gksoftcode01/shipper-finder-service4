package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.SubscribeTypeRepository;
import ai.yarmook.shipperfinder.service.SubscribeTypeQueryService;
import ai.yarmook.shipperfinder.service.SubscribeTypeService;
import ai.yarmook.shipperfinder.service.criteria.SubscribeTypeCriteria;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.SubscribeType}.
 */
@RestController
@RequestMapping("/api/subscribe-types")
public class SubscribeTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeTypeResource.class);

    private static final String ENTITY_NAME = "subscribeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscribeTypeService subscribeTypeService;

    private final SubscribeTypeRepository subscribeTypeRepository;

    private final SubscribeTypeQueryService subscribeTypeQueryService;

    public SubscribeTypeResource(
        SubscribeTypeService subscribeTypeService,
        SubscribeTypeRepository subscribeTypeRepository,
        SubscribeTypeQueryService subscribeTypeQueryService
    ) {
        this.subscribeTypeService = subscribeTypeService;
        this.subscribeTypeRepository = subscribeTypeRepository;
        this.subscribeTypeQueryService = subscribeTypeQueryService;
    }

    /**
     * {@code POST  /subscribe-types} : Create a new subscribeType.
     *
     * @param subscribeTypeDTO the subscribeTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscribeTypeDTO, or with status {@code 400 (Bad Request)} if the subscribeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscribeTypeDTO> createSubscribeType(@RequestBody SubscribeTypeDTO subscribeTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save SubscribeType : {}", subscribeTypeDTO);
        if (subscribeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscribeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscribeTypeDTO = subscribeTypeService.save(subscribeTypeDTO);
        return ResponseEntity.created(new URI("/api/subscribe-types/" + subscribeTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subscribeTypeDTO.getId().toString()))
            .body(subscribeTypeDTO);
    }

    /**
     * {@code PUT  /subscribe-types/:id} : Updates an existing subscribeType.
     *
     * @param id the id of the subscribeTypeDTO to save.
     * @param subscribeTypeDTO the subscribeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscribeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the subscribeTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscribeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscribeTypeDTO> updateSubscribeType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubscribeTypeDTO subscribeTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubscribeType : {}, {}", id, subscribeTypeDTO);
        if (subscribeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscribeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscribeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscribeTypeDTO = subscribeTypeService.update(subscribeTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscribeTypeDTO.getId().toString()))
            .body(subscribeTypeDTO);
    }

    /**
     * {@code PATCH  /subscribe-types/:id} : Partial updates given fields of an existing subscribeType, field will ignore if it is null
     *
     * @param id the id of the subscribeTypeDTO to save.
     * @param subscribeTypeDTO the subscribeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscribeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the subscribeTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscribeTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscribeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscribeTypeDTO> partialUpdateSubscribeType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubscribeTypeDTO subscribeTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubscribeType partially : {}, {}", id, subscribeTypeDTO);
        if (subscribeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscribeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscribeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscribeTypeDTO> result = subscribeTypeService.partialUpdate(subscribeTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscribeTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscribe-types} : get all the subscribeTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscribeTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubscribeTypeDTO>> getAllSubscribeTypes(
        SubscribeTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SubscribeTypes by criteria: {}", criteria);

        Page<SubscribeTypeDTO> page = subscribeTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscribe-types/count} : count all the subscribeTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubscribeTypes(SubscribeTypeCriteria criteria) {
        LOG.debug("REST request to count SubscribeTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(subscribeTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /subscribe-types/:id} : get the "id" subscribeType.
     *
     * @param id the id of the subscribeTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscribeTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscribeTypeDTO> getSubscribeType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubscribeType : {}", id);
        Optional<SubscribeTypeDTO> subscribeTypeDTO = subscribeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscribeTypeDTO);
    }

    /**
     * {@code DELETE  /subscribe-types/:id} : delete the "id" subscribeType.
     *
     * @param id the id of the subscribeTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscribeType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubscribeType : {}", id);
        subscribeTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /subscribe-types/_search?query=:query} : search for the subscribeType corresponding
     * to the query.
     *
     * @param query the query of the subscribeType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SubscribeTypeDTO>> searchSubscribeTypes(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of SubscribeTypes for query {}", query);
        try {
            Page<SubscribeTypeDTO> page = subscribeTypeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.SubscribeTypeDetailRepository;
import ai.yarmook.shipperfinder.service.SubscribeTypeDetailService;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDetailDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.SubscribeTypeDetail}.
 */
@RestController
@RequestMapping("/api/subscribe-type-details")
public class SubscribeTypeDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeTypeDetailResource.class);

    private static final String ENTITY_NAME = "subscribeTypeDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscribeTypeDetailService subscribeTypeDetailService;

    private final SubscribeTypeDetailRepository subscribeTypeDetailRepository;

    public SubscribeTypeDetailResource(
        SubscribeTypeDetailService subscribeTypeDetailService,
        SubscribeTypeDetailRepository subscribeTypeDetailRepository
    ) {
        this.subscribeTypeDetailService = subscribeTypeDetailService;
        this.subscribeTypeDetailRepository = subscribeTypeDetailRepository;
    }

    /**
     * {@code POST  /subscribe-type-details} : Create a new subscribeTypeDetail.
     *
     * @param subscribeTypeDetailDTO the subscribeTypeDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscribeTypeDetailDTO, or with status {@code 400 (Bad Request)} if the subscribeTypeDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscribeTypeDetailDTO> createSubscribeTypeDetail(@RequestBody SubscribeTypeDetailDTO subscribeTypeDetailDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SubscribeTypeDetail : {}", subscribeTypeDetailDTO);
        if (subscribeTypeDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscribeTypeDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscribeTypeDetailDTO = subscribeTypeDetailService.save(subscribeTypeDetailDTO);
        return ResponseEntity.created(new URI("/api/subscribe-type-details/" + subscribeTypeDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subscribeTypeDetailDTO.getId().toString()))
            .body(subscribeTypeDetailDTO);
    }

    /**
     * {@code PUT  /subscribe-type-details/:id} : Updates an existing subscribeTypeDetail.
     *
     * @param id the id of the subscribeTypeDetailDTO to save.
     * @param subscribeTypeDetailDTO the subscribeTypeDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscribeTypeDetailDTO,
     * or with status {@code 400 (Bad Request)} if the subscribeTypeDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscribeTypeDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscribeTypeDetailDTO> updateSubscribeTypeDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubscribeTypeDetailDTO subscribeTypeDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubscribeTypeDetail : {}, {}", id, subscribeTypeDetailDTO);
        if (subscribeTypeDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscribeTypeDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscribeTypeDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscribeTypeDetailDTO = subscribeTypeDetailService.update(subscribeTypeDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscribeTypeDetailDTO.getId().toString()))
            .body(subscribeTypeDetailDTO);
    }

    /**
     * {@code PATCH  /subscribe-type-details/:id} : Partial updates given fields of an existing subscribeTypeDetail, field will ignore if it is null
     *
     * @param id the id of the subscribeTypeDetailDTO to save.
     * @param subscribeTypeDetailDTO the subscribeTypeDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscribeTypeDetailDTO,
     * or with status {@code 400 (Bad Request)} if the subscribeTypeDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscribeTypeDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscribeTypeDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscribeTypeDetailDTO> partialUpdateSubscribeTypeDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubscribeTypeDetailDTO subscribeTypeDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubscribeTypeDetail partially : {}, {}", id, subscribeTypeDetailDTO);
        if (subscribeTypeDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscribeTypeDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscribeTypeDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscribeTypeDetailDTO> result = subscribeTypeDetailService.partialUpdate(subscribeTypeDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscribeTypeDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscribe-type-details} : get all the subscribeTypeDetails.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscribeTypeDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubscribeTypeDetailDTO>> getAllSubscribeTypeDetails(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of SubscribeTypeDetails");
        Page<SubscribeTypeDetailDTO> page;
        if (eagerload) {
            page = subscribeTypeDetailService.findAllWithEagerRelationships(pageable);
        } else {
            page = subscribeTypeDetailService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscribe-type-details/:id} : get the "id" subscribeTypeDetail.
     *
     * @param id the id of the subscribeTypeDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscribeTypeDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscribeTypeDetailDTO> getSubscribeTypeDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubscribeTypeDetail : {}", id);
        Optional<SubscribeTypeDetailDTO> subscribeTypeDetailDTO = subscribeTypeDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscribeTypeDetailDTO);
    }

    /**
     * {@code DELETE  /subscribe-type-details/:id} : delete the "id" subscribeTypeDetail.
     *
     * @param id the id of the subscribeTypeDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscribeTypeDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubscribeTypeDetail : {}", id);
        subscribeTypeDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /subscribe-type-details/_search?query=:query} : search for the subscribeTypeDetail corresponding
     * to the query.
     *
     * @param query the query of the subscribeTypeDetail search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SubscribeTypeDetailDTO>> searchSubscribeTypeDetails(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of SubscribeTypeDetails for query {}", query);
        try {
            Page<SubscribeTypeDetailDTO> page = subscribeTypeDetailService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

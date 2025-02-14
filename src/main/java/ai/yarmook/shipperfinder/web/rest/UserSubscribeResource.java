package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.UserSubscribeRepository;
import ai.yarmook.shipperfinder.service.UserSubscribeQueryService;
import ai.yarmook.shipperfinder.service.UserSubscribeService;
import ai.yarmook.shipperfinder.service.criteria.UserSubscribeCriteria;
import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.UserSubscribe}.
 */
@RestController
@RequestMapping("/api/user-subscribes")
public class UserSubscribeResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserSubscribeResource.class);

    private static final String ENTITY_NAME = "userSubscribe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSubscribeService userSubscribeService;

    private final UserSubscribeRepository userSubscribeRepository;

    private final UserSubscribeQueryService userSubscribeQueryService;

    public UserSubscribeResource(
        UserSubscribeService userSubscribeService,
        UserSubscribeRepository userSubscribeRepository,
        UserSubscribeQueryService userSubscribeQueryService
    ) {
        this.userSubscribeService = userSubscribeService;
        this.userSubscribeRepository = userSubscribeRepository;
        this.userSubscribeQueryService = userSubscribeQueryService;
    }

    /**
     * {@code POST  /user-subscribes} : Create a new userSubscribe.
     *
     * @param userSubscribeDTO the userSubscribeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSubscribeDTO, or with status {@code 400 (Bad Request)} if the userSubscribe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserSubscribeDTO> createUserSubscribe(@RequestBody UserSubscribeDTO userSubscribeDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserSubscribe : {}", userSubscribeDTO);
        if (userSubscribeDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSubscribe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userSubscribeDTO = userSubscribeService.save(userSubscribeDTO);
        return ResponseEntity.created(new URI("/api/user-subscribes/" + userSubscribeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userSubscribeDTO.getId().toString()))
            .body(userSubscribeDTO);
    }

    /**
     * {@code PUT  /user-subscribes/:id} : Updates an existing userSubscribe.
     *
     * @param id the id of the userSubscribeDTO to save.
     * @param userSubscribeDTO the userSubscribeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSubscribeDTO,
     * or with status {@code 400 (Bad Request)} if the userSubscribeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSubscribeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserSubscribeDTO> updateUserSubscribe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSubscribeDTO userSubscribeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserSubscribe : {}, {}", id, userSubscribeDTO);
        if (userSubscribeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSubscribeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSubscribeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userSubscribeDTO = userSubscribeService.update(userSubscribeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSubscribeDTO.getId().toString()))
            .body(userSubscribeDTO);
    }

    /**
     * {@code PATCH  /user-subscribes/:id} : Partial updates given fields of an existing userSubscribe, field will ignore if it is null
     *
     * @param id the id of the userSubscribeDTO to save.
     * @param userSubscribeDTO the userSubscribeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSubscribeDTO,
     * or with status {@code 400 (Bad Request)} if the userSubscribeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userSubscribeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSubscribeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSubscribeDTO> partialUpdateUserSubscribe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSubscribeDTO userSubscribeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserSubscribe partially : {}, {}", id, userSubscribeDTO);
        if (userSubscribeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSubscribeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSubscribeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSubscribeDTO> result = userSubscribeService.partialUpdate(userSubscribeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSubscribeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-subscribes} : get all the userSubscribes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSubscribes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserSubscribeDTO>> getAllUserSubscribes(
        UserSubscribeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get UserSubscribes by criteria: {}", criteria);

        Page<UserSubscribeDTO> page = userSubscribeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-subscribes/count} : count all the userSubscribes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserSubscribes(UserSubscribeCriteria criteria) {
        LOG.debug("REST request to count UserSubscribes by criteria: {}", criteria);
        return ResponseEntity.ok().body(userSubscribeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-subscribes/:id} : get the "id" userSubscribe.
     *
     * @param id the id of the userSubscribeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSubscribeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserSubscribeDTO> getUserSubscribe(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserSubscribe : {}", id);
        Optional<UserSubscribeDTO> userSubscribeDTO = userSubscribeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSubscribeDTO);
    }

    /**
     * {@code DELETE  /user-subscribes/:id} : delete the "id" userSubscribe.
     *
     * @param id the id of the userSubscribeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSubscribe(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserSubscribe : {}", id);
        userSubscribeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /user-subscribes/_search?query=:query} : search for the userSubscribe corresponding
     * to the query.
     *
     * @param query the query of the userSubscribe search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<UserSubscribeDTO>> searchUserSubscribes(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of UserSubscribes for query {}", query);
        try {
            Page<UserSubscribeDTO> page = userSubscribeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

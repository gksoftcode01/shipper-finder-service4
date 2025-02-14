package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.UserRateRepository;
import ai.yarmook.shipperfinder.service.UserRateService;
import ai.yarmook.shipperfinder.service.dto.UserRateDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.UserRate}.
 */
@RestController
@RequestMapping("/api/user-rates")
public class UserRateResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserRateResource.class);

    private static final String ENTITY_NAME = "userRate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRateService userRateService;

    private final UserRateRepository userRateRepository;

    public UserRateResource(UserRateService userRateService, UserRateRepository userRateRepository) {
        this.userRateService = userRateService;
        this.userRateRepository = userRateRepository;
    }

    /**
     * {@code POST  /user-rates} : Create a new userRate.
     *
     * @param userRateDTO the userRateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRateDTO, or with status {@code 400 (Bad Request)} if the userRate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserRateDTO> createUserRate(@RequestBody UserRateDTO userRateDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserRate : {}", userRateDTO);
        if (userRateDTO.getId() != null) {
            throw new BadRequestAlertException("A new userRate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userRateDTO = userRateService.save(userRateDTO);
        return ResponseEntity.created(new URI("/api/user-rates/" + userRateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userRateDTO.getId().toString()))
            .body(userRateDTO);
    }

    /**
     * {@code PUT  /user-rates/:id} : Updates an existing userRate.
     *
     * @param id the id of the userRateDTO to save.
     * @param userRateDTO the userRateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRateDTO,
     * or with status {@code 400 (Bad Request)} if the userRateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userRateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserRateDTO> updateUserRate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRateDTO userRateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserRate : {}, {}", id, userRateDTO);
        if (userRateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userRateDTO = userRateService.update(userRateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRateDTO.getId().toString()))
            .body(userRateDTO);
    }

    /**
     * {@code PATCH  /user-rates/:id} : Partial updates given fields of an existing userRate, field will ignore if it is null
     *
     * @param id the id of the userRateDTO to save.
     * @param userRateDTO the userRateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRateDTO,
     * or with status {@code 400 (Bad Request)} if the userRateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userRateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userRateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserRateDTO> partialUpdateUserRate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRateDTO userRateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserRate partially : {}, {}", id, userRateDTO);
        if (userRateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserRateDTO> result = userRateService.partialUpdate(userRateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-rates} : get all the userRates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userRates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserRateDTO>> getAllUserRates(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of UserRates");
        Page<UserRateDTO> page = userRateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-rates/:id} : get the "id" userRate.
     *
     * @param id the id of the userRateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userRateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserRateDTO> getUserRate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserRate : {}", id);
        Optional<UserRateDTO> userRateDTO = userRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userRateDTO);
    }

    /**
     * {@code DELETE  /user-rates/:id} : delete the "id" userRate.
     *
     * @param id the id of the userRateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserRate : {}", id);
        userRateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /user-rates/_search?query=:query} : search for the userRate corresponding
     * to the query.
     *
     * @param query the query of the userRate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<UserRateDTO>> searchUserRates(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of UserRates for query {}", query);
        try {
            Page<UserRateDTO> page = userRateService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

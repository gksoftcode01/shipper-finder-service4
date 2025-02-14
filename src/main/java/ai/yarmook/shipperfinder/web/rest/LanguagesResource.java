package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.LanguagesRepository;
import ai.yarmook.shipperfinder.service.LanguagesService;
import ai.yarmook.shipperfinder.service.dto.LanguagesDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.Languages}.
 */
@RestController
@RequestMapping("/api/languages")
public class LanguagesResource {

    private static final Logger LOG = LoggerFactory.getLogger(LanguagesResource.class);

    private static final String ENTITY_NAME = "languages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LanguagesService languagesService;

    private final LanguagesRepository languagesRepository;

    public LanguagesResource(LanguagesService languagesService, LanguagesRepository languagesRepository) {
        this.languagesService = languagesService;
        this.languagesRepository = languagesRepository;
    }

    /**
     * {@code POST  /languages} : Create a new languages.
     *
     * @param languagesDTO the languagesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new languagesDTO, or with status {@code 400 (Bad Request)} if the languages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LanguagesDTO> createLanguages(@RequestBody LanguagesDTO languagesDTO) throws URISyntaxException {
        LOG.debug("REST request to save Languages : {}", languagesDTO);
        if (languagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new languages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        languagesDTO = languagesService.save(languagesDTO);
        return ResponseEntity.created(new URI("/api/languages/" + languagesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, languagesDTO.getId().toString()))
            .body(languagesDTO);
    }

    /**
     * {@code PUT  /languages/:id} : Updates an existing languages.
     *
     * @param id the id of the languagesDTO to save.
     * @param languagesDTO the languagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated languagesDTO,
     * or with status {@code 400 (Bad Request)} if the languagesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the languagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LanguagesDTO> updateLanguages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LanguagesDTO languagesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Languages : {}, {}", id, languagesDTO);
        if (languagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, languagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        languagesDTO = languagesService.update(languagesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, languagesDTO.getId().toString()))
            .body(languagesDTO);
    }

    /**
     * {@code PATCH  /languages/:id} : Partial updates given fields of an existing languages, field will ignore if it is null
     *
     * @param id the id of the languagesDTO to save.
     * @param languagesDTO the languagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated languagesDTO,
     * or with status {@code 400 (Bad Request)} if the languagesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the languagesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the languagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LanguagesDTO> partialUpdateLanguages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LanguagesDTO languagesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Languages partially : {}, {}", id, languagesDTO);
        if (languagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, languagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LanguagesDTO> result = languagesService.partialUpdate(languagesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, languagesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /languages} : get all the languages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of languages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LanguagesDTO>> getAllLanguages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Languages");
        Page<LanguagesDTO> page = languagesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /languages/:id} : get the "id" languages.
     *
     * @param id the id of the languagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the languagesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LanguagesDTO> getLanguages(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Languages : {}", id);
        Optional<LanguagesDTO> languagesDTO = languagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(languagesDTO);
    }

    /**
     * {@code DELETE  /languages/:id} : delete the "id" languages.
     *
     * @param id the id of the languagesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguages(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Languages : {}", id);
        languagesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /languages/_search?query=:query} : search for the languages corresponding
     * to the query.
     *
     * @param query the query of the languages search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<LanguagesDTO>> searchLanguages(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Languages for query {}", query);
        try {
            Page<LanguagesDTO> page = languagesService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

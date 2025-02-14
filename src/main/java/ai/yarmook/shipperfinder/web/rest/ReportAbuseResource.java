package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.ReportAbuseRepository;
import ai.yarmook.shipperfinder.service.ReportAbuseService;
import ai.yarmook.shipperfinder.service.dto.ReportAbuseDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.ReportAbuse}.
 */
@RestController
@RequestMapping("/api/report-abuses")
public class ReportAbuseResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportAbuseResource.class);

    private static final String ENTITY_NAME = "reportAbuse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportAbuseService reportAbuseService;

    private final ReportAbuseRepository reportAbuseRepository;

    public ReportAbuseResource(ReportAbuseService reportAbuseService, ReportAbuseRepository reportAbuseRepository) {
        this.reportAbuseService = reportAbuseService;
        this.reportAbuseRepository = reportAbuseRepository;
    }

    /**
     * {@code POST  /report-abuses} : Create a new reportAbuse.
     *
     * @param reportAbuseDTO the reportAbuseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportAbuseDTO, or with status {@code 400 (Bad Request)} if the reportAbuse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportAbuseDTO> createReportAbuse(@RequestBody ReportAbuseDTO reportAbuseDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReportAbuse : {}", reportAbuseDTO);
        if (reportAbuseDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportAbuse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportAbuseDTO = reportAbuseService.save(reportAbuseDTO);
        return ResponseEntity.created(new URI("/api/report-abuses/" + reportAbuseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportAbuseDTO.getId().toString()))
            .body(reportAbuseDTO);
    }

    /**
     * {@code PUT  /report-abuses/:id} : Updates an existing reportAbuse.
     *
     * @param id the id of the reportAbuseDTO to save.
     * @param reportAbuseDTO the reportAbuseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportAbuseDTO,
     * or with status {@code 400 (Bad Request)} if the reportAbuseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportAbuseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportAbuseDTO> updateReportAbuse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportAbuseDTO reportAbuseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportAbuse : {}, {}", id, reportAbuseDTO);
        if (reportAbuseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportAbuseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportAbuseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportAbuseDTO = reportAbuseService.update(reportAbuseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportAbuseDTO.getId().toString()))
            .body(reportAbuseDTO);
    }

    /**
     * {@code PATCH  /report-abuses/:id} : Partial updates given fields of an existing reportAbuse, field will ignore if it is null
     *
     * @param id the id of the reportAbuseDTO to save.
     * @param reportAbuseDTO the reportAbuseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportAbuseDTO,
     * or with status {@code 400 (Bad Request)} if the reportAbuseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportAbuseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportAbuseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportAbuseDTO> partialUpdateReportAbuse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportAbuseDTO reportAbuseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportAbuse partially : {}, {}", id, reportAbuseDTO);
        if (reportAbuseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportAbuseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportAbuseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportAbuseDTO> result = reportAbuseService.partialUpdate(reportAbuseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportAbuseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-abuses} : get all the reportAbuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportAbuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportAbuseDTO>> getAllReportAbuses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ReportAbuses");
        Page<ReportAbuseDTO> page = reportAbuseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-abuses/:id} : get the "id" reportAbuse.
     *
     * @param id the id of the reportAbuseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportAbuseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportAbuseDTO> getReportAbuse(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportAbuse : {}", id);
        Optional<ReportAbuseDTO> reportAbuseDTO = reportAbuseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportAbuseDTO);
    }

    /**
     * {@code DELETE  /report-abuses/:id} : delete the "id" reportAbuse.
     *
     * @param id the id of the reportAbuseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportAbuse(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportAbuse : {}", id);
        reportAbuseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /report-abuses/_search?query=:query} : search for the reportAbuse corresponding
     * to the query.
     *
     * @param query the query of the reportAbuse search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ReportAbuseDTO>> searchReportAbuses(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ReportAbuses for query {}", query);
        try {
            Page<ReportAbuseDTO> page = reportAbuseService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

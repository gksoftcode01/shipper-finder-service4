package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.CargoMsgRepository;
import ai.yarmook.shipperfinder.service.CargoMsgQueryService;
import ai.yarmook.shipperfinder.service.CargoMsgService;
import ai.yarmook.shipperfinder.service.criteria.CargoMsgCriteria;
import ai.yarmook.shipperfinder.service.dto.CargoMsgDTO;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.CargoMsg}.
 */
@RestController
@RequestMapping("/api/cargo-msgs")
public class CargoMsgResource {

    private static final Logger LOG = LoggerFactory.getLogger(CargoMsgResource.class);

    private static final String ENTITY_NAME = "cargoMsg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CargoMsgService cargoMsgService;

    private final CargoMsgRepository cargoMsgRepository;

    private final CargoMsgQueryService cargoMsgQueryService;

    public CargoMsgResource(
        CargoMsgService cargoMsgService,
        CargoMsgRepository cargoMsgRepository,
        CargoMsgQueryService cargoMsgQueryService
    ) {
        this.cargoMsgService = cargoMsgService;
        this.cargoMsgRepository = cargoMsgRepository;
        this.cargoMsgQueryService = cargoMsgQueryService;
    }

    /**
     * {@code POST  /cargo-msgs} : Create a new cargoMsg.
     *
     * @param cargoMsgDTO the cargoMsgDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoMsgDTO, or with status {@code 400 (Bad Request)} if the cargoMsg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CargoMsgDTO> createCargoMsg(@RequestBody CargoMsgDTO cargoMsgDTO) throws URISyntaxException {
        LOG.debug("REST request to save CargoMsg : {}", cargoMsgDTO);
        if (cargoMsgDTO.getId() != null) {
            throw new BadRequestAlertException("A new cargoMsg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cargoMsgDTO = cargoMsgService.save(cargoMsgDTO);
        return ResponseEntity.created(new URI("/api/cargo-msgs/" + cargoMsgDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cargoMsgDTO.getId().toString()))
            .body(cargoMsgDTO);
    }

    /**
     * {@code PUT  /cargo-msgs/:id} : Updates an existing cargoMsg.
     *
     * @param id the id of the cargoMsgDTO to save.
     * @param cargoMsgDTO the cargoMsgDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoMsgDTO,
     * or with status {@code 400 (Bad Request)} if the cargoMsgDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoMsgDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CargoMsgDTO> updateCargoMsg(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoMsgDTO cargoMsgDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CargoMsg : {}, {}", id, cargoMsgDTO);
        if (cargoMsgDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoMsgDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoMsgRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cargoMsgDTO = cargoMsgService.update(cargoMsgDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoMsgDTO.getId().toString()))
            .body(cargoMsgDTO);
    }

    /**
     * {@code PATCH  /cargo-msgs/:id} : Partial updates given fields of an existing cargoMsg, field will ignore if it is null
     *
     * @param id the id of the cargoMsgDTO to save.
     * @param cargoMsgDTO the cargoMsgDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoMsgDTO,
     * or with status {@code 400 (Bad Request)} if the cargoMsgDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cargoMsgDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoMsgDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CargoMsgDTO> partialUpdateCargoMsg(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoMsgDTO cargoMsgDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CargoMsg partially : {}, {}", id, cargoMsgDTO);
        if (cargoMsgDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoMsgDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoMsgRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CargoMsgDTO> result = cargoMsgService.partialUpdate(cargoMsgDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoMsgDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cargo-msgs} : get all the cargoMsgs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cargoMsgs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CargoMsgDTO>> getAllCargoMsgs(
        CargoMsgCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CargoMsgs by criteria: {}", criteria);

        Page<CargoMsgDTO> page = cargoMsgQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cargo-msgs/count} : count all the cargoMsgs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCargoMsgs(CargoMsgCriteria criteria) {
        LOG.debug("REST request to count CargoMsgs by criteria: {}", criteria);
        return ResponseEntity.ok().body(cargoMsgQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cargo-msgs/:id} : get the "id" cargoMsg.
     *
     * @param id the id of the cargoMsgDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoMsgDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CargoMsgDTO> getCargoMsg(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CargoMsg : {}", id);
        Optional<CargoMsgDTO> cargoMsgDTO = cargoMsgService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoMsgDTO);
    }

    /**
     * {@code DELETE  /cargo-msgs/:id} : delete the "id" cargoMsg.
     *
     * @param id the id of the cargoMsgDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargoMsg(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CargoMsg : {}", id);
        cargoMsgService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /cargo-msgs/_search?query=:query} : search for the cargoMsg corresponding
     * to the query.
     *
     * @param query the query of the cargoMsg search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<CargoMsgDTO>> searchCargoMsgs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of CargoMsgs for query {}", query);
        try {
            Page<CargoMsgDTO> page = cargoMsgService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

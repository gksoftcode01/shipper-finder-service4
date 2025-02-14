package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.ItemTypeRepository;
import ai.yarmook.shipperfinder.service.ItemTypeQueryService;
import ai.yarmook.shipperfinder.service.ItemTypeService;
import ai.yarmook.shipperfinder.service.criteria.ItemTypeCriteria;
import ai.yarmook.shipperfinder.service.dto.ItemTypeDTO;
import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import ai.yarmook.shipperfinder.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.ItemType}.
 */
@RestController
@RequestMapping("/api/item-types")
public class ItemTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ItemTypeResource.class);

    private static final String ENTITY_NAME = "itemType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemTypeService itemTypeService;

    private final ItemTypeRepository itemTypeRepository;

    private final ItemTypeQueryService itemTypeQueryService;

    public ItemTypeResource(
        ItemTypeService itemTypeService,
        ItemTypeRepository itemTypeRepository,
        ItemTypeQueryService itemTypeQueryService
    ) {
        this.itemTypeService = itemTypeService;
        this.itemTypeRepository = itemTypeRepository;
        this.itemTypeQueryService = itemTypeQueryService;
    }

    /**
     * {@code POST  /item-types} : Create a new itemType.
     *
     * @param itemTypeDTO the itemTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemTypeDTO, or with status {@code 400 (Bad Request)} if the itemType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ItemTypeDTO> createItemType(@Valid @RequestBody ItemTypeDTO itemTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save ItemType : {}", itemTypeDTO);
        if (itemTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        itemTypeDTO = itemTypeService.save(itemTypeDTO);
        return ResponseEntity.created(new URI("/api/item-types/" + itemTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, itemTypeDTO.getId().toString()))
            .body(itemTypeDTO);
    }

    /**
     * {@code PUT  /item-types/:id} : Updates an existing itemType.
     *
     * @param id the id of the itemTypeDTO to save.
     * @param itemTypeDTO the itemTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTypeDTO,
     * or with status {@code 400 (Bad Request)} if the itemTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemTypeDTO> updateItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemTypeDTO itemTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ItemType : {}, {}", id, itemTypeDTO);
        if (itemTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        itemTypeDTO = itemTypeService.update(itemTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemTypeDTO.getId().toString()))
            .body(itemTypeDTO);
    }

    /**
     * {@code PATCH  /item-types/:id} : Partial updates given fields of an existing itemType, field will ignore if it is null
     *
     * @param id the id of the itemTypeDTO to save.
     * @param itemTypeDTO the itemTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTypeDTO,
     * or with status {@code 400 (Bad Request)} if the itemTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemTypeDTO> partialUpdateItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemTypeDTO itemTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ItemType partially : {}, {}", id, itemTypeDTO);
        if (itemTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemTypeDTO> result = itemTypeService.partialUpdate(itemTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-types} : get all the itemTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ItemTypeDTO>> getAllItemTypes(
        ItemTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ItemTypes by criteria: {}", criteria);

        Page<ItemTypeDTO> page = itemTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-types/count} : count all the itemTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countItemTypes(ItemTypeCriteria criteria) {
        LOG.debug("REST request to count ItemTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(itemTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /item-types/:id} : get the "id" itemType.
     *
     * @param id the id of the itemTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemTypeDTO> getItemType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ItemType : {}", id);
        Optional<ItemTypeDTO> itemTypeDTO = itemTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemTypeDTO);
    }

    /**
     * {@code DELETE  /item-types/:id} : delete the "id" itemType.
     *
     * @param id the id of the itemTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ItemType : {}", id);
        itemTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /item-types/_search?query=:query} : search for the itemType corresponding
     * to the query.
     *
     * @param query the query of the itemType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ItemTypeDTO>> searchItemTypes(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ItemTypes for query {}", query);
        try {
            Page<ItemTypeDTO> page = itemTypeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}

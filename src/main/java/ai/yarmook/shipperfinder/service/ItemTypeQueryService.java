package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.ItemType;
import ai.yarmook.shipperfinder.repository.ItemTypeRepository;
import ai.yarmook.shipperfinder.repository.search.ItemTypeSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.ItemTypeCriteria;
import ai.yarmook.shipperfinder.service.dto.ItemTypeDTO;
import ai.yarmook.shipperfinder.service.mapper.ItemTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ItemType} entities in the database.
 * The main input is a {@link ItemTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ItemTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemTypeQueryService extends QueryService<ItemType> {

    private static final Logger LOG = LoggerFactory.getLogger(ItemTypeQueryService.class);

    private final ItemTypeRepository itemTypeRepository;

    private final ItemTypeMapper itemTypeMapper;

    private final ItemTypeSearchRepository itemTypeSearchRepository;

    public ItemTypeQueryService(
        ItemTypeRepository itemTypeRepository,
        ItemTypeMapper itemTypeMapper,
        ItemTypeSearchRepository itemTypeSearchRepository
    ) {
        this.itemTypeRepository = itemTypeRepository;
        this.itemTypeMapper = itemTypeMapper;
        this.itemTypeSearchRepository = itemTypeSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ItemTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemTypeDTO> findByCriteria(ItemTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemType> specification = createSpecification(criteria);
        return itemTypeRepository.findAll(specification, page).map(itemTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ItemType> specification = createSpecification(criteria);
        return itemTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ItemType> createSpecification(ItemTypeCriteria criteria) {
        Specification<ItemType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ItemType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ItemType_.name));
            }
            if (criteria.getNameEn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameEn(), ItemType_.nameEn));
            }
            if (criteria.getNameAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameAr(), ItemType_.nameAr));
            }
            if (criteria.getNameFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameFr(), ItemType_.nameFr));
            }
            if (criteria.getNameDe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameDe(), ItemType_.nameDe));
            }
            if (criteria.getNameUrdu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameUrdu(), ItemType_.nameUrdu));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), ItemType_.isActive));
            }
        }
        return specification;
    }
}

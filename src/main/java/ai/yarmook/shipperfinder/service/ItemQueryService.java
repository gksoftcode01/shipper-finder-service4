package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.Item;
import ai.yarmook.shipperfinder.repository.ItemRepository;
import ai.yarmook.shipperfinder.repository.search.ItemSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.ItemCriteria;
import ai.yarmook.shipperfinder.service.dto.ItemDTO;
import ai.yarmook.shipperfinder.service.mapper.ItemMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Item} entities in the database.
 * The main input is a {@link ItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemQueryService extends QueryService<Item> {

    private static final Logger LOG = LoggerFactory.getLogger(ItemQueryService.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final ItemSearchRepository itemSearchRepository;

    public ItemQueryService(ItemRepository itemRepository, ItemMapper itemMapper, ItemSearchRepository itemSearchRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemSearchRepository = itemSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemDTO> findByCriteria(ItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.findAll(specification, page).map(itemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Item> createSpecification(ItemCriteria criteria) {
        Specification<Item> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Item_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Item_.name));
            }
            if (criteria.getNameEn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameEn(), Item_.nameEn));
            }
            if (criteria.getNameAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameAr(), Item_.nameAr));
            }
            if (criteria.getNameFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameFr(), Item_.nameFr));
            }
            if (criteria.getNameDe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameDe(), Item_.nameDe));
            }
            if (criteria.getNameUrdu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameUrdu(), Item_.nameUrdu));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Item_.isActive));
            }
            if (criteria.getDefaultUOM() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDefaultUOM(), Item_.defaultUOM));
            }
            if (criteria.getItemTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getItemTypeId(), root -> root.join(Item_.itemType, JoinType.LEFT).get(ItemType_.id))
                );
            }
        }
        return specification;
    }
}

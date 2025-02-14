package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.TripItem;
import ai.yarmook.shipperfinder.repository.TripItemRepository;
import ai.yarmook.shipperfinder.repository.search.TripItemSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.TripItemCriteria;
import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
import ai.yarmook.shipperfinder.service.mapper.TripItemMapper;
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
 * Service for executing complex queries for {@link TripItem} entities in the database.
 * The main input is a {@link TripItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TripItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TripItemQueryService extends QueryService<TripItem> {

    private static final Logger LOG = LoggerFactory.getLogger(TripItemQueryService.class);

    private final TripItemRepository tripItemRepository;

    private final TripItemMapper tripItemMapper;

    private final TripItemSearchRepository tripItemSearchRepository;

    public TripItemQueryService(
        TripItemRepository tripItemRepository,
        TripItemMapper tripItemMapper,
        TripItemSearchRepository tripItemSearchRepository
    ) {
        this.tripItemRepository = tripItemRepository;
        this.tripItemMapper = tripItemMapper;
        this.tripItemSearchRepository = tripItemSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link TripItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TripItemDTO> findByCriteria(TripItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TripItem> specification = createSpecification(criteria);
        return tripItemRepository.fetchBagRelationships(tripItemRepository.findAll(specification, page)).map(tripItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TripItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TripItem> specification = createSpecification(criteria);
        return tripItemRepository.count(specification);
    }

    /**
     * Function to convert {@link TripItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TripItem> createSpecification(TripItemCriteria criteria) {
        Specification<TripItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TripItem_.id));
            }
            if (criteria.getItemPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemPrice(), TripItem_.itemPrice));
            }
            if (criteria.getMaxQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxQty(), TripItem_.maxQty));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getItemId(), root -> root.join(TripItem_.item, JoinType.LEFT).get(Item_.id))
                );
            }
            if (criteria.getUnitId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUnitId(), root -> root.join(TripItem_.unit, JoinType.LEFT).get(Unit_.id))
                );
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTagId(), root -> root.join(TripItem_.tags, JoinType.LEFT).get(Tag_.id))
                );
            }
            if (criteria.getTripId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTripId(), root -> root.join(TripItem_.trip, JoinType.LEFT).get(Trip_.id))
                );
            }
        }
        return specification;
    }
}

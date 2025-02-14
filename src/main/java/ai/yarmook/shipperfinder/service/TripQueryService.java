package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.repository.TripRepository;
import ai.yarmook.shipperfinder.repository.search.TripSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.TripCriteria;
import ai.yarmook.shipperfinder.service.dto.TripDTO;
import ai.yarmook.shipperfinder.service.mapper.TripMapper;
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
 * Service for executing complex queries for {@link Trip} entities in the database.
 * The main input is a {@link TripCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TripDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TripQueryService extends QueryService<Trip> {

    private static final Logger LOG = LoggerFactory.getLogger(TripQueryService.class);

    private final TripRepository tripRepository;

    private final TripMapper tripMapper;

    private final TripSearchRepository tripSearchRepository;

    public TripQueryService(TripRepository tripRepository, TripMapper tripMapper, TripSearchRepository tripSearchRepository) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
        this.tripSearchRepository = tripSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link TripDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TripDTO> findByCriteria(TripCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trip> specification = createSpecification(criteria);
        return tripRepository.findAll(specification, page).map(tripMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TripCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Trip> specification = createSpecification(criteria);
        return tripRepository.count(specification);
    }

    /**
     * Function to convert {@link TripCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Trip> createSpecification(TripCriteria criteria) {
        Specification<Trip> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Trip_.id));
            }
            if (criteria.getTripDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTripDate(), Trip_.tripDate));
            }
            if (criteria.getArriveDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getArriveDate(), Trip_.arriveDate));
            }
            if (criteria.getMaxWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxWeight(), Trip_.maxWeight));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Trip_.notes));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Trip_.createDate));
            }
            if (criteria.getIsNegotiate() != null) {
                specification = specification.and(buildSpecification(criteria.getIsNegotiate(), Trip_.isNegotiate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Trip_.status));
            }
            if (criteria.getCreatedByEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedByEncId(), Trip_.createdByEncId));
            }
            if (criteria.getEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getEncId(), Trip_.encId));
            }
            if (criteria.getItemsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getItemsId(), root -> root.join(Trip_.items, JoinType.LEFT).get(TripItem_.id))
                );
            }
            if (criteria.getFromCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getFromCountryId(), root -> root.join(Trip_.fromCountry, JoinType.LEFT).get(Country_.id))
                );
            }
            if (criteria.getToCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getToCountryId(), root -> root.join(Trip_.toCountry, JoinType.LEFT).get(Country_.id))
                );
            }
            if (criteria.getFromStateId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getFromStateId(), root -> root.join(Trip_.fromState, JoinType.LEFT).get(StateProvince_.id))
                );
            }
            if (criteria.getToStateId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getToStateId(), root -> root.join(Trip_.toState, JoinType.LEFT).get(StateProvince_.id))
                );
            }
        }
        return specification;
    }
}

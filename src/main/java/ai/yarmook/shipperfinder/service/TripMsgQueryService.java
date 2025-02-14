package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.TripMsg;
import ai.yarmook.shipperfinder.repository.TripMsgRepository;
import ai.yarmook.shipperfinder.repository.search.TripMsgSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.TripMsgCriteria;
import ai.yarmook.shipperfinder.service.dto.TripMsgDTO;
import ai.yarmook.shipperfinder.service.mapper.TripMsgMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TripMsg} entities in the database.
 * The main input is a {@link TripMsgCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TripMsgDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TripMsgQueryService extends QueryService<TripMsg> {

    private static final Logger LOG = LoggerFactory.getLogger(TripMsgQueryService.class);

    private final TripMsgRepository tripMsgRepository;

    private final TripMsgMapper tripMsgMapper;

    private final TripMsgSearchRepository tripMsgSearchRepository;

    public TripMsgQueryService(
        TripMsgRepository tripMsgRepository,
        TripMsgMapper tripMsgMapper,
        TripMsgSearchRepository tripMsgSearchRepository
    ) {
        this.tripMsgRepository = tripMsgRepository;
        this.tripMsgMapper = tripMsgMapper;
        this.tripMsgSearchRepository = tripMsgSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link TripMsgDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TripMsgDTO> findByCriteria(TripMsgCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TripMsg> specification = createSpecification(criteria);
        return tripMsgRepository.findAll(specification, page).map(tripMsgMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TripMsgCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TripMsg> specification = createSpecification(criteria);
        return tripMsgRepository.count(specification);
    }

    /**
     * Function to convert {@link TripMsgCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TripMsg> createSpecification(TripMsgCriteria criteria) {
        Specification<TripMsg> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TripMsg_.id));
            }
            if (criteria.getMsg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMsg(), TripMsg_.msg));
            }
            if (criteria.getFromUserEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getFromUserEncId(), TripMsg_.fromUserEncId));
            }
            if (criteria.getToUserEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getToUserEncId(), TripMsg_.toUserEncId));
            }
            if (criteria.getTripId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTripId(), TripMsg_.tripId));
            }
        }
        return specification;
    }
}

package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.UserSubscribe;
import ai.yarmook.shipperfinder.repository.UserSubscribeRepository;
import ai.yarmook.shipperfinder.repository.search.UserSubscribeSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.UserSubscribeCriteria;
import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import ai.yarmook.shipperfinder.service.mapper.UserSubscribeMapper;
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
 * Service for executing complex queries for {@link UserSubscribe} entities in the database.
 * The main input is a {@link UserSubscribeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserSubscribeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserSubscribeQueryService extends QueryService<UserSubscribe> {

    private static final Logger LOG = LoggerFactory.getLogger(UserSubscribeQueryService.class);

    private final UserSubscribeRepository userSubscribeRepository;

    private final UserSubscribeMapper userSubscribeMapper;

    private final UserSubscribeSearchRepository userSubscribeSearchRepository;

    public UserSubscribeQueryService(
        UserSubscribeRepository userSubscribeRepository,
        UserSubscribeMapper userSubscribeMapper,
        UserSubscribeSearchRepository userSubscribeSearchRepository
    ) {
        this.userSubscribeRepository = userSubscribeRepository;
        this.userSubscribeMapper = userSubscribeMapper;
        this.userSubscribeSearchRepository = userSubscribeSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link UserSubscribeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserSubscribeDTO> findByCriteria(UserSubscribeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserSubscribe> specification = createSpecification(criteria);
        return userSubscribeRepository.findAll(specification, page).map(userSubscribeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserSubscribeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserSubscribe> specification = createSpecification(criteria);
        return userSubscribeRepository.count(specification);
    }

    /**
     * Function to convert {@link UserSubscribeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserSubscribe> createSpecification(UserSubscribeCriteria criteria) {
        Specification<UserSubscribe> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserSubscribe_.id));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), UserSubscribe_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), UserSubscribe_.toDate));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), UserSubscribe_.isActive));
            }
            if (criteria.getSubscribedUserEncId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSubscribedUserEncId(), UserSubscribe_.subscribedUserEncId)
                );
            }
            if (criteria.getSubscribeTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSubscribeTypeId(), root ->
                        root.join(UserSubscribe_.subscribeType, JoinType.LEFT).get(SubscribeType_.id)
                    )
                );
            }
        }
        return specification;
    }
}

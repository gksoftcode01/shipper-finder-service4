package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.repository.SubscribeTypeRepository;
import ai.yarmook.shipperfinder.repository.search.SubscribeTypeSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.SubscribeTypeCriteria;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
import ai.yarmook.shipperfinder.service.mapper.SubscribeTypeMapper;
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
 * Service for executing complex queries for {@link SubscribeType} entities in the database.
 * The main input is a {@link SubscribeTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SubscribeTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubscribeTypeQueryService extends QueryService<SubscribeType> {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeTypeQueryService.class);

    private final SubscribeTypeRepository subscribeTypeRepository;

    private final SubscribeTypeMapper subscribeTypeMapper;

    private final SubscribeTypeSearchRepository subscribeTypeSearchRepository;

    public SubscribeTypeQueryService(
        SubscribeTypeRepository subscribeTypeRepository,
        SubscribeTypeMapper subscribeTypeMapper,
        SubscribeTypeSearchRepository subscribeTypeSearchRepository
    ) {
        this.subscribeTypeRepository = subscribeTypeRepository;
        this.subscribeTypeMapper = subscribeTypeMapper;
        this.subscribeTypeSearchRepository = subscribeTypeSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SubscribeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscribeTypeDTO> findByCriteria(SubscribeTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubscribeType> specification = createSpecification(criteria);
        return subscribeTypeRepository.findAll(specification, page).map(subscribeTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubscribeTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SubscribeType> specification = createSpecification(criteria);
        return subscribeTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link SubscribeTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubscribeType> createSpecification(SubscribeTypeCriteria criteria) {
        Specification<SubscribeType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubscribeType_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), SubscribeType_.type));
            }
            if (criteria.getNameEn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameEn(), SubscribeType_.nameEn));
            }
            if (criteria.getNameAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameAr(), SubscribeType_.nameAr));
            }
            if (criteria.getNameFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameFr(), SubscribeType_.nameFr));
            }
            if (criteria.getNameDe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameDe(), SubscribeType_.nameDe));
            }
            if (criteria.getNameUrdu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameUrdu(), SubscribeType_.nameUrdu));
            }
            if (criteria.getDetailsEn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetailsEn(), SubscribeType_.detailsEn));
            }
            if (criteria.getDetailsAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetailsAr(), SubscribeType_.detailsAr));
            }
            if (criteria.getDetailsFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetailsFr(), SubscribeType_.detailsFr));
            }
            if (criteria.getDetailsDe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetailsDe(), SubscribeType_.detailsDe));
            }
            if (criteria.getDetailsUrdu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetailsUrdu(), SubscribeType_.detailsUrdu));
            }
            if (criteria.getSubscribeTypeDetailId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSubscribeTypeDetailId(), root ->
                        root.join(SubscribeType_.subscribeTypeDetail, JoinType.LEFT).get(SubscribeTypeDetail_.id)
                    )
                );
            }
        }
        return specification;
    }
}

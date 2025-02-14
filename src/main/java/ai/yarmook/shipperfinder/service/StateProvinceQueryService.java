package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.StateProvince;
import ai.yarmook.shipperfinder.repository.StateProvinceRepository;
import ai.yarmook.shipperfinder.repository.search.StateProvinceSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.StateProvinceCriteria;
import ai.yarmook.shipperfinder.service.dto.StateProvinceDTO;
import ai.yarmook.shipperfinder.service.mapper.StateProvinceMapper;
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
 * Service for executing complex queries for {@link StateProvince} entities in the database.
 * The main input is a {@link StateProvinceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link StateProvinceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StateProvinceQueryService extends QueryService<StateProvince> {

    private static final Logger LOG = LoggerFactory.getLogger(StateProvinceQueryService.class);

    private final StateProvinceRepository stateProvinceRepository;

    private final StateProvinceMapper stateProvinceMapper;

    private final StateProvinceSearchRepository stateProvinceSearchRepository;

    public StateProvinceQueryService(
        StateProvinceRepository stateProvinceRepository,
        StateProvinceMapper stateProvinceMapper,
        StateProvinceSearchRepository stateProvinceSearchRepository
    ) {
        this.stateProvinceRepository = stateProvinceRepository;
        this.stateProvinceMapper = stateProvinceMapper;
        this.stateProvinceSearchRepository = stateProvinceSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link StateProvinceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StateProvinceDTO> findByCriteria(StateProvinceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StateProvince> specification = createSpecification(criteria);
        return stateProvinceRepository.findAll(specification, page).map(stateProvinceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StateProvinceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<StateProvince> specification = createSpecification(criteria);
        return stateProvinceRepository.count(specification);
    }

    /**
     * Function to convert {@link StateProvinceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StateProvince> createSpecification(StateProvinceCriteria criteria) {
        Specification<StateProvince> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StateProvince_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), StateProvince_.name));
            }
            if (criteria.getLocalName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocalName(), StateProvince_.localName));
            }
            if (criteria.getIsoCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIsoCode(), StateProvince_.isoCode));
            }
            if (criteria.getCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCountryId(), root -> root.join(StateProvince_.country, JoinType.LEFT).get(Country_.id))
                );
            }
        }
        return specification;
    }
}

package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.repository.CargoRequestRepository;
import ai.yarmook.shipperfinder.repository.search.CargoRequestSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.CargoRequestCriteria;
import ai.yarmook.shipperfinder.service.dto.CargoRequestDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoRequestMapper;
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
 * Service for executing complex queries for {@link CargoRequest} entities in the database.
 * The main input is a {@link CargoRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CargoRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CargoRequestQueryService extends QueryService<CargoRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(CargoRequestQueryService.class);

    private final CargoRequestRepository cargoRequestRepository;

    private final CargoRequestMapper cargoRequestMapper;

    private final CargoRequestSearchRepository cargoRequestSearchRepository;

    public CargoRequestQueryService(
        CargoRequestRepository cargoRequestRepository,
        CargoRequestMapper cargoRequestMapper,
        CargoRequestSearchRepository cargoRequestSearchRepository
    ) {
        this.cargoRequestRepository = cargoRequestRepository;
        this.cargoRequestMapper = cargoRequestMapper;
        this.cargoRequestSearchRepository = cargoRequestSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link CargoRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CargoRequestDTO> findByCriteria(CargoRequestCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CargoRequest> specification = createSpecification(criteria);
        return cargoRequestRepository.findAll(specification, page).map(cargoRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CargoRequestCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CargoRequest> specification = createSpecification(criteria);
        return cargoRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link CargoRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CargoRequest> createSpecification(CargoRequestCriteria criteria) {
        Specification<CargoRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CargoRequest_.id));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), CargoRequest_.createDate));
            }
            if (criteria.getValidUntil() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidUntil(), CargoRequest_.validUntil));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), CargoRequest_.status));
            }
            if (criteria.getIsNegotiable() != null) {
                specification = specification.and(buildSpecification(criteria.getIsNegotiable(), CargoRequest_.isNegotiable));
            }
            if (criteria.getBudget() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBudget(), CargoRequest_.budget));
            }
            if (criteria.getCreatedByEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedByEncId(), CargoRequest_.createdByEncId));
            }
            if (criteria.getTakenByEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getTakenByEncId(), CargoRequest_.takenByEncId));
            }
            if (criteria.getEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getEncId(), CargoRequest_.encId));
            }
            if (criteria.getItemsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getItemsId(), root ->
                        root.join(CargoRequest_.items, JoinType.LEFT).get(CargoRequestItem_.id)
                    )
                );
            }
            if (criteria.getFromCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getFromCountryId(), root ->
                        root.join(CargoRequest_.fromCountry, JoinType.LEFT).get(Country_.id)
                    )
                );
            }
            if (criteria.getToCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getToCountryId(), root -> root.join(CargoRequest_.toCountry, JoinType.LEFT).get(Country_.id)
                    )
                );
            }
            if (criteria.getFromStateId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getFromStateId(), root ->
                        root.join(CargoRequest_.fromState, JoinType.LEFT).get(StateProvince_.id)
                    )
                );
            }
            if (criteria.getToStateId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getToStateId(), root ->
                        root.join(CargoRequest_.toState, JoinType.LEFT).get(StateProvince_.id)
                    )
                );
            }
        }
        return specification;
    }
}

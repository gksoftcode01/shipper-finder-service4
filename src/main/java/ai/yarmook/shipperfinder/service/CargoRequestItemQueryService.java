package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.repository.CargoRequestItemRepository;
import ai.yarmook.shipperfinder.repository.search.CargoRequestItemSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.CargoRequestItemCriteria;
import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoRequestItemMapper;
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
 * Service for executing complex queries for {@link CargoRequestItem} entities in the database.
 * The main input is a {@link CargoRequestItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CargoRequestItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CargoRequestItemQueryService extends QueryService<CargoRequestItem> {

    private static final Logger LOG = LoggerFactory.getLogger(CargoRequestItemQueryService.class);

    private final CargoRequestItemRepository cargoRequestItemRepository;

    private final CargoRequestItemMapper cargoRequestItemMapper;

    private final CargoRequestItemSearchRepository cargoRequestItemSearchRepository;

    public CargoRequestItemQueryService(
        CargoRequestItemRepository cargoRequestItemRepository,
        CargoRequestItemMapper cargoRequestItemMapper,
        CargoRequestItemSearchRepository cargoRequestItemSearchRepository
    ) {
        this.cargoRequestItemRepository = cargoRequestItemRepository;
        this.cargoRequestItemMapper = cargoRequestItemMapper;
        this.cargoRequestItemSearchRepository = cargoRequestItemSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link CargoRequestItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CargoRequestItemDTO> findByCriteria(CargoRequestItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CargoRequestItem> specification = createSpecification(criteria);
        return cargoRequestItemRepository
            .fetchBagRelationships(cargoRequestItemRepository.findAll(specification, page))
            .map(cargoRequestItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CargoRequestItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CargoRequestItem> specification = createSpecification(criteria);
        return cargoRequestItemRepository.count(specification);
    }

    /**
     * Function to convert {@link CargoRequestItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CargoRequestItem> createSpecification(CargoRequestItemCriteria criteria) {
        Specification<CargoRequestItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CargoRequestItem_.id));
            }
            if (criteria.getMaxQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxQty(), CargoRequestItem_.maxQty));
            }
            if (criteria.getPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhotoUrl(), CargoRequestItem_.photoUrl));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getItemId(), root -> root.join(CargoRequestItem_.item, JoinType.LEFT).get(Item_.id))
                );
            }
            if (criteria.getUnitId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUnitId(), root -> root.join(CargoRequestItem_.unit, JoinType.LEFT).get(Unit_.id))
                );
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTagId(), root -> root.join(CargoRequestItem_.tags, JoinType.LEFT).get(Tag_.id))
                );
            }
            if (criteria.getCargoRequestId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCargoRequestId(), root ->
                        root.join(CargoRequestItem_.cargoRequest, JoinType.LEFT).get(CargoRequest_.id)
                    )
                );
            }
        }
        return specification;
    }
}

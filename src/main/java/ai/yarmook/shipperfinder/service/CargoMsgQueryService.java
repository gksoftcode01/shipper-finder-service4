package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.CargoMsg;
import ai.yarmook.shipperfinder.repository.CargoMsgRepository;
import ai.yarmook.shipperfinder.repository.search.CargoMsgSearchRepository;
import ai.yarmook.shipperfinder.service.criteria.CargoMsgCriteria;
import ai.yarmook.shipperfinder.service.dto.CargoMsgDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoMsgMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CargoMsg} entities in the database.
 * The main input is a {@link CargoMsgCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CargoMsgDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CargoMsgQueryService extends QueryService<CargoMsg> {

    private static final Logger LOG = LoggerFactory.getLogger(CargoMsgQueryService.class);

    private final CargoMsgRepository cargoMsgRepository;

    private final CargoMsgMapper cargoMsgMapper;

    private final CargoMsgSearchRepository cargoMsgSearchRepository;

    public CargoMsgQueryService(
        CargoMsgRepository cargoMsgRepository,
        CargoMsgMapper cargoMsgMapper,
        CargoMsgSearchRepository cargoMsgSearchRepository
    ) {
        this.cargoMsgRepository = cargoMsgRepository;
        this.cargoMsgMapper = cargoMsgMapper;
        this.cargoMsgSearchRepository = cargoMsgSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link CargoMsgDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CargoMsgDTO> findByCriteria(CargoMsgCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CargoMsg> specification = createSpecification(criteria);
        return cargoMsgRepository.findAll(specification, page).map(cargoMsgMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CargoMsgCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CargoMsg> specification = createSpecification(criteria);
        return cargoMsgRepository.count(specification);
    }

    /**
     * Function to convert {@link CargoMsgCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CargoMsg> createSpecification(CargoMsgCriteria criteria) {
        Specification<CargoMsg> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CargoMsg_.id));
            }
            if (criteria.getMsg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMsg(), CargoMsg_.msg));
            }
            if (criteria.getFromUserEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getFromUserEncId(), CargoMsg_.fromUserEncId));
            }
            if (criteria.getToUserEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getToUserEncId(), CargoMsg_.toUserEncId));
            }
            if (criteria.getCargoRequestId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCargoRequestId(), CargoMsg_.cargoRequestId));
            }
        }
        return specification;
    }
}

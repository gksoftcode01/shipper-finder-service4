package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.ShowNumberHistory;
import ai.yarmook.shipperfinder.repository.ShowNumberHistoryRepository;
import ai.yarmook.shipperfinder.repository.search.ShowNumberHistorySearchRepository;
import ai.yarmook.shipperfinder.service.criteria.ShowNumberHistoryCriteria;
import ai.yarmook.shipperfinder.service.dto.ShowNumberHistoryDTO;
import ai.yarmook.shipperfinder.service.mapper.ShowNumberHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ShowNumberHistory} entities in the database.
 * The main input is a {@link ShowNumberHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShowNumberHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShowNumberHistoryQueryService extends QueryService<ShowNumberHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(ShowNumberHistoryQueryService.class);

    private final ShowNumberHistoryRepository showNumberHistoryRepository;

    private final ShowNumberHistoryMapper showNumberHistoryMapper;

    private final ShowNumberHistorySearchRepository showNumberHistorySearchRepository;

    public ShowNumberHistoryQueryService(
        ShowNumberHistoryRepository showNumberHistoryRepository,
        ShowNumberHistoryMapper showNumberHistoryMapper,
        ShowNumberHistorySearchRepository showNumberHistorySearchRepository
    ) {
        this.showNumberHistoryRepository = showNumberHistoryRepository;
        this.showNumberHistoryMapper = showNumberHistoryMapper;
        this.showNumberHistorySearchRepository = showNumberHistorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ShowNumberHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShowNumberHistoryDTO> findByCriteria(ShowNumberHistoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShowNumberHistory> specification = createSpecification(criteria);
        return showNumberHistoryRepository.findAll(specification, page).map(showNumberHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShowNumberHistoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShowNumberHistory> specification = createSpecification(criteria);
        return showNumberHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ShowNumberHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShowNumberHistory> createSpecification(ShowNumberHistoryCriteria criteria) {
        Specification<ShowNumberHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShowNumberHistory_.id));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ShowNumberHistory_.createdDate));
            }
            if (criteria.getActionByEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getActionByEncId(), ShowNumberHistory_.actionByEncId));
            }
            if (criteria.getEntityType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntityType(), ShowNumberHistory_.entityType));
            }
            if (criteria.getEntityEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityEncId(), ShowNumberHistory_.entityEncId));
            }
        }
        return specification;
    }
}

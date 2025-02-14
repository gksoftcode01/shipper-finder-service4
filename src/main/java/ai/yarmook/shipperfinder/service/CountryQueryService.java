package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.repository.CountryRepository;
import ai.yarmook.shipperfinder.repository.search.CountrySearchRepository;
import ai.yarmook.shipperfinder.service.criteria.CountryCriteria;
import ai.yarmook.shipperfinder.service.dto.CountryDTO;
import ai.yarmook.shipperfinder.service.mapper.CountryMapper;
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
 * Service for executing complex queries for {@link Country} entities in the database.
 * The main input is a {@link CountryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CountryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountryQueryService extends QueryService<Country> {

    private static final Logger LOG = LoggerFactory.getLogger(CountryQueryService.class);

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    private final CountrySearchRepository countrySearchRepository;

    public CountryQueryService(
        CountryRepository countryRepository,
        CountryMapper countryMapper,
        CountrySearchRepository countrySearchRepository
    ) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.countrySearchRepository = countrySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link CountryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountryDTO> findByCriteria(CountryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Country> specification = createSpecification(criteria);
        return countryRepository.findAll(specification, page).map(countryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Country> specification = createSpecification(criteria);
        return countryRepository.count(specification);
    }

    /**
     * Function to convert {@link CountryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Country> createSpecification(CountryCriteria criteria) {
        Specification<Country> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Country_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Country_.name));
            }
            if (criteria.getLocalName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocalName(), Country_.localName));
            }
            if (criteria.getIso2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIso2(), Country_.iso2));
            }
            if (criteria.getIso3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIso3(), Country_.iso3));
            }
            if (criteria.getNumericCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumericCode(), Country_.numericCode));
            }
            if (criteria.getPhoneCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneCode(), Country_.phoneCode));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), Country_.currency));
            }
            if (criteria.getCurrencyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyName(), Country_.currencyName));
            }
            if (criteria.getCurrencySymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencySymbol(), Country_.currencySymbol));
            }
            if (criteria.getEmoji() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmoji(), Country_.emoji));
            }
            if (criteria.getEmojiU() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmojiU(), Country_.emojiU));
            }
            if (criteria.getStateProvinceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getStateProvinceId(), root ->
                        root.join(Country_.stateProvinces, JoinType.LEFT).get(StateProvince_.id)
                    )
                );
            }
        }
        return specification;
    }
}

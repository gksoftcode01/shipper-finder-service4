package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.UserRate;
import ai.yarmook.shipperfinder.repository.UserRateRepository;
import ai.yarmook.shipperfinder.repository.search.UserRateSearchRepository;
import ai.yarmook.shipperfinder.service.UserRateService;
import ai.yarmook.shipperfinder.service.dto.UserRateDTO;
import ai.yarmook.shipperfinder.service.mapper.UserRateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.UserRate}.
 */
@Service
@Transactional
public class UserRateServiceImpl implements UserRateService {

    private static final Logger LOG = LoggerFactory.getLogger(UserRateServiceImpl.class);

    private final UserRateRepository userRateRepository;

    private final UserRateMapper userRateMapper;

    private final UserRateSearchRepository userRateSearchRepository;

    public UserRateServiceImpl(
        UserRateRepository userRateRepository,
        UserRateMapper userRateMapper,
        UserRateSearchRepository userRateSearchRepository
    ) {
        this.userRateRepository = userRateRepository;
        this.userRateMapper = userRateMapper;
        this.userRateSearchRepository = userRateSearchRepository;
    }

    @Override
    public UserRateDTO save(UserRateDTO userRateDTO) {
        LOG.debug("Request to save UserRate : {}", userRateDTO);
        UserRate userRate = userRateMapper.toEntity(userRateDTO);
        userRate = userRateRepository.save(userRate);
        userRateSearchRepository.index(userRate);
        return userRateMapper.toDto(userRate);
    }

    @Override
    public UserRateDTO update(UserRateDTO userRateDTO) {
        LOG.debug("Request to update UserRate : {}", userRateDTO);
        UserRate userRate = userRateMapper.toEntity(userRateDTO);
        userRate = userRateRepository.save(userRate);
        userRateSearchRepository.index(userRate);
        return userRateMapper.toDto(userRate);
    }

    @Override
    public Optional<UserRateDTO> partialUpdate(UserRateDTO userRateDTO) {
        LOG.debug("Request to partially update UserRate : {}", userRateDTO);

        return userRateRepository
            .findById(userRateDTO.getId())
            .map(existingUserRate -> {
                userRateMapper.partialUpdate(existingUserRate, userRateDTO);

                return existingUserRate;
            })
            .map(userRateRepository::save)
            .map(savedUserRate -> {
                userRateSearchRepository.index(savedUserRate);
                return savedUserRate;
            })
            .map(userRateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserRateDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserRates");
        return userRateRepository.findAll(pageable).map(userRateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRateDTO> findOne(Long id) {
        LOG.debug("Request to get UserRate : {}", id);
        return userRateRepository.findById(id).map(userRateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserRate : {}", id);
        userRateRepository.deleteById(id);
        userRateSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserRateDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of UserRates for query {}", query);
        return userRateSearchRepository.search(query, pageable).map(userRateMapper::toDto);
    }
}

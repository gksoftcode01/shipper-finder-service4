package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.UserSubscribe;
import ai.yarmook.shipperfinder.repository.UserSubscribeRepository;
import ai.yarmook.shipperfinder.repository.search.UserSubscribeSearchRepository;
import ai.yarmook.shipperfinder.service.UserSubscribeService;
import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import ai.yarmook.shipperfinder.service.mapper.UserSubscribeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.UserSubscribe}.
 */
@Service
@Transactional
public class UserSubscribeServiceImpl implements UserSubscribeService {

    private static final Logger LOG = LoggerFactory.getLogger(UserSubscribeServiceImpl.class);

    private final UserSubscribeRepository userSubscribeRepository;

    private final UserSubscribeMapper userSubscribeMapper;

    private final UserSubscribeSearchRepository userSubscribeSearchRepository;

    public UserSubscribeServiceImpl(
        UserSubscribeRepository userSubscribeRepository,
        UserSubscribeMapper userSubscribeMapper,
        UserSubscribeSearchRepository userSubscribeSearchRepository
    ) {
        this.userSubscribeRepository = userSubscribeRepository;
        this.userSubscribeMapper = userSubscribeMapper;
        this.userSubscribeSearchRepository = userSubscribeSearchRepository;
    }

    @Override
    public UserSubscribeDTO save(UserSubscribeDTO userSubscribeDTO) {
        LOG.debug("Request to save UserSubscribe : {}", userSubscribeDTO);
        UserSubscribe userSubscribe = userSubscribeMapper.toEntity(userSubscribeDTO);
        userSubscribe = userSubscribeRepository.save(userSubscribe);
        userSubscribeSearchRepository.index(userSubscribe);
        return userSubscribeMapper.toDto(userSubscribe);
    }

    @Override
    public UserSubscribeDTO update(UserSubscribeDTO userSubscribeDTO) {
        LOG.debug("Request to update UserSubscribe : {}", userSubscribeDTO);
        UserSubscribe userSubscribe = userSubscribeMapper.toEntity(userSubscribeDTO);
        userSubscribe = userSubscribeRepository.save(userSubscribe);
        userSubscribeSearchRepository.index(userSubscribe);
        return userSubscribeMapper.toDto(userSubscribe);
    }

    @Override
    public Optional<UserSubscribeDTO> partialUpdate(UserSubscribeDTO userSubscribeDTO) {
        LOG.debug("Request to partially update UserSubscribe : {}", userSubscribeDTO);

        return userSubscribeRepository
            .findById(userSubscribeDTO.getId())
            .map(existingUserSubscribe -> {
                userSubscribeMapper.partialUpdate(existingUserSubscribe, userSubscribeDTO);

                return existingUserSubscribe;
            })
            .map(userSubscribeRepository::save)
            .map(savedUserSubscribe -> {
                userSubscribeSearchRepository.index(savedUserSubscribe);
                return savedUserSubscribe;
            })
            .map(userSubscribeMapper::toDto);
    }

    public Page<UserSubscribeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userSubscribeRepository.findAllWithEagerRelationships(pageable).map(userSubscribeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSubscribeDTO> findOne(Long id) {
        LOG.debug("Request to get UserSubscribe : {}", id);
        return userSubscribeRepository.findOneWithEagerRelationships(id).map(userSubscribeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserSubscribe : {}", id);
        userSubscribeRepository.deleteById(id);
        userSubscribeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSubscribeDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of UserSubscribes for query {}", query);
        return userSubscribeSearchRepository.search(query, pageable).map(userSubscribeMapper::toDto);
    }
}

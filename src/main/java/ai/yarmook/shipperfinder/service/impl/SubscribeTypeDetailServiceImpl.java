package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.SubscribeTypeDetail;
import ai.yarmook.shipperfinder.repository.SubscribeTypeDetailRepository;
import ai.yarmook.shipperfinder.repository.search.SubscribeTypeDetailSearchRepository;
import ai.yarmook.shipperfinder.service.SubscribeTypeDetailService;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDetailDTO;
import ai.yarmook.shipperfinder.service.mapper.SubscribeTypeDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.SubscribeTypeDetail}.
 */
@Service
@Transactional
public class SubscribeTypeDetailServiceImpl implements SubscribeTypeDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeTypeDetailServiceImpl.class);

    private final SubscribeTypeDetailRepository subscribeTypeDetailRepository;

    private final SubscribeTypeDetailMapper subscribeTypeDetailMapper;

    private final SubscribeTypeDetailSearchRepository subscribeTypeDetailSearchRepository;

    public SubscribeTypeDetailServiceImpl(
        SubscribeTypeDetailRepository subscribeTypeDetailRepository,
        SubscribeTypeDetailMapper subscribeTypeDetailMapper,
        SubscribeTypeDetailSearchRepository subscribeTypeDetailSearchRepository
    ) {
        this.subscribeTypeDetailRepository = subscribeTypeDetailRepository;
        this.subscribeTypeDetailMapper = subscribeTypeDetailMapper;
        this.subscribeTypeDetailSearchRepository = subscribeTypeDetailSearchRepository;
    }

    @Override
    public SubscribeTypeDetailDTO save(SubscribeTypeDetailDTO subscribeTypeDetailDTO) {
        LOG.debug("Request to save SubscribeTypeDetail : {}", subscribeTypeDetailDTO);
        SubscribeTypeDetail subscribeTypeDetail = subscribeTypeDetailMapper.toEntity(subscribeTypeDetailDTO);
        subscribeTypeDetail = subscribeTypeDetailRepository.save(subscribeTypeDetail);
        subscribeTypeDetailSearchRepository.index(subscribeTypeDetail);
        return subscribeTypeDetailMapper.toDto(subscribeTypeDetail);
    }

    @Override
    public SubscribeTypeDetailDTO update(SubscribeTypeDetailDTO subscribeTypeDetailDTO) {
        LOG.debug("Request to update SubscribeTypeDetail : {}", subscribeTypeDetailDTO);
        SubscribeTypeDetail subscribeTypeDetail = subscribeTypeDetailMapper.toEntity(subscribeTypeDetailDTO);
        subscribeTypeDetail = subscribeTypeDetailRepository.save(subscribeTypeDetail);
        subscribeTypeDetailSearchRepository.index(subscribeTypeDetail);
        return subscribeTypeDetailMapper.toDto(subscribeTypeDetail);
    }

    @Override
    public Optional<SubscribeTypeDetailDTO> partialUpdate(SubscribeTypeDetailDTO subscribeTypeDetailDTO) {
        LOG.debug("Request to partially update SubscribeTypeDetail : {}", subscribeTypeDetailDTO);

        return subscribeTypeDetailRepository
            .findById(subscribeTypeDetailDTO.getId())
            .map(existingSubscribeTypeDetail -> {
                subscribeTypeDetailMapper.partialUpdate(existingSubscribeTypeDetail, subscribeTypeDetailDTO);

                return existingSubscribeTypeDetail;
            })
            .map(subscribeTypeDetailRepository::save)
            .map(savedSubscribeTypeDetail -> {
                subscribeTypeDetailSearchRepository.index(savedSubscribeTypeDetail);
                return savedSubscribeTypeDetail;
            })
            .map(subscribeTypeDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscribeTypeDetailDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SubscribeTypeDetails");
        return subscribeTypeDetailRepository.findAll(pageable).map(subscribeTypeDetailMapper::toDto);
    }

    public Page<SubscribeTypeDetailDTO> findAllWithEagerRelationships(Pageable pageable) {
        return subscribeTypeDetailRepository.findAllWithEagerRelationships(pageable).map(subscribeTypeDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscribeTypeDetailDTO> findOne(Long id) {
        LOG.debug("Request to get SubscribeTypeDetail : {}", id);
        return subscribeTypeDetailRepository.findOneWithEagerRelationships(id).map(subscribeTypeDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SubscribeTypeDetail : {}", id);
        subscribeTypeDetailRepository.deleteById(id);
        subscribeTypeDetailSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscribeTypeDetailDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SubscribeTypeDetails for query {}", query);
        return subscribeTypeDetailSearchRepository.search(query, pageable).map(subscribeTypeDetailMapper::toDto);
    }
}

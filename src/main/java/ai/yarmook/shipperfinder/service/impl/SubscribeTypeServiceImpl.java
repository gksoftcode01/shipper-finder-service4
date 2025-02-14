package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.repository.SubscribeTypeRepository;
import ai.yarmook.shipperfinder.repository.search.SubscribeTypeSearchRepository;
import ai.yarmook.shipperfinder.service.SubscribeTypeService;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
import ai.yarmook.shipperfinder.service.mapper.SubscribeTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.SubscribeType}.
 */
@Service
@Transactional
public class SubscribeTypeServiceImpl implements SubscribeTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeTypeServiceImpl.class);

    private final SubscribeTypeRepository subscribeTypeRepository;

    private final SubscribeTypeMapper subscribeTypeMapper;

    private final SubscribeTypeSearchRepository subscribeTypeSearchRepository;

    public SubscribeTypeServiceImpl(
        SubscribeTypeRepository subscribeTypeRepository,
        SubscribeTypeMapper subscribeTypeMapper,
        SubscribeTypeSearchRepository subscribeTypeSearchRepository
    ) {
        this.subscribeTypeRepository = subscribeTypeRepository;
        this.subscribeTypeMapper = subscribeTypeMapper;
        this.subscribeTypeSearchRepository = subscribeTypeSearchRepository;
    }

    @Override
    public SubscribeTypeDTO save(SubscribeTypeDTO subscribeTypeDTO) {
        LOG.debug("Request to save SubscribeType : {}", subscribeTypeDTO);
        SubscribeType subscribeType = subscribeTypeMapper.toEntity(subscribeTypeDTO);
        subscribeType = subscribeTypeRepository.save(subscribeType);
        subscribeTypeSearchRepository.index(subscribeType);
        return subscribeTypeMapper.toDto(subscribeType);
    }

    @Override
    public SubscribeTypeDTO update(SubscribeTypeDTO subscribeTypeDTO) {
        LOG.debug("Request to update SubscribeType : {}", subscribeTypeDTO);
        SubscribeType subscribeType = subscribeTypeMapper.toEntity(subscribeTypeDTO);
        subscribeType = subscribeTypeRepository.save(subscribeType);
        subscribeTypeSearchRepository.index(subscribeType);
        return subscribeTypeMapper.toDto(subscribeType);
    }

    @Override
    public Optional<SubscribeTypeDTO> partialUpdate(SubscribeTypeDTO subscribeTypeDTO) {
        LOG.debug("Request to partially update SubscribeType : {}", subscribeTypeDTO);

        return subscribeTypeRepository
            .findById(subscribeTypeDTO.getId())
            .map(existingSubscribeType -> {
                subscribeTypeMapper.partialUpdate(existingSubscribeType, subscribeTypeDTO);

                return existingSubscribeType;
            })
            .map(subscribeTypeRepository::save)
            .map(savedSubscribeType -> {
                subscribeTypeSearchRepository.index(savedSubscribeType);
                return savedSubscribeType;
            })
            .map(subscribeTypeMapper::toDto);
    }

    /**
     *  Get all the subscribeTypes where SubscribeTypeDetail is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscribeTypeDTO> findAllWhereSubscribeTypeDetailIsNull() {
        LOG.debug("Request to get all subscribeTypes where SubscribeTypeDetail is null");
        return StreamSupport.stream(subscribeTypeRepository.findAll().spliterator(), false)
            .filter(subscribeType -> subscribeType.getSubscribeTypeDetail() == null)
            .map(subscribeTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscribeTypeDTO> findOne(Long id) {
        LOG.debug("Request to get SubscribeType : {}", id);
        return subscribeTypeRepository.findById(id).map(subscribeTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SubscribeType : {}", id);
        subscribeTypeRepository.deleteById(id);
        subscribeTypeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscribeTypeDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SubscribeTypes for query {}", query);
        return subscribeTypeSearchRepository.search(query, pageable).map(subscribeTypeMapper::toDto);
    }
}

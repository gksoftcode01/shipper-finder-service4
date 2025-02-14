package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.StateProvince;
import ai.yarmook.shipperfinder.repository.StateProvinceRepository;
import ai.yarmook.shipperfinder.repository.search.StateProvinceSearchRepository;
import ai.yarmook.shipperfinder.service.StateProvinceService;
import ai.yarmook.shipperfinder.service.dto.StateProvinceDTO;
import ai.yarmook.shipperfinder.service.mapper.StateProvinceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.StateProvince}.
 */
@Service
@Transactional
public class StateProvinceServiceImpl implements StateProvinceService {

    private static final Logger LOG = LoggerFactory.getLogger(StateProvinceServiceImpl.class);

    private final StateProvinceRepository stateProvinceRepository;

    private final StateProvinceMapper stateProvinceMapper;

    private final StateProvinceSearchRepository stateProvinceSearchRepository;

    public StateProvinceServiceImpl(
        StateProvinceRepository stateProvinceRepository,
        StateProvinceMapper stateProvinceMapper,
        StateProvinceSearchRepository stateProvinceSearchRepository
    ) {
        this.stateProvinceRepository = stateProvinceRepository;
        this.stateProvinceMapper = stateProvinceMapper;
        this.stateProvinceSearchRepository = stateProvinceSearchRepository;
    }

    @Override
    public StateProvinceDTO save(StateProvinceDTO stateProvinceDTO) {
        LOG.debug("Request to save StateProvince : {}", stateProvinceDTO);
        StateProvince stateProvince = stateProvinceMapper.toEntity(stateProvinceDTO);
        stateProvince = stateProvinceRepository.save(stateProvince);
        stateProvinceSearchRepository.index(stateProvince);
        return stateProvinceMapper.toDto(stateProvince);
    }

    @Override
    public StateProvinceDTO update(StateProvinceDTO stateProvinceDTO) {
        LOG.debug("Request to update StateProvince : {}", stateProvinceDTO);
        StateProvince stateProvince = stateProvinceMapper.toEntity(stateProvinceDTO);
        stateProvince = stateProvinceRepository.save(stateProvince);
        stateProvinceSearchRepository.index(stateProvince);
        return stateProvinceMapper.toDto(stateProvince);
    }

    @Override
    public Optional<StateProvinceDTO> partialUpdate(StateProvinceDTO stateProvinceDTO) {
        LOG.debug("Request to partially update StateProvince : {}", stateProvinceDTO);

        return stateProvinceRepository
            .findById(stateProvinceDTO.getId())
            .map(existingStateProvince -> {
                stateProvinceMapper.partialUpdate(existingStateProvince, stateProvinceDTO);

                return existingStateProvince;
            })
            .map(stateProvinceRepository::save)
            .map(savedStateProvince -> {
                stateProvinceSearchRepository.index(savedStateProvince);
                return savedStateProvince;
            })
            .map(stateProvinceMapper::toDto);
    }

    public Page<StateProvinceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return stateProvinceRepository.findAllWithEagerRelationships(pageable).map(stateProvinceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StateProvinceDTO> findOne(Long id) {
        LOG.debug("Request to get StateProvince : {}", id);
        return stateProvinceRepository.findOneWithEagerRelationships(id).map(stateProvinceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StateProvince : {}", id);
        stateProvinceRepository.deleteById(id);
        stateProvinceSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StateProvinceDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of StateProvinces for query {}", query);
        return stateProvinceSearchRepository.search(query, pageable).map(stateProvinceMapper::toDto);
    }
}

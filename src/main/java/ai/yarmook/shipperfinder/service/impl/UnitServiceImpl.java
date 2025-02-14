package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.Unit;
import ai.yarmook.shipperfinder.repository.UnitRepository;
import ai.yarmook.shipperfinder.repository.search.UnitSearchRepository;
import ai.yarmook.shipperfinder.service.UnitService;
import ai.yarmook.shipperfinder.service.dto.UnitDTO;
import ai.yarmook.shipperfinder.service.mapper.UnitMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.Unit}.
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    private static final Logger LOG = LoggerFactory.getLogger(UnitServiceImpl.class);

    private final UnitRepository unitRepository;

    private final UnitMapper unitMapper;

    private final UnitSearchRepository unitSearchRepository;

    public UnitServiceImpl(UnitRepository unitRepository, UnitMapper unitMapper, UnitSearchRepository unitSearchRepository) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
        this.unitSearchRepository = unitSearchRepository;
    }

    @Override
    public UnitDTO save(UnitDTO unitDTO) {
        LOG.debug("Request to save Unit : {}", unitDTO);
        Unit unit = unitMapper.toEntity(unitDTO);
        unit = unitRepository.save(unit);
        unitSearchRepository.index(unit);
        return unitMapper.toDto(unit);
    }

    @Override
    public UnitDTO update(UnitDTO unitDTO) {
        LOG.debug("Request to update Unit : {}", unitDTO);
        Unit unit = unitMapper.toEntity(unitDTO);
        unit = unitRepository.save(unit);
        unitSearchRepository.index(unit);
        return unitMapper.toDto(unit);
    }

    @Override
    public Optional<UnitDTO> partialUpdate(UnitDTO unitDTO) {
        LOG.debug("Request to partially update Unit : {}", unitDTO);

        return unitRepository
            .findById(unitDTO.getId())
            .map(existingUnit -> {
                unitMapper.partialUpdate(existingUnit, unitDTO);

                return existingUnit;
            })
            .map(unitRepository::save)
            .map(savedUnit -> {
                unitSearchRepository.index(savedUnit);
                return savedUnit;
            })
            .map(unitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitDTO> findOne(Long id) {
        LOG.debug("Request to get Unit : {}", id);
        return unitRepository.findById(id).map(unitMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Unit : {}", id);
        unitRepository.deleteById(id);
        unitSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Units for query {}", query);
        return unitSearchRepository.search(query, pageable).map(unitMapper::toDto);
    }
}

package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.CargoMsg;
import ai.yarmook.shipperfinder.repository.CargoMsgRepository;
import ai.yarmook.shipperfinder.repository.search.CargoMsgSearchRepository;
import ai.yarmook.shipperfinder.service.CargoMsgService;
import ai.yarmook.shipperfinder.service.dto.CargoMsgDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoMsgMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.CargoMsg}.
 */
@Service
@Transactional
public class CargoMsgServiceImpl implements CargoMsgService {

    private static final Logger LOG = LoggerFactory.getLogger(CargoMsgServiceImpl.class);

    private final CargoMsgRepository cargoMsgRepository;

    private final CargoMsgMapper cargoMsgMapper;

    private final CargoMsgSearchRepository cargoMsgSearchRepository;

    public CargoMsgServiceImpl(
        CargoMsgRepository cargoMsgRepository,
        CargoMsgMapper cargoMsgMapper,
        CargoMsgSearchRepository cargoMsgSearchRepository
    ) {
        this.cargoMsgRepository = cargoMsgRepository;
        this.cargoMsgMapper = cargoMsgMapper;
        this.cargoMsgSearchRepository = cargoMsgSearchRepository;
    }

    @Override
    public CargoMsgDTO save(CargoMsgDTO cargoMsgDTO) {
        LOG.debug("Request to save CargoMsg : {}", cargoMsgDTO);
        CargoMsg cargoMsg = cargoMsgMapper.toEntity(cargoMsgDTO);
        cargoMsg = cargoMsgRepository.save(cargoMsg);
        cargoMsgSearchRepository.index(cargoMsg);
        return cargoMsgMapper.toDto(cargoMsg);
    }

    @Override
    public CargoMsgDTO update(CargoMsgDTO cargoMsgDTO) {
        LOG.debug("Request to update CargoMsg : {}", cargoMsgDTO);
        CargoMsg cargoMsg = cargoMsgMapper.toEntity(cargoMsgDTO);
        cargoMsg = cargoMsgRepository.save(cargoMsg);
        cargoMsgSearchRepository.index(cargoMsg);
        return cargoMsgMapper.toDto(cargoMsg);
    }

    @Override
    public Optional<CargoMsgDTO> partialUpdate(CargoMsgDTO cargoMsgDTO) {
        LOG.debug("Request to partially update CargoMsg : {}", cargoMsgDTO);

        return cargoMsgRepository
            .findById(cargoMsgDTO.getId())
            .map(existingCargoMsg -> {
                cargoMsgMapper.partialUpdate(existingCargoMsg, cargoMsgDTO);

                return existingCargoMsg;
            })
            .map(cargoMsgRepository::save)
            .map(savedCargoMsg -> {
                cargoMsgSearchRepository.index(savedCargoMsg);
                return savedCargoMsg;
            })
            .map(cargoMsgMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CargoMsgDTO> findOne(Long id) {
        LOG.debug("Request to get CargoMsg : {}", id);
        return cargoMsgRepository.findById(id).map(cargoMsgMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CargoMsg : {}", id);
        cargoMsgRepository.deleteById(id);
        cargoMsgSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CargoMsgDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of CargoMsgs for query {}", query);
        return cargoMsgSearchRepository.search(query, pageable).map(cargoMsgMapper::toDto);
    }
}

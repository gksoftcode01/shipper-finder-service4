package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.repository.CargoRequestRepository;
import ai.yarmook.shipperfinder.repository.search.CargoRequestSearchRepository;
import ai.yarmook.shipperfinder.service.CargoRequestService;
import ai.yarmook.shipperfinder.service.dto.CargoRequestDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.CargoRequest}.
 */
@Service
@Transactional
public class CargoRequestServiceImpl implements CargoRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CargoRequestServiceImpl.class);

    private final CargoRequestRepository cargoRequestRepository;

    private final CargoRequestMapper cargoRequestMapper;

    private final CargoRequestSearchRepository cargoRequestSearchRepository;

    public CargoRequestServiceImpl(
        CargoRequestRepository cargoRequestRepository,
        CargoRequestMapper cargoRequestMapper,
        CargoRequestSearchRepository cargoRequestSearchRepository
    ) {
        this.cargoRequestRepository = cargoRequestRepository;
        this.cargoRequestMapper = cargoRequestMapper;
        this.cargoRequestSearchRepository = cargoRequestSearchRepository;
    }

    @Override
    public CargoRequestDTO save(CargoRequestDTO cargoRequestDTO) {
        LOG.debug("Request to save CargoRequest : {}", cargoRequestDTO);
        CargoRequest cargoRequest = cargoRequestMapper.toEntity(cargoRequestDTO);
        cargoRequest = cargoRequestRepository.save(cargoRequest);
        cargoRequestSearchRepository.index(cargoRequest);
        return cargoRequestMapper.toDto(cargoRequest);
    }

    @Override
    public CargoRequestDTO update(CargoRequestDTO cargoRequestDTO) {
        LOG.debug("Request to update CargoRequest : {}", cargoRequestDTO);
        CargoRequest cargoRequest = cargoRequestMapper.toEntity(cargoRequestDTO);
        cargoRequest = cargoRequestRepository.save(cargoRequest);
        cargoRequestSearchRepository.index(cargoRequest);
        return cargoRequestMapper.toDto(cargoRequest);
    }

    @Override
    public Optional<CargoRequestDTO> partialUpdate(CargoRequestDTO cargoRequestDTO) {
        LOG.debug("Request to partially update CargoRequest : {}", cargoRequestDTO);

        return cargoRequestRepository
            .findById(cargoRequestDTO.getId())
            .map(existingCargoRequest -> {
                cargoRequestMapper.partialUpdate(existingCargoRequest, cargoRequestDTO);

                return existingCargoRequest;
            })
            .map(cargoRequestRepository::save)
            .map(savedCargoRequest -> {
                cargoRequestSearchRepository.index(savedCargoRequest);
                return savedCargoRequest;
            })
            .map(cargoRequestMapper::toDto);
    }

    public Page<CargoRequestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cargoRequestRepository.findAllWithEagerRelationships(pageable).map(cargoRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CargoRequestDTO> findOne(Long id) {
        LOG.debug("Request to get CargoRequest : {}", id);
        return cargoRequestRepository.findOneWithEagerRelationships(id).map(cargoRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CargoRequest : {}", id);
        cargoRequestRepository.deleteById(id);
        cargoRequestSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CargoRequestDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of CargoRequests for query {}", query);
        return cargoRequestSearchRepository.search(query, pageable).map(cargoRequestMapper::toDto);
    }
}

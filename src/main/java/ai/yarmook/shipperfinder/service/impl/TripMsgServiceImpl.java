package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.TripMsg;
import ai.yarmook.shipperfinder.repository.TripMsgRepository;
import ai.yarmook.shipperfinder.repository.search.TripMsgSearchRepository;
import ai.yarmook.shipperfinder.service.TripMsgService;
import ai.yarmook.shipperfinder.service.dto.TripMsgDTO;
import ai.yarmook.shipperfinder.service.mapper.TripMsgMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.TripMsg}.
 */
@Service
@Transactional
public class TripMsgServiceImpl implements TripMsgService {

    private static final Logger LOG = LoggerFactory.getLogger(TripMsgServiceImpl.class);

    private final TripMsgRepository tripMsgRepository;

    private final TripMsgMapper tripMsgMapper;

    private final TripMsgSearchRepository tripMsgSearchRepository;

    public TripMsgServiceImpl(
        TripMsgRepository tripMsgRepository,
        TripMsgMapper tripMsgMapper,
        TripMsgSearchRepository tripMsgSearchRepository
    ) {
        this.tripMsgRepository = tripMsgRepository;
        this.tripMsgMapper = tripMsgMapper;
        this.tripMsgSearchRepository = tripMsgSearchRepository;
    }

    @Override
    public TripMsgDTO save(TripMsgDTO tripMsgDTO) {
        LOG.debug("Request to save TripMsg : {}", tripMsgDTO);
        TripMsg tripMsg = tripMsgMapper.toEntity(tripMsgDTO);
        tripMsg = tripMsgRepository.save(tripMsg);
        tripMsgSearchRepository.index(tripMsg);
        return tripMsgMapper.toDto(tripMsg);
    }

    @Override
    public TripMsgDTO update(TripMsgDTO tripMsgDTO) {
        LOG.debug("Request to update TripMsg : {}", tripMsgDTO);
        TripMsg tripMsg = tripMsgMapper.toEntity(tripMsgDTO);
        tripMsg = tripMsgRepository.save(tripMsg);
        tripMsgSearchRepository.index(tripMsg);
        return tripMsgMapper.toDto(tripMsg);
    }

    @Override
    public Optional<TripMsgDTO> partialUpdate(TripMsgDTO tripMsgDTO) {
        LOG.debug("Request to partially update TripMsg : {}", tripMsgDTO);

        return tripMsgRepository
            .findById(tripMsgDTO.getId())
            .map(existingTripMsg -> {
                tripMsgMapper.partialUpdate(existingTripMsg, tripMsgDTO);

                return existingTripMsg;
            })
            .map(tripMsgRepository::save)
            .map(savedTripMsg -> {
                tripMsgSearchRepository.index(savedTripMsg);
                return savedTripMsg;
            })
            .map(tripMsgMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TripMsgDTO> findOne(Long id) {
        LOG.debug("Request to get TripMsg : {}", id);
        return tripMsgRepository.findById(id).map(tripMsgMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TripMsg : {}", id);
        tripMsgRepository.deleteById(id);
        tripMsgSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripMsgDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of TripMsgs for query {}", query);
        return tripMsgSearchRepository.search(query, pageable).map(tripMsgMapper::toDto);
    }
}

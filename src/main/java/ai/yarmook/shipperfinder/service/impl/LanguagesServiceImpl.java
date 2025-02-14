package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.Languages;
import ai.yarmook.shipperfinder.repository.LanguagesRepository;
import ai.yarmook.shipperfinder.repository.search.LanguagesSearchRepository;
import ai.yarmook.shipperfinder.service.LanguagesService;
import ai.yarmook.shipperfinder.service.dto.LanguagesDTO;
import ai.yarmook.shipperfinder.service.mapper.LanguagesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.Languages}.
 */
@Service
@Transactional
public class LanguagesServiceImpl implements LanguagesService {

    private static final Logger LOG = LoggerFactory.getLogger(LanguagesServiceImpl.class);

    private final LanguagesRepository languagesRepository;

    private final LanguagesMapper languagesMapper;

    private final LanguagesSearchRepository languagesSearchRepository;

    public LanguagesServiceImpl(
        LanguagesRepository languagesRepository,
        LanguagesMapper languagesMapper,
        LanguagesSearchRepository languagesSearchRepository
    ) {
        this.languagesRepository = languagesRepository;
        this.languagesMapper = languagesMapper;
        this.languagesSearchRepository = languagesSearchRepository;
    }

    @Override
    public LanguagesDTO save(LanguagesDTO languagesDTO) {
        LOG.debug("Request to save Languages : {}", languagesDTO);
        Languages languages = languagesMapper.toEntity(languagesDTO);
        languages = languagesRepository.save(languages);
        languagesSearchRepository.index(languages);
        return languagesMapper.toDto(languages);
    }

    @Override
    public LanguagesDTO update(LanguagesDTO languagesDTO) {
        LOG.debug("Request to update Languages : {}", languagesDTO);
        Languages languages = languagesMapper.toEntity(languagesDTO);
        languages = languagesRepository.save(languages);
        languagesSearchRepository.index(languages);
        return languagesMapper.toDto(languages);
    }

    @Override
    public Optional<LanguagesDTO> partialUpdate(LanguagesDTO languagesDTO) {
        LOG.debug("Request to partially update Languages : {}", languagesDTO);

        return languagesRepository
            .findById(languagesDTO.getId())
            .map(existingLanguages -> {
                languagesMapper.partialUpdate(existingLanguages, languagesDTO);

                return existingLanguages;
            })
            .map(languagesRepository::save)
            .map(savedLanguages -> {
                languagesSearchRepository.index(savedLanguages);
                return savedLanguages;
            })
            .map(languagesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LanguagesDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Languages");
        return languagesRepository.findAll(pageable).map(languagesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LanguagesDTO> findOne(Long id) {
        LOG.debug("Request to get Languages : {}", id);
        return languagesRepository.findById(id).map(languagesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Languages : {}", id);
        languagesRepository.deleteById(id);
        languagesSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LanguagesDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Languages for query {}", query);
        return languagesSearchRepository.search(query, pageable).map(languagesMapper::toDto);
    }
}

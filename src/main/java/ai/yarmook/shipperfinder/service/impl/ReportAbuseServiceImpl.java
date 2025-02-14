package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.ReportAbuse;
import ai.yarmook.shipperfinder.repository.ReportAbuseRepository;
import ai.yarmook.shipperfinder.repository.search.ReportAbuseSearchRepository;
import ai.yarmook.shipperfinder.service.ReportAbuseService;
import ai.yarmook.shipperfinder.service.dto.ReportAbuseDTO;
import ai.yarmook.shipperfinder.service.mapper.ReportAbuseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.ReportAbuse}.
 */
@Service
@Transactional
public class ReportAbuseServiceImpl implements ReportAbuseService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportAbuseServiceImpl.class);

    private final ReportAbuseRepository reportAbuseRepository;

    private final ReportAbuseMapper reportAbuseMapper;

    private final ReportAbuseSearchRepository reportAbuseSearchRepository;

    public ReportAbuseServiceImpl(
        ReportAbuseRepository reportAbuseRepository,
        ReportAbuseMapper reportAbuseMapper,
        ReportAbuseSearchRepository reportAbuseSearchRepository
    ) {
        this.reportAbuseRepository = reportAbuseRepository;
        this.reportAbuseMapper = reportAbuseMapper;
        this.reportAbuseSearchRepository = reportAbuseSearchRepository;
    }

    @Override
    public ReportAbuseDTO save(ReportAbuseDTO reportAbuseDTO) {
        LOG.debug("Request to save ReportAbuse : {}", reportAbuseDTO);
        ReportAbuse reportAbuse = reportAbuseMapper.toEntity(reportAbuseDTO);
        reportAbuse = reportAbuseRepository.save(reportAbuse);
        reportAbuseSearchRepository.index(reportAbuse);
        return reportAbuseMapper.toDto(reportAbuse);
    }

    @Override
    public ReportAbuseDTO update(ReportAbuseDTO reportAbuseDTO) {
        LOG.debug("Request to update ReportAbuse : {}", reportAbuseDTO);
        ReportAbuse reportAbuse = reportAbuseMapper.toEntity(reportAbuseDTO);
        reportAbuse = reportAbuseRepository.save(reportAbuse);
        reportAbuseSearchRepository.index(reportAbuse);
        return reportAbuseMapper.toDto(reportAbuse);
    }

    @Override
    public Optional<ReportAbuseDTO> partialUpdate(ReportAbuseDTO reportAbuseDTO) {
        LOG.debug("Request to partially update ReportAbuse : {}", reportAbuseDTO);

        return reportAbuseRepository
            .findById(reportAbuseDTO.getId())
            .map(existingReportAbuse -> {
                reportAbuseMapper.partialUpdate(existingReportAbuse, reportAbuseDTO);

                return existingReportAbuse;
            })
            .map(reportAbuseRepository::save)
            .map(savedReportAbuse -> {
                reportAbuseSearchRepository.index(savedReportAbuse);
                return savedReportAbuse;
            })
            .map(reportAbuseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportAbuseDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ReportAbuses");
        return reportAbuseRepository.findAll(pageable).map(reportAbuseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportAbuseDTO> findOne(Long id) {
        LOG.debug("Request to get ReportAbuse : {}", id);
        return reportAbuseRepository.findById(id).map(reportAbuseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReportAbuse : {}", id);
        reportAbuseRepository.deleteById(id);
        reportAbuseSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportAbuseDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ReportAbuses for query {}", query);
        return reportAbuseSearchRepository.search(query, pageable).map(reportAbuseMapper::toDto);
    }
}

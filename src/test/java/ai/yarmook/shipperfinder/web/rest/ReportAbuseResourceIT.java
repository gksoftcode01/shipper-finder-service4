package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.ReportAbuseAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.ReportAbuse;
import ai.yarmook.shipperfinder.domain.enumeration.ReportStatus;
import ai.yarmook.shipperfinder.repository.ReportAbuseRepository;
import ai.yarmook.shipperfinder.repository.search.ReportAbuseSearchRepository;
import ai.yarmook.shipperfinder.service.dto.ReportAbuseDTO;
import ai.yarmook.shipperfinder.service.mapper.ReportAbuseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReportAbuseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportAbuseResourceIT {

    private static final UUID DEFAULT_REPORT_BY_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPORT_BY_ENC_ID = UUID.randomUUID();

    private static final UUID DEFAULT_REPORTED_AGAINST_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPORTED_AGAINST_ENC_ID = UUID.randomUUID();

    private static final Instant DEFAULT_REPORT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REPORT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REPORT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_DATA = "BBBBBBBBBB";

    private static final ReportStatus DEFAULT_REPORT_STATUS = ReportStatus.NEW;
    private static final ReportStatus UPDATED_REPORT_STATUS = ReportStatus.UNDER_PROCESS;

    private static final String ENTITY_API_URL = "/api/report-abuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/report-abuses/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportAbuseRepository reportAbuseRepository;

    @Autowired
    private ReportAbuseMapper reportAbuseMapper;

    @Autowired
    private ReportAbuseSearchRepository reportAbuseSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportAbuseMockMvc;

    private ReportAbuse reportAbuse;

    private ReportAbuse insertedReportAbuse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportAbuse createEntity() {
        return new ReportAbuse()
            .reportByEncId(DEFAULT_REPORT_BY_ENC_ID)
            .reportedAgainstEncId(DEFAULT_REPORTED_AGAINST_ENC_ID)
            .reportDate(DEFAULT_REPORT_DATE)
            .reportData(DEFAULT_REPORT_DATA)
            .reportStatus(DEFAULT_REPORT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportAbuse createUpdatedEntity() {
        return new ReportAbuse()
            .reportByEncId(UPDATED_REPORT_BY_ENC_ID)
            .reportedAgainstEncId(UPDATED_REPORTED_AGAINST_ENC_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .reportData(UPDATED_REPORT_DATA)
            .reportStatus(UPDATED_REPORT_STATUS);
    }

    @BeforeEach
    public void initTest() {
        reportAbuse = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReportAbuse != null) {
            reportAbuseRepository.delete(insertedReportAbuse);
            reportAbuseSearchRepository.delete(insertedReportAbuse);
            insertedReportAbuse = null;
        }
    }

    @Test
    @Transactional
    void createReportAbuse() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        // Create the ReportAbuse
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(reportAbuse);
        var returnedReportAbuseDTO = om.readValue(
            restReportAbuseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportAbuseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportAbuseDTO.class
        );

        // Validate the ReportAbuse in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportAbuse = reportAbuseMapper.toEntity(returnedReportAbuseDTO);
        assertReportAbuseUpdatableFieldsEquals(returnedReportAbuse, getPersistedReportAbuse(returnedReportAbuse));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedReportAbuse = returnedReportAbuse;
    }

    @Test
    @Transactional
    void createReportAbuseWithExistingId() throws Exception {
        // Create the ReportAbuse with an existing ID
        reportAbuse.setId(1L);
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(reportAbuse);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportAbuseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportAbuseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportAbuse in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllReportAbuses() throws Exception {
        // Initialize the database
        insertedReportAbuse = reportAbuseRepository.saveAndFlush(reportAbuse);

        // Get all the reportAbuseList
        restReportAbuseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportAbuse.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportByEncId").value(hasItem(DEFAULT_REPORT_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].reportedAgainstEncId").value(hasItem(DEFAULT_REPORTED_AGAINST_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportData").value(hasItem(DEFAULT_REPORT_DATA)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getReportAbuse() throws Exception {
        // Initialize the database
        insertedReportAbuse = reportAbuseRepository.saveAndFlush(reportAbuse);

        // Get the reportAbuse
        restReportAbuseMockMvc
            .perform(get(ENTITY_API_URL_ID, reportAbuse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportAbuse.getId().intValue()))
            .andExpect(jsonPath("$.reportByEncId").value(DEFAULT_REPORT_BY_ENC_ID.toString()))
            .andExpect(jsonPath("$.reportedAgainstEncId").value(DEFAULT_REPORTED_AGAINST_ENC_ID.toString()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.reportData").value(DEFAULT_REPORT_DATA))
            .andExpect(jsonPath("$.reportStatus").value(DEFAULT_REPORT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReportAbuse() throws Exception {
        // Get the reportAbuse
        restReportAbuseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportAbuse() throws Exception {
        // Initialize the database
        insertedReportAbuse = reportAbuseRepository.saveAndFlush(reportAbuse);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportAbuseSearchRepository.save(reportAbuse);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());

        // Update the reportAbuse
        ReportAbuse updatedReportAbuse = reportAbuseRepository.findById(reportAbuse.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportAbuse are not directly saved in db
        em.detach(updatedReportAbuse);
        updatedReportAbuse
            .reportByEncId(UPDATED_REPORT_BY_ENC_ID)
            .reportedAgainstEncId(UPDATED_REPORTED_AGAINST_ENC_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .reportData(UPDATED_REPORT_DATA)
            .reportStatus(UPDATED_REPORT_STATUS);
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(updatedReportAbuse);

        restReportAbuseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportAbuseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportAbuseDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportAbuse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportAbuseToMatchAllProperties(updatedReportAbuse);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ReportAbuse> reportAbuseSearchList = Streamable.of(reportAbuseSearchRepository.findAll()).toList();
                ReportAbuse testReportAbuseSearch = reportAbuseSearchList.get(searchDatabaseSizeAfter - 1);

                assertReportAbuseAllPropertiesEquals(testReportAbuseSearch, updatedReportAbuse);
            });
    }

    @Test
    @Transactional
    void putNonExistingReportAbuse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        reportAbuse.setId(longCount.incrementAndGet());

        // Create the ReportAbuse
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(reportAbuse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportAbuseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportAbuseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportAbuseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportAbuse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportAbuse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        reportAbuse.setId(longCount.incrementAndGet());

        // Create the ReportAbuse
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(reportAbuse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportAbuseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportAbuseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportAbuse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportAbuse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        reportAbuse.setId(longCount.incrementAndGet());

        // Create the ReportAbuse
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(reportAbuse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportAbuseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportAbuseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportAbuse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateReportAbuseWithPatch() throws Exception {
        // Initialize the database
        insertedReportAbuse = reportAbuseRepository.saveAndFlush(reportAbuse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportAbuse using partial update
        ReportAbuse partialUpdatedReportAbuse = new ReportAbuse();
        partialUpdatedReportAbuse.setId(reportAbuse.getId());

        partialUpdatedReportAbuse
            .reportByEncId(UPDATED_REPORT_BY_ENC_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .reportData(UPDATED_REPORT_DATA)
            .reportStatus(UPDATED_REPORT_STATUS);

        restReportAbuseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportAbuse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportAbuse))
            )
            .andExpect(status().isOk());

        // Validate the ReportAbuse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportAbuseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportAbuse, reportAbuse),
            getPersistedReportAbuse(reportAbuse)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportAbuseWithPatch() throws Exception {
        // Initialize the database
        insertedReportAbuse = reportAbuseRepository.saveAndFlush(reportAbuse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportAbuse using partial update
        ReportAbuse partialUpdatedReportAbuse = new ReportAbuse();
        partialUpdatedReportAbuse.setId(reportAbuse.getId());

        partialUpdatedReportAbuse
            .reportByEncId(UPDATED_REPORT_BY_ENC_ID)
            .reportedAgainstEncId(UPDATED_REPORTED_AGAINST_ENC_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .reportData(UPDATED_REPORT_DATA)
            .reportStatus(UPDATED_REPORT_STATUS);

        restReportAbuseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportAbuse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportAbuse))
            )
            .andExpect(status().isOk());

        // Validate the ReportAbuse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportAbuseUpdatableFieldsEquals(partialUpdatedReportAbuse, getPersistedReportAbuse(partialUpdatedReportAbuse));
    }

    @Test
    @Transactional
    void patchNonExistingReportAbuse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        reportAbuse.setId(longCount.incrementAndGet());

        // Create the ReportAbuse
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(reportAbuse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportAbuseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportAbuseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportAbuseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportAbuse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportAbuse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        reportAbuse.setId(longCount.incrementAndGet());

        // Create the ReportAbuse
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(reportAbuse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportAbuseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportAbuseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportAbuse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportAbuse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        reportAbuse.setId(longCount.incrementAndGet());

        // Create the ReportAbuse
        ReportAbuseDTO reportAbuseDTO = reportAbuseMapper.toDto(reportAbuse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportAbuseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportAbuseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportAbuse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteReportAbuse() throws Exception {
        // Initialize the database
        insertedReportAbuse = reportAbuseRepository.saveAndFlush(reportAbuse);
        reportAbuseRepository.save(reportAbuse);
        reportAbuseSearchRepository.save(reportAbuse);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the reportAbuse
        restReportAbuseMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportAbuse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportAbuseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchReportAbuse() throws Exception {
        // Initialize the database
        insertedReportAbuse = reportAbuseRepository.saveAndFlush(reportAbuse);
        reportAbuseSearchRepository.save(reportAbuse);

        // Search the reportAbuse
        restReportAbuseMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportAbuse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportAbuse.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportByEncId").value(hasItem(DEFAULT_REPORT_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].reportedAgainstEncId").value(hasItem(DEFAULT_REPORTED_AGAINST_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportData").value(hasItem(DEFAULT_REPORT_DATA)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return reportAbuseRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ReportAbuse getPersistedReportAbuse(ReportAbuse reportAbuse) {
        return reportAbuseRepository.findById(reportAbuse.getId()).orElseThrow();
    }

    protected void assertPersistedReportAbuseToMatchAllProperties(ReportAbuse expectedReportAbuse) {
        assertReportAbuseAllPropertiesEquals(expectedReportAbuse, getPersistedReportAbuse(expectedReportAbuse));
    }

    protected void assertPersistedReportAbuseToMatchUpdatableProperties(ReportAbuse expectedReportAbuse) {
        assertReportAbuseAllUpdatablePropertiesEquals(expectedReportAbuse, getPersistedReportAbuse(expectedReportAbuse));
    }
}

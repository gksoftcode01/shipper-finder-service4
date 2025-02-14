package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.UserRateAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.UserRate;
import ai.yarmook.shipperfinder.repository.UserRateRepository;
import ai.yarmook.shipperfinder.repository.search.UserRateSearchRepository;
import ai.yarmook.shipperfinder.service.dto.UserRateDTO;
import ai.yarmook.shipperfinder.service.mapper.UserRateMapper;
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
 * Integration tests for the {@link UserRateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserRateResourceIT {

    private static final Long DEFAULT_RATE = 1L;
    private static final Long UPDATED_RATE = 2L;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_RATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UUID DEFAULT_RATED_BY_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_RATED_BY_ENC_ID = UUID.randomUUID();

    private static final UUID DEFAULT_RATED_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_RATED_ENC_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/user-rates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-rates/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRateRepository userRateRepository;

    @Autowired
    private UserRateMapper userRateMapper;

    @Autowired
    private UserRateSearchRepository userRateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserRateMockMvc;

    private UserRate userRate;

    private UserRate insertedUserRate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRate createEntity() {
        return new UserRate()
            .rate(DEFAULT_RATE)
            .note(DEFAULT_NOTE)
            .rateDate(DEFAULT_RATE_DATE)
            .ratedByEncId(DEFAULT_RATED_BY_ENC_ID)
            .ratedEncId(DEFAULT_RATED_ENC_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRate createUpdatedEntity() {
        return new UserRate()
            .rate(UPDATED_RATE)
            .note(UPDATED_NOTE)
            .rateDate(UPDATED_RATE_DATE)
            .ratedByEncId(UPDATED_RATED_BY_ENC_ID)
            .ratedEncId(UPDATED_RATED_ENC_ID);
    }

    @BeforeEach
    public void initTest() {
        userRate = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserRate != null) {
            userRateRepository.delete(insertedUserRate);
            userRateSearchRepository.delete(insertedUserRate);
            insertedUserRate = null;
        }
    }

    @Test
    @Transactional
    void createUserRate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        // Create the UserRate
        UserRateDTO userRateDTO = userRateMapper.toDto(userRate);
        var returnedUserRateDTO = om.readValue(
            restUserRateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserRateDTO.class
        );

        // Validate the UserRate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserRate = userRateMapper.toEntity(returnedUserRateDTO);
        assertUserRateUpdatableFieldsEquals(returnedUserRate, getPersistedUserRate(returnedUserRate));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedUserRate = returnedUserRate;
    }

    @Test
    @Transactional
    void createUserRateWithExistingId() throws Exception {
        // Create the UserRate with an existing ID
        userRate.setId(1L);
        UserRateDTO userRateDTO = userRateMapper.toDto(userRate);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUserRates() throws Exception {
        // Initialize the database
        insertedUserRate = userRateRepository.saveAndFlush(userRate);

        // Get all the userRateList
        restUserRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].rateDate").value(hasItem(DEFAULT_RATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].ratedByEncId").value(hasItem(DEFAULT_RATED_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].ratedEncId").value(hasItem(DEFAULT_RATED_ENC_ID.toString())));
    }

    @Test
    @Transactional
    void getUserRate() throws Exception {
        // Initialize the database
        insertedUserRate = userRateRepository.saveAndFlush(userRate);

        // Get the userRate
        restUserRateMockMvc
            .perform(get(ENTITY_API_URL_ID, userRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userRate.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.rateDate").value(DEFAULT_RATE_DATE.toString()))
            .andExpect(jsonPath("$.ratedByEncId").value(DEFAULT_RATED_BY_ENC_ID.toString()))
            .andExpect(jsonPath("$.ratedEncId").value(DEFAULT_RATED_ENC_ID.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserRate() throws Exception {
        // Get the userRate
        restUserRateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserRate() throws Exception {
        // Initialize the database
        insertedUserRate = userRateRepository.saveAndFlush(userRate);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRateSearchRepository.save(userRate);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());

        // Update the userRate
        UserRate updatedUserRate = userRateRepository.findById(userRate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserRate are not directly saved in db
        em.detach(updatedUserRate);
        updatedUserRate
            .rate(UPDATED_RATE)
            .note(UPDATED_NOTE)
            .rateDate(UPDATED_RATE_DATE)
            .ratedByEncId(UPDATED_RATED_BY_ENC_ID)
            .ratedEncId(UPDATED_RATED_ENC_ID);
        UserRateDTO userRateDTO = userRateMapper.toDto(updatedUserRate);

        restUserRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRateDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserRateToMatchAllProperties(updatedUserRate);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserRate> userRateSearchList = Streamable.of(userRateSearchRepository.findAll()).toList();
                UserRate testUserRateSearch = userRateSearchList.get(searchDatabaseSizeAfter - 1);

                assertUserRateAllPropertiesEquals(testUserRateSearch, updatedUserRate);
            });
    }

    @Test
    @Transactional
    void putNonExistingUserRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        userRate.setId(longCount.incrementAndGet());

        // Create the UserRate
        UserRateDTO userRateDTO = userRateMapper.toDto(userRate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        userRate.setId(longCount.incrementAndGet());

        // Create the UserRate
        UserRateDTO userRateDTO = userRateMapper.toDto(userRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        userRate.setId(longCount.incrementAndGet());

        // Create the UserRate
        UserRateDTO userRateDTO = userRateMapper.toDto(userRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUserRateWithPatch() throws Exception {
        // Initialize the database
        insertedUserRate = userRateRepository.saveAndFlush(userRate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRate using partial update
        UserRate partialUpdatedUserRate = new UserRate();
        partialUpdatedUserRate.setId(userRate.getId());

        partialUpdatedUserRate.rate(UPDATED_RATE).note(UPDATED_NOTE).ratedByEncId(UPDATED_RATED_BY_ENC_ID).ratedEncId(UPDATED_RATED_ENC_ID);

        restUserRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserRate))
            )
            .andExpect(status().isOk());

        // Validate the UserRate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserRateUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUserRate, userRate), getPersistedUserRate(userRate));
    }

    @Test
    @Transactional
    void fullUpdateUserRateWithPatch() throws Exception {
        // Initialize the database
        insertedUserRate = userRateRepository.saveAndFlush(userRate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRate using partial update
        UserRate partialUpdatedUserRate = new UserRate();
        partialUpdatedUserRate.setId(userRate.getId());

        partialUpdatedUserRate
            .rate(UPDATED_RATE)
            .note(UPDATED_NOTE)
            .rateDate(UPDATED_RATE_DATE)
            .ratedByEncId(UPDATED_RATED_BY_ENC_ID)
            .ratedEncId(UPDATED_RATED_ENC_ID);

        restUserRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserRate))
            )
            .andExpect(status().isOk());

        // Validate the UserRate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserRateUpdatableFieldsEquals(partialUpdatedUserRate, getPersistedUserRate(partialUpdatedUserRate));
    }

    @Test
    @Transactional
    void patchNonExistingUserRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        userRate.setId(longCount.incrementAndGet());

        // Create the UserRate
        UserRateDTO userRateDTO = userRateMapper.toDto(userRate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userRateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        userRate.setId(longCount.incrementAndGet());

        // Create the UserRate
        UserRateDTO userRateDTO = userRateMapper.toDto(userRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        userRate.setId(longCount.incrementAndGet());

        // Create the UserRate
        UserRateDTO userRateDTO = userRateMapper.toDto(userRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userRateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUserRate() throws Exception {
        // Initialize the database
        insertedUserRate = userRateRepository.saveAndFlush(userRate);
        userRateRepository.save(userRate);
        userRateSearchRepository.save(userRate);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userRate
        restUserRateMockMvc
            .perform(delete(ENTITY_API_URL_ID, userRate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userRateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUserRate() throws Exception {
        // Initialize the database
        insertedUserRate = userRateRepository.saveAndFlush(userRate);
        userRateSearchRepository.save(userRate);

        // Search the userRate
        restUserRateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + userRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].rateDate").value(hasItem(DEFAULT_RATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].ratedByEncId").value(hasItem(DEFAULT_RATED_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].ratedEncId").value(hasItem(DEFAULT_RATED_ENC_ID.toString())));
    }

    protected long getRepositoryCount() {
        return userRateRepository.count();
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

    protected UserRate getPersistedUserRate(UserRate userRate) {
        return userRateRepository.findById(userRate.getId()).orElseThrow();
    }

    protected void assertPersistedUserRateToMatchAllProperties(UserRate expectedUserRate) {
        assertUserRateAllPropertiesEquals(expectedUserRate, getPersistedUserRate(expectedUserRate));
    }

    protected void assertPersistedUserRateToMatchUpdatableProperties(UserRate expectedUserRate) {
        assertUserRateAllUpdatablePropertiesEquals(expectedUserRate, getPersistedUserRate(expectedUserRate));
    }
}

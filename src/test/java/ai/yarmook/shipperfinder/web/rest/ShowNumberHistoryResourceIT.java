package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.ShowNumberHistoryAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.ShowNumberHistory;
import ai.yarmook.shipperfinder.repository.ShowNumberHistoryRepository;
import ai.yarmook.shipperfinder.repository.search.ShowNumberHistorySearchRepository;
import ai.yarmook.shipperfinder.service.dto.ShowNumberHistoryDTO;
import ai.yarmook.shipperfinder.service.mapper.ShowNumberHistoryMapper;
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
 * Integration tests for the {@link ShowNumberHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShowNumberHistoryResourceIT {

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UUID DEFAULT_ACTION_BY_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_ACTION_BY_ENC_ID = UUID.randomUUID();

    private static final Integer DEFAULT_ENTITY_TYPE = 1;
    private static final Integer UPDATED_ENTITY_TYPE = 2;
    private static final Integer SMALLER_ENTITY_TYPE = 1 - 1;

    private static final UUID DEFAULT_ENTITY_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_ENTITY_ENC_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/show-number-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/show-number-histories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShowNumberHistoryRepository showNumberHistoryRepository;

    @Autowired
    private ShowNumberHistoryMapper showNumberHistoryMapper;

    @Autowired
    private ShowNumberHistorySearchRepository showNumberHistorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShowNumberHistoryMockMvc;

    private ShowNumberHistory showNumberHistory;

    private ShowNumberHistory insertedShowNumberHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShowNumberHistory createEntity() {
        return new ShowNumberHistory()
            .createdDate(DEFAULT_CREATED_DATE)
            .actionByEncId(DEFAULT_ACTION_BY_ENC_ID)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityEncId(DEFAULT_ENTITY_ENC_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShowNumberHistory createUpdatedEntity() {
        return new ShowNumberHistory()
            .createdDate(UPDATED_CREATED_DATE)
            .actionByEncId(UPDATED_ACTION_BY_ENC_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityEncId(UPDATED_ENTITY_ENC_ID);
    }

    @BeforeEach
    public void initTest() {
        showNumberHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShowNumberHistory != null) {
            showNumberHistoryRepository.delete(insertedShowNumberHistory);
            showNumberHistorySearchRepository.delete(insertedShowNumberHistory);
            insertedShowNumberHistory = null;
        }
    }

    @Test
    @Transactional
    void createShowNumberHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        // Create the ShowNumberHistory
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(showNumberHistory);
        var returnedShowNumberHistoryDTO = om.readValue(
            restShowNumberHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showNumberHistoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShowNumberHistoryDTO.class
        );

        // Validate the ShowNumberHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShowNumberHistory = showNumberHistoryMapper.toEntity(returnedShowNumberHistoryDTO);
        assertShowNumberHistoryUpdatableFieldsEquals(returnedShowNumberHistory, getPersistedShowNumberHistory(returnedShowNumberHistory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedShowNumberHistory = returnedShowNumberHistory;
    }

    @Test
    @Transactional
    void createShowNumberHistoryWithExistingId() throws Exception {
        // Create the ShowNumberHistory with an existing ID
        showNumberHistory.setId(1L);
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(showNumberHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restShowNumberHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showNumberHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShowNumberHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllShowNumberHistories() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList
        restShowNumberHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(showNumberHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].actionByEncId").value(hasItem(DEFAULT_ACTION_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityEncId").value(hasItem(DEFAULT_ENTITY_ENC_ID.toString())));
    }

    @Test
    @Transactional
    void getShowNumberHistory() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get the showNumberHistory
        restShowNumberHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, showNumberHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(showNumberHistory.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.actionByEncId").value(DEFAULT_ACTION_BY_ENC_ID.toString()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityEncId").value(DEFAULT_ENTITY_ENC_ID.toString()));
    }

    @Test
    @Transactional
    void getShowNumberHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        Long id = showNumberHistory.getId();

        defaultShowNumberHistoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShowNumberHistoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShowNumberHistoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where createdDate equals to
        defaultShowNumberHistoryFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where createdDate in
        defaultShowNumberHistoryFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where createdDate is not null
        defaultShowNumberHistoryFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByActionByEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where actionByEncId equals to
        defaultShowNumberHistoryFiltering(
            "actionByEncId.equals=" + DEFAULT_ACTION_BY_ENC_ID,
            "actionByEncId.equals=" + UPDATED_ACTION_BY_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByActionByEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where actionByEncId in
        defaultShowNumberHistoryFiltering(
            "actionByEncId.in=" + DEFAULT_ACTION_BY_ENC_ID + "," + UPDATED_ACTION_BY_ENC_ID,
            "actionByEncId.in=" + UPDATED_ACTION_BY_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByActionByEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where actionByEncId is not null
        defaultShowNumberHistoryFiltering("actionByEncId.specified=true", "actionByEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityType equals to
        defaultShowNumberHistoryFiltering("entityType.equals=" + DEFAULT_ENTITY_TYPE, "entityType.equals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityType in
        defaultShowNumberHistoryFiltering(
            "entityType.in=" + DEFAULT_ENTITY_TYPE + "," + UPDATED_ENTITY_TYPE,
            "entityType.in=" + UPDATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityType is not null
        defaultShowNumberHistoryFiltering("entityType.specified=true", "entityType.specified=false");
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityType is greater than or equal to
        defaultShowNumberHistoryFiltering(
            "entityType.greaterThanOrEqual=" + DEFAULT_ENTITY_TYPE,
            "entityType.greaterThanOrEqual=" + UPDATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityType is less than or equal to
        defaultShowNumberHistoryFiltering(
            "entityType.lessThanOrEqual=" + DEFAULT_ENTITY_TYPE,
            "entityType.lessThanOrEqual=" + SMALLER_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityType is less than
        defaultShowNumberHistoryFiltering("entityType.lessThan=" + UPDATED_ENTITY_TYPE, "entityType.lessThan=" + DEFAULT_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityType is greater than
        defaultShowNumberHistoryFiltering("entityType.greaterThan=" + SMALLER_ENTITY_TYPE, "entityType.greaterThan=" + DEFAULT_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityEncId equals to
        defaultShowNumberHistoryFiltering("entityEncId.equals=" + DEFAULT_ENTITY_ENC_ID, "entityEncId.equals=" + UPDATED_ENTITY_ENC_ID);
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityEncId in
        defaultShowNumberHistoryFiltering(
            "entityEncId.in=" + DEFAULT_ENTITY_ENC_ID + "," + UPDATED_ENTITY_ENC_ID,
            "entityEncId.in=" + UPDATED_ENTITY_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllShowNumberHistoriesByEntityEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        // Get all the showNumberHistoryList where entityEncId is not null
        defaultShowNumberHistoryFiltering("entityEncId.specified=true", "entityEncId.specified=false");
    }

    private void defaultShowNumberHistoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShowNumberHistoryShouldBeFound(shouldBeFound);
        defaultShowNumberHistoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShowNumberHistoryShouldBeFound(String filter) throws Exception {
        restShowNumberHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(showNumberHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].actionByEncId").value(hasItem(DEFAULT_ACTION_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityEncId").value(hasItem(DEFAULT_ENTITY_ENC_ID.toString())));

        // Check, that the count call also returns 1
        restShowNumberHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShowNumberHistoryShouldNotBeFound(String filter) throws Exception {
        restShowNumberHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShowNumberHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShowNumberHistory() throws Exception {
        // Get the showNumberHistory
        restShowNumberHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShowNumberHistory() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        showNumberHistorySearchRepository.save(showNumberHistory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());

        // Update the showNumberHistory
        ShowNumberHistory updatedShowNumberHistory = showNumberHistoryRepository.findById(showNumberHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShowNumberHistory are not directly saved in db
        em.detach(updatedShowNumberHistory);
        updatedShowNumberHistory
            .createdDate(UPDATED_CREATED_DATE)
            .actionByEncId(UPDATED_ACTION_BY_ENC_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityEncId(UPDATED_ENTITY_ENC_ID);
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(updatedShowNumberHistory);

        restShowNumberHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, showNumberHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(showNumberHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShowNumberHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShowNumberHistoryToMatchAllProperties(updatedShowNumberHistory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ShowNumberHistory> showNumberHistorySearchList = Streamable.of(showNumberHistorySearchRepository.findAll()).toList();
                ShowNumberHistory testShowNumberHistorySearch = showNumberHistorySearchList.get(searchDatabaseSizeAfter - 1);

                assertShowNumberHistoryAllPropertiesEquals(testShowNumberHistorySearch, updatedShowNumberHistory);
            });
    }

    @Test
    @Transactional
    void putNonExistingShowNumberHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        showNumberHistory.setId(longCount.incrementAndGet());

        // Create the ShowNumberHistory
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(showNumberHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowNumberHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, showNumberHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(showNumberHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShowNumberHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchShowNumberHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        showNumberHistory.setId(longCount.incrementAndGet());

        // Create the ShowNumberHistory
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(showNumberHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowNumberHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(showNumberHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShowNumberHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShowNumberHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        showNumberHistory.setId(longCount.incrementAndGet());

        // Create the ShowNumberHistory
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(showNumberHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowNumberHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showNumberHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShowNumberHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateShowNumberHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the showNumberHistory using partial update
        ShowNumberHistory partialUpdatedShowNumberHistory = new ShowNumberHistory();
        partialUpdatedShowNumberHistory.setId(showNumberHistory.getId());

        partialUpdatedShowNumberHistory.entityType(UPDATED_ENTITY_TYPE);

        restShowNumberHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShowNumberHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShowNumberHistory))
            )
            .andExpect(status().isOk());

        // Validate the ShowNumberHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShowNumberHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShowNumberHistory, showNumberHistory),
            getPersistedShowNumberHistory(showNumberHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateShowNumberHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the showNumberHistory using partial update
        ShowNumberHistory partialUpdatedShowNumberHistory = new ShowNumberHistory();
        partialUpdatedShowNumberHistory.setId(showNumberHistory.getId());

        partialUpdatedShowNumberHistory
            .createdDate(UPDATED_CREATED_DATE)
            .actionByEncId(UPDATED_ACTION_BY_ENC_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityEncId(UPDATED_ENTITY_ENC_ID);

        restShowNumberHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShowNumberHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShowNumberHistory))
            )
            .andExpect(status().isOk());

        // Validate the ShowNumberHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShowNumberHistoryUpdatableFieldsEquals(
            partialUpdatedShowNumberHistory,
            getPersistedShowNumberHistory(partialUpdatedShowNumberHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingShowNumberHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        showNumberHistory.setId(longCount.incrementAndGet());

        // Create the ShowNumberHistory
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(showNumberHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowNumberHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, showNumberHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(showNumberHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShowNumberHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShowNumberHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        showNumberHistory.setId(longCount.incrementAndGet());

        // Create the ShowNumberHistory
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(showNumberHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowNumberHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(showNumberHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShowNumberHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShowNumberHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        showNumberHistory.setId(longCount.incrementAndGet());

        // Create the ShowNumberHistory
        ShowNumberHistoryDTO showNumberHistoryDTO = showNumberHistoryMapper.toDto(showNumberHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowNumberHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(showNumberHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShowNumberHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteShowNumberHistory() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);
        showNumberHistoryRepository.save(showNumberHistory);
        showNumberHistorySearchRepository.save(showNumberHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the showNumberHistory
        restShowNumberHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, showNumberHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(showNumberHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchShowNumberHistory() throws Exception {
        // Initialize the database
        insertedShowNumberHistory = showNumberHistoryRepository.saveAndFlush(showNumberHistory);
        showNumberHistorySearchRepository.save(showNumberHistory);

        // Search the showNumberHistory
        restShowNumberHistoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + showNumberHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(showNumberHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].actionByEncId").value(hasItem(DEFAULT_ACTION_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityEncId").value(hasItem(DEFAULT_ENTITY_ENC_ID.toString())));
    }

    protected long getRepositoryCount() {
        return showNumberHistoryRepository.count();
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

    protected ShowNumberHistory getPersistedShowNumberHistory(ShowNumberHistory showNumberHistory) {
        return showNumberHistoryRepository.findById(showNumberHistory.getId()).orElseThrow();
    }

    protected void assertPersistedShowNumberHistoryToMatchAllProperties(ShowNumberHistory expectedShowNumberHistory) {
        assertShowNumberHistoryAllPropertiesEquals(expectedShowNumberHistory, getPersistedShowNumberHistory(expectedShowNumberHistory));
    }

    protected void assertPersistedShowNumberHistoryToMatchUpdatableProperties(ShowNumberHistory expectedShowNumberHistory) {
        assertShowNumberHistoryAllUpdatablePropertiesEquals(
            expectedShowNumberHistory,
            getPersistedShowNumberHistory(expectedShowNumberHistory)
        );
    }
}

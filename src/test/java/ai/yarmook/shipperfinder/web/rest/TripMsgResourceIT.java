package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.TripMsgAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.TripMsg;
import ai.yarmook.shipperfinder.repository.TripMsgRepository;
import ai.yarmook.shipperfinder.repository.search.TripMsgSearchRepository;
import ai.yarmook.shipperfinder.service.dto.TripMsgDTO;
import ai.yarmook.shipperfinder.service.mapper.TripMsgMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TripMsgResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripMsgResourceIT {

    private static final String DEFAULT_MSG = "AAAAAAAAAA";
    private static final String UPDATED_MSG = "BBBBBBBBBB";

    private static final UUID DEFAULT_FROM_USER_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_FROM_USER_ENC_ID = UUID.randomUUID();

    private static final UUID DEFAULT_TO_USER_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_TO_USER_ENC_ID = UUID.randomUUID();

    private static final Long DEFAULT_TRIP_ID = 1L;
    private static final Long UPDATED_TRIP_ID = 2L;
    private static final Long SMALLER_TRIP_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/trip-msgs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/trip-msgs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TripMsgRepository tripMsgRepository;

    @Autowired
    private TripMsgMapper tripMsgMapper;

    @Autowired
    private TripMsgSearchRepository tripMsgSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMsgMockMvc;

    private TripMsg tripMsg;

    private TripMsg insertedTripMsg;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripMsg createEntity() {
        return new TripMsg()
            .msg(DEFAULT_MSG)
            .fromUserEncId(DEFAULT_FROM_USER_ENC_ID)
            .toUserEncId(DEFAULT_TO_USER_ENC_ID)
            .tripId(DEFAULT_TRIP_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripMsg createUpdatedEntity() {
        return new TripMsg()
            .msg(UPDATED_MSG)
            .fromUserEncId(UPDATED_FROM_USER_ENC_ID)
            .toUserEncId(UPDATED_TO_USER_ENC_ID)
            .tripId(UPDATED_TRIP_ID);
    }

    @BeforeEach
    public void initTest() {
        tripMsg = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTripMsg != null) {
            tripMsgRepository.delete(insertedTripMsg);
            tripMsgSearchRepository.delete(insertedTripMsg);
            insertedTripMsg = null;
        }
    }

    @Test
    @Transactional
    void createTripMsg() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        // Create the TripMsg
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(tripMsg);
        var returnedTripMsgDTO = om.readValue(
            restTripMsgMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripMsgDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TripMsgDTO.class
        );

        // Validate the TripMsg in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTripMsg = tripMsgMapper.toEntity(returnedTripMsgDTO);
        assertTripMsgUpdatableFieldsEquals(returnedTripMsg, getPersistedTripMsg(returnedTripMsg));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedTripMsg = returnedTripMsg;
    }

    @Test
    @Transactional
    void createTripMsgWithExistingId() throws Exception {
        // Create the TripMsg with an existing ID
        tripMsg.setId(1L);
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(tripMsg);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMsgMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripMsgDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TripMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTripMsgs() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList
        restTripMsgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripMsg.getId().intValue())))
            .andExpect(jsonPath("$.[*].msg").value(hasItem(DEFAULT_MSG)))
            .andExpect(jsonPath("$.[*].fromUserEncId").value(hasItem(DEFAULT_FROM_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].toUserEncId").value(hasItem(DEFAULT_TO_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].tripId").value(hasItem(DEFAULT_TRIP_ID.intValue())));
    }

    @Test
    @Transactional
    void getTripMsg() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get the tripMsg
        restTripMsgMockMvc
            .perform(get(ENTITY_API_URL_ID, tripMsg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripMsg.getId().intValue()))
            .andExpect(jsonPath("$.msg").value(DEFAULT_MSG))
            .andExpect(jsonPath("$.fromUserEncId").value(DEFAULT_FROM_USER_ENC_ID.toString()))
            .andExpect(jsonPath("$.toUserEncId").value(DEFAULT_TO_USER_ENC_ID.toString()))
            .andExpect(jsonPath("$.tripId").value(DEFAULT_TRIP_ID.intValue()));
    }

    @Test
    @Transactional
    void getTripMsgsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        Long id = tripMsg.getId();

        defaultTripMsgFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTripMsgFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTripMsgFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTripMsgsByMsgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where msg equals to
        defaultTripMsgFiltering("msg.equals=" + DEFAULT_MSG, "msg.equals=" + UPDATED_MSG);
    }

    @Test
    @Transactional
    void getAllTripMsgsByMsgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where msg in
        defaultTripMsgFiltering("msg.in=" + DEFAULT_MSG + "," + UPDATED_MSG, "msg.in=" + UPDATED_MSG);
    }

    @Test
    @Transactional
    void getAllTripMsgsByMsgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where msg is not null
        defaultTripMsgFiltering("msg.specified=true", "msg.specified=false");
    }

    @Test
    @Transactional
    void getAllTripMsgsByMsgContainsSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where msg contains
        defaultTripMsgFiltering("msg.contains=" + DEFAULT_MSG, "msg.contains=" + UPDATED_MSG);
    }

    @Test
    @Transactional
    void getAllTripMsgsByMsgNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where msg does not contain
        defaultTripMsgFiltering("msg.doesNotContain=" + UPDATED_MSG, "msg.doesNotContain=" + DEFAULT_MSG);
    }

    @Test
    @Transactional
    void getAllTripMsgsByFromUserEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where fromUserEncId equals to
        defaultTripMsgFiltering("fromUserEncId.equals=" + DEFAULT_FROM_USER_ENC_ID, "fromUserEncId.equals=" + UPDATED_FROM_USER_ENC_ID);
    }

    @Test
    @Transactional
    void getAllTripMsgsByFromUserEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where fromUserEncId in
        defaultTripMsgFiltering(
            "fromUserEncId.in=" + DEFAULT_FROM_USER_ENC_ID + "," + UPDATED_FROM_USER_ENC_ID,
            "fromUserEncId.in=" + UPDATED_FROM_USER_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllTripMsgsByFromUserEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where fromUserEncId is not null
        defaultTripMsgFiltering("fromUserEncId.specified=true", "fromUserEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllTripMsgsByToUserEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where toUserEncId equals to
        defaultTripMsgFiltering("toUserEncId.equals=" + DEFAULT_TO_USER_ENC_ID, "toUserEncId.equals=" + UPDATED_TO_USER_ENC_ID);
    }

    @Test
    @Transactional
    void getAllTripMsgsByToUserEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where toUserEncId in
        defaultTripMsgFiltering(
            "toUserEncId.in=" + DEFAULT_TO_USER_ENC_ID + "," + UPDATED_TO_USER_ENC_ID,
            "toUserEncId.in=" + UPDATED_TO_USER_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllTripMsgsByToUserEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where toUserEncId is not null
        defaultTripMsgFiltering("toUserEncId.specified=true", "toUserEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllTripMsgsByTripIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where tripId equals to
        defaultTripMsgFiltering("tripId.equals=" + DEFAULT_TRIP_ID, "tripId.equals=" + UPDATED_TRIP_ID);
    }

    @Test
    @Transactional
    void getAllTripMsgsByTripIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where tripId in
        defaultTripMsgFiltering("tripId.in=" + DEFAULT_TRIP_ID + "," + UPDATED_TRIP_ID, "tripId.in=" + UPDATED_TRIP_ID);
    }

    @Test
    @Transactional
    void getAllTripMsgsByTripIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where tripId is not null
        defaultTripMsgFiltering("tripId.specified=true", "tripId.specified=false");
    }

    @Test
    @Transactional
    void getAllTripMsgsByTripIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where tripId is greater than or equal to
        defaultTripMsgFiltering("tripId.greaterThanOrEqual=" + DEFAULT_TRIP_ID, "tripId.greaterThanOrEqual=" + UPDATED_TRIP_ID);
    }

    @Test
    @Transactional
    void getAllTripMsgsByTripIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where tripId is less than or equal to
        defaultTripMsgFiltering("tripId.lessThanOrEqual=" + DEFAULT_TRIP_ID, "tripId.lessThanOrEqual=" + SMALLER_TRIP_ID);
    }

    @Test
    @Transactional
    void getAllTripMsgsByTripIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where tripId is less than
        defaultTripMsgFiltering("tripId.lessThan=" + UPDATED_TRIP_ID, "tripId.lessThan=" + DEFAULT_TRIP_ID);
    }

    @Test
    @Transactional
    void getAllTripMsgsByTripIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        // Get all the tripMsgList where tripId is greater than
        defaultTripMsgFiltering("tripId.greaterThan=" + SMALLER_TRIP_ID, "tripId.greaterThan=" + DEFAULT_TRIP_ID);
    }

    private void defaultTripMsgFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTripMsgShouldBeFound(shouldBeFound);
        defaultTripMsgShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTripMsgShouldBeFound(String filter) throws Exception {
        restTripMsgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripMsg.getId().intValue())))
            .andExpect(jsonPath("$.[*].msg").value(hasItem(DEFAULT_MSG)))
            .andExpect(jsonPath("$.[*].fromUserEncId").value(hasItem(DEFAULT_FROM_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].toUserEncId").value(hasItem(DEFAULT_TO_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].tripId").value(hasItem(DEFAULT_TRIP_ID.intValue())));

        // Check, that the count call also returns 1
        restTripMsgMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTripMsgShouldNotBeFound(String filter) throws Exception {
        restTripMsgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTripMsgMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTripMsg() throws Exception {
        // Get the tripMsg
        restTripMsgMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTripMsg() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripMsgSearchRepository.save(tripMsg);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());

        // Update the tripMsg
        TripMsg updatedTripMsg = tripMsgRepository.findById(tripMsg.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTripMsg are not directly saved in db
        em.detach(updatedTripMsg);
        updatedTripMsg.msg(UPDATED_MSG).fromUserEncId(UPDATED_FROM_USER_ENC_ID).toUserEncId(UPDATED_TO_USER_ENC_ID).tripId(UPDATED_TRIP_ID);
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(updatedTripMsg);

        restTripMsgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripMsgDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripMsgDTO))
            )
            .andExpect(status().isOk());

        // Validate the TripMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTripMsgToMatchAllProperties(updatedTripMsg);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TripMsg> tripMsgSearchList = Streamable.of(tripMsgSearchRepository.findAll()).toList();
                TripMsg testTripMsgSearch = tripMsgSearchList.get(searchDatabaseSizeAfter - 1);

                assertTripMsgAllPropertiesEquals(testTripMsgSearch, updatedTripMsg);
            });
    }

    @Test
    @Transactional
    void putNonExistingTripMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        tripMsg.setId(longCount.incrementAndGet());

        // Create the TripMsg
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(tripMsg);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMsgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripMsgDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripMsgDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        tripMsg.setId(longCount.incrementAndGet());

        // Create the TripMsg
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(tripMsg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMsgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripMsgDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        tripMsg.setId(longCount.incrementAndGet());

        // Create the TripMsg
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(tripMsg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMsgMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripMsgDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTripMsgWithPatch() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tripMsg using partial update
        TripMsg partialUpdatedTripMsg = new TripMsg();
        partialUpdatedTripMsg.setId(tripMsg.getId());

        partialUpdatedTripMsg
            .msg(UPDATED_MSG)
            .fromUserEncId(UPDATED_FROM_USER_ENC_ID)
            .toUserEncId(UPDATED_TO_USER_ENC_ID)
            .tripId(UPDATED_TRIP_ID);

        restTripMsgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripMsg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTripMsg))
            )
            .andExpect(status().isOk());

        // Validate the TripMsg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripMsgUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTripMsg, tripMsg), getPersistedTripMsg(tripMsg));
    }

    @Test
    @Transactional
    void fullUpdateTripMsgWithPatch() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tripMsg using partial update
        TripMsg partialUpdatedTripMsg = new TripMsg();
        partialUpdatedTripMsg.setId(tripMsg.getId());

        partialUpdatedTripMsg
            .msg(UPDATED_MSG)
            .fromUserEncId(UPDATED_FROM_USER_ENC_ID)
            .toUserEncId(UPDATED_TO_USER_ENC_ID)
            .tripId(UPDATED_TRIP_ID);

        restTripMsgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripMsg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTripMsg))
            )
            .andExpect(status().isOk());

        // Validate the TripMsg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripMsgUpdatableFieldsEquals(partialUpdatedTripMsg, getPersistedTripMsg(partialUpdatedTripMsg));
    }

    @Test
    @Transactional
    void patchNonExistingTripMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        tripMsg.setId(longCount.incrementAndGet());

        // Create the TripMsg
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(tripMsg);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMsgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripMsgDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tripMsgDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        tripMsg.setId(longCount.incrementAndGet());

        // Create the TripMsg
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(tripMsg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMsgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tripMsgDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        tripMsg.setId(longCount.incrementAndGet());

        // Create the TripMsg
        TripMsgDTO tripMsgDTO = tripMsgMapper.toDto(tripMsg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMsgMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tripMsgDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTripMsg() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);
        tripMsgRepository.save(tripMsg);
        tripMsgSearchRepository.save(tripMsg);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tripMsg
        restTripMsgMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripMsg.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTripMsg() throws Exception {
        // Initialize the database
        insertedTripMsg = tripMsgRepository.saveAndFlush(tripMsg);
        tripMsgSearchRepository.save(tripMsg);

        // Search the tripMsg
        restTripMsgMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tripMsg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripMsg.getId().intValue())))
            .andExpect(jsonPath("$.[*].msg").value(hasItem(DEFAULT_MSG)))
            .andExpect(jsonPath("$.[*].fromUserEncId").value(hasItem(DEFAULT_FROM_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].toUserEncId").value(hasItem(DEFAULT_TO_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].tripId").value(hasItem(DEFAULT_TRIP_ID.intValue())));
    }

    protected long getRepositoryCount() {
        return tripMsgRepository.count();
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

    protected TripMsg getPersistedTripMsg(TripMsg tripMsg) {
        return tripMsgRepository.findById(tripMsg.getId()).orElseThrow();
    }

    protected void assertPersistedTripMsgToMatchAllProperties(TripMsg expectedTripMsg) {
        assertTripMsgAllPropertiesEquals(expectedTripMsg, getPersistedTripMsg(expectedTripMsg));
    }

    protected void assertPersistedTripMsgToMatchUpdatableProperties(TripMsg expectedTripMsg) {
        assertTripMsgAllUpdatablePropertiesEquals(expectedTripMsg, getPersistedTripMsg(expectedTripMsg));
    }
}

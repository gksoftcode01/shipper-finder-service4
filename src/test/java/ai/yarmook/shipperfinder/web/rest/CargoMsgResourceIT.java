package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.CargoMsgAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.CargoMsg;
import ai.yarmook.shipperfinder.repository.CargoMsgRepository;
import ai.yarmook.shipperfinder.repository.search.CargoMsgSearchRepository;
import ai.yarmook.shipperfinder.service.dto.CargoMsgDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoMsgMapper;
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
 * Integration tests for the {@link CargoMsgResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CargoMsgResourceIT {

    private static final String DEFAULT_MSG = "AAAAAAAAAA";
    private static final String UPDATED_MSG = "BBBBBBBBBB";

    private static final UUID DEFAULT_FROM_USER_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_FROM_USER_ENC_ID = UUID.randomUUID();

    private static final UUID DEFAULT_TO_USER_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_TO_USER_ENC_ID = UUID.randomUUID();

    private static final Long DEFAULT_CARGO_REQUEST_ID = 1L;
    private static final Long UPDATED_CARGO_REQUEST_ID = 2L;
    private static final Long SMALLER_CARGO_REQUEST_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/cargo-msgs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/cargo-msgs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CargoMsgRepository cargoMsgRepository;

    @Autowired
    private CargoMsgMapper cargoMsgMapper;

    @Autowired
    private CargoMsgSearchRepository cargoMsgSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoMsgMockMvc;

    private CargoMsg cargoMsg;

    private CargoMsg insertedCargoMsg;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoMsg createEntity() {
        return new CargoMsg()
            .msg(DEFAULT_MSG)
            .fromUserEncId(DEFAULT_FROM_USER_ENC_ID)
            .toUserEncId(DEFAULT_TO_USER_ENC_ID)
            .cargoRequestId(DEFAULT_CARGO_REQUEST_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoMsg createUpdatedEntity() {
        return new CargoMsg()
            .msg(UPDATED_MSG)
            .fromUserEncId(UPDATED_FROM_USER_ENC_ID)
            .toUserEncId(UPDATED_TO_USER_ENC_ID)
            .cargoRequestId(UPDATED_CARGO_REQUEST_ID);
    }

    @BeforeEach
    public void initTest() {
        cargoMsg = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCargoMsg != null) {
            cargoMsgRepository.delete(insertedCargoMsg);
            cargoMsgSearchRepository.delete(insertedCargoMsg);
            insertedCargoMsg = null;
        }
    }

    @Test
    @Transactional
    void createCargoMsg() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        // Create the CargoMsg
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(cargoMsg);
        var returnedCargoMsgDTO = om.readValue(
            restCargoMsgMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoMsgDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CargoMsgDTO.class
        );

        // Validate the CargoMsg in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCargoMsg = cargoMsgMapper.toEntity(returnedCargoMsgDTO);
        assertCargoMsgUpdatableFieldsEquals(returnedCargoMsg, getPersistedCargoMsg(returnedCargoMsg));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCargoMsg = returnedCargoMsg;
    }

    @Test
    @Transactional
    void createCargoMsgWithExistingId() throws Exception {
        // Create the CargoMsg with an existing ID
        cargoMsg.setId(1L);
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(cargoMsg);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoMsgMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoMsgDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CargoMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCargoMsgs() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList
        restCargoMsgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoMsg.getId().intValue())))
            .andExpect(jsonPath("$.[*].msg").value(hasItem(DEFAULT_MSG)))
            .andExpect(jsonPath("$.[*].fromUserEncId").value(hasItem(DEFAULT_FROM_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].toUserEncId").value(hasItem(DEFAULT_TO_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].cargoRequestId").value(hasItem(DEFAULT_CARGO_REQUEST_ID.intValue())));
    }

    @Test
    @Transactional
    void getCargoMsg() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get the cargoMsg
        restCargoMsgMockMvc
            .perform(get(ENTITY_API_URL_ID, cargoMsg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargoMsg.getId().intValue()))
            .andExpect(jsonPath("$.msg").value(DEFAULT_MSG))
            .andExpect(jsonPath("$.fromUserEncId").value(DEFAULT_FROM_USER_ENC_ID.toString()))
            .andExpect(jsonPath("$.toUserEncId").value(DEFAULT_TO_USER_ENC_ID.toString()))
            .andExpect(jsonPath("$.cargoRequestId").value(DEFAULT_CARGO_REQUEST_ID.intValue()));
    }

    @Test
    @Transactional
    void getCargoMsgsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        Long id = cargoMsg.getId();

        defaultCargoMsgFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCargoMsgFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCargoMsgFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCargoMsgsByMsgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where msg equals to
        defaultCargoMsgFiltering("msg.equals=" + DEFAULT_MSG, "msg.equals=" + UPDATED_MSG);
    }

    @Test
    @Transactional
    void getAllCargoMsgsByMsgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where msg in
        defaultCargoMsgFiltering("msg.in=" + DEFAULT_MSG + "," + UPDATED_MSG, "msg.in=" + UPDATED_MSG);
    }

    @Test
    @Transactional
    void getAllCargoMsgsByMsgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where msg is not null
        defaultCargoMsgFiltering("msg.specified=true", "msg.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoMsgsByMsgContainsSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where msg contains
        defaultCargoMsgFiltering("msg.contains=" + DEFAULT_MSG, "msg.contains=" + UPDATED_MSG);
    }

    @Test
    @Transactional
    void getAllCargoMsgsByMsgNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where msg does not contain
        defaultCargoMsgFiltering("msg.doesNotContain=" + UPDATED_MSG, "msg.doesNotContain=" + DEFAULT_MSG);
    }

    @Test
    @Transactional
    void getAllCargoMsgsByFromUserEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where fromUserEncId equals to
        defaultCargoMsgFiltering("fromUserEncId.equals=" + DEFAULT_FROM_USER_ENC_ID, "fromUserEncId.equals=" + UPDATED_FROM_USER_ENC_ID);
    }

    @Test
    @Transactional
    void getAllCargoMsgsByFromUserEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where fromUserEncId in
        defaultCargoMsgFiltering(
            "fromUserEncId.in=" + DEFAULT_FROM_USER_ENC_ID + "," + UPDATED_FROM_USER_ENC_ID,
            "fromUserEncId.in=" + UPDATED_FROM_USER_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoMsgsByFromUserEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where fromUserEncId is not null
        defaultCargoMsgFiltering("fromUserEncId.specified=true", "fromUserEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoMsgsByToUserEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where toUserEncId equals to
        defaultCargoMsgFiltering("toUserEncId.equals=" + DEFAULT_TO_USER_ENC_ID, "toUserEncId.equals=" + UPDATED_TO_USER_ENC_ID);
    }

    @Test
    @Transactional
    void getAllCargoMsgsByToUserEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where toUserEncId in
        defaultCargoMsgFiltering(
            "toUserEncId.in=" + DEFAULT_TO_USER_ENC_ID + "," + UPDATED_TO_USER_ENC_ID,
            "toUserEncId.in=" + UPDATED_TO_USER_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoMsgsByToUserEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where toUserEncId is not null
        defaultCargoMsgFiltering("toUserEncId.specified=true", "toUserEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoMsgsByCargoRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where cargoRequestId equals to
        defaultCargoMsgFiltering("cargoRequestId.equals=" + DEFAULT_CARGO_REQUEST_ID, "cargoRequestId.equals=" + UPDATED_CARGO_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllCargoMsgsByCargoRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where cargoRequestId in
        defaultCargoMsgFiltering(
            "cargoRequestId.in=" + DEFAULT_CARGO_REQUEST_ID + "," + UPDATED_CARGO_REQUEST_ID,
            "cargoRequestId.in=" + UPDATED_CARGO_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoMsgsByCargoRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where cargoRequestId is not null
        defaultCargoMsgFiltering("cargoRequestId.specified=true", "cargoRequestId.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoMsgsByCargoRequestIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where cargoRequestId is greater than or equal to
        defaultCargoMsgFiltering(
            "cargoRequestId.greaterThanOrEqual=" + DEFAULT_CARGO_REQUEST_ID,
            "cargoRequestId.greaterThanOrEqual=" + UPDATED_CARGO_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoMsgsByCargoRequestIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where cargoRequestId is less than or equal to
        defaultCargoMsgFiltering(
            "cargoRequestId.lessThanOrEqual=" + DEFAULT_CARGO_REQUEST_ID,
            "cargoRequestId.lessThanOrEqual=" + SMALLER_CARGO_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoMsgsByCargoRequestIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where cargoRequestId is less than
        defaultCargoMsgFiltering(
            "cargoRequestId.lessThan=" + UPDATED_CARGO_REQUEST_ID,
            "cargoRequestId.lessThan=" + DEFAULT_CARGO_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoMsgsByCargoRequestIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        // Get all the cargoMsgList where cargoRequestId is greater than
        defaultCargoMsgFiltering(
            "cargoRequestId.greaterThan=" + SMALLER_CARGO_REQUEST_ID,
            "cargoRequestId.greaterThan=" + DEFAULT_CARGO_REQUEST_ID
        );
    }

    private void defaultCargoMsgFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCargoMsgShouldBeFound(shouldBeFound);
        defaultCargoMsgShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCargoMsgShouldBeFound(String filter) throws Exception {
        restCargoMsgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoMsg.getId().intValue())))
            .andExpect(jsonPath("$.[*].msg").value(hasItem(DEFAULT_MSG)))
            .andExpect(jsonPath("$.[*].fromUserEncId").value(hasItem(DEFAULT_FROM_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].toUserEncId").value(hasItem(DEFAULT_TO_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].cargoRequestId").value(hasItem(DEFAULT_CARGO_REQUEST_ID.intValue())));

        // Check, that the count call also returns 1
        restCargoMsgMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCargoMsgShouldNotBeFound(String filter) throws Exception {
        restCargoMsgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCargoMsgMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCargoMsg() throws Exception {
        // Get the cargoMsg
        restCargoMsgMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCargoMsg() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoMsgSearchRepository.save(cargoMsg);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());

        // Update the cargoMsg
        CargoMsg updatedCargoMsg = cargoMsgRepository.findById(cargoMsg.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCargoMsg are not directly saved in db
        em.detach(updatedCargoMsg);
        updatedCargoMsg
            .msg(UPDATED_MSG)
            .fromUserEncId(UPDATED_FROM_USER_ENC_ID)
            .toUserEncId(UPDATED_TO_USER_ENC_ID)
            .cargoRequestId(UPDATED_CARGO_REQUEST_ID);
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(updatedCargoMsg);

        restCargoMsgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoMsgDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoMsgDTO))
            )
            .andExpect(status().isOk());

        // Validate the CargoMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCargoMsgToMatchAllProperties(updatedCargoMsg);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CargoMsg> cargoMsgSearchList = Streamable.of(cargoMsgSearchRepository.findAll()).toList();
                CargoMsg testCargoMsgSearch = cargoMsgSearchList.get(searchDatabaseSizeAfter - 1);

                assertCargoMsgAllPropertiesEquals(testCargoMsgSearch, updatedCargoMsg);
            });
    }

    @Test
    @Transactional
    void putNonExistingCargoMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        cargoMsg.setId(longCount.incrementAndGet());

        // Create the CargoMsg
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(cargoMsg);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoMsgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoMsgDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoMsgDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargoMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        cargoMsg.setId(longCount.incrementAndGet());

        // Create the CargoMsg
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(cargoMsg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMsgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoMsgDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargoMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        cargoMsg.setId(longCount.incrementAndGet());

        // Create the CargoMsg
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(cargoMsg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMsgMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoMsgDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCargoMsgWithPatch() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoMsg using partial update
        CargoMsg partialUpdatedCargoMsg = new CargoMsg();
        partialUpdatedCargoMsg.setId(cargoMsg.getId());

        partialUpdatedCargoMsg
            .msg(UPDATED_MSG)
            .fromUserEncId(UPDATED_FROM_USER_ENC_ID)
            .toUserEncId(UPDATED_TO_USER_ENC_ID)
            .cargoRequestId(UPDATED_CARGO_REQUEST_ID);

        restCargoMsgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoMsg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargoMsg))
            )
            .andExpect(status().isOk());

        // Validate the CargoMsg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoMsgUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCargoMsg, cargoMsg), getPersistedCargoMsg(cargoMsg));
    }

    @Test
    @Transactional
    void fullUpdateCargoMsgWithPatch() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoMsg using partial update
        CargoMsg partialUpdatedCargoMsg = new CargoMsg();
        partialUpdatedCargoMsg.setId(cargoMsg.getId());

        partialUpdatedCargoMsg
            .msg(UPDATED_MSG)
            .fromUserEncId(UPDATED_FROM_USER_ENC_ID)
            .toUserEncId(UPDATED_TO_USER_ENC_ID)
            .cargoRequestId(UPDATED_CARGO_REQUEST_ID);

        restCargoMsgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoMsg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargoMsg))
            )
            .andExpect(status().isOk());

        // Validate the CargoMsg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoMsgUpdatableFieldsEquals(partialUpdatedCargoMsg, getPersistedCargoMsg(partialUpdatedCargoMsg));
    }

    @Test
    @Transactional
    void patchNonExistingCargoMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        cargoMsg.setId(longCount.incrementAndGet());

        // Create the CargoMsg
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(cargoMsg);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoMsgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoMsgDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoMsgDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargoMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        cargoMsg.setId(longCount.incrementAndGet());

        // Create the CargoMsg
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(cargoMsg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMsgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoMsgDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargoMsg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        cargoMsg.setId(longCount.incrementAndGet());

        // Create the CargoMsg
        CargoMsgDTO cargoMsgDTO = cargoMsgMapper.toDto(cargoMsg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMsgMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cargoMsgDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoMsg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCargoMsg() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);
        cargoMsgRepository.save(cargoMsg);
        cargoMsgSearchRepository.save(cargoMsg);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the cargoMsg
        restCargoMsgMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargoMsg.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoMsgSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCargoMsg() throws Exception {
        // Initialize the database
        insertedCargoMsg = cargoMsgRepository.saveAndFlush(cargoMsg);
        cargoMsgSearchRepository.save(cargoMsg);

        // Search the cargoMsg
        restCargoMsgMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cargoMsg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoMsg.getId().intValue())))
            .andExpect(jsonPath("$.[*].msg").value(hasItem(DEFAULT_MSG)))
            .andExpect(jsonPath("$.[*].fromUserEncId").value(hasItem(DEFAULT_FROM_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].toUserEncId").value(hasItem(DEFAULT_TO_USER_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].cargoRequestId").value(hasItem(DEFAULT_CARGO_REQUEST_ID.intValue())));
    }

    protected long getRepositoryCount() {
        return cargoMsgRepository.count();
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

    protected CargoMsg getPersistedCargoMsg(CargoMsg cargoMsg) {
        return cargoMsgRepository.findById(cargoMsg.getId()).orElseThrow();
    }

    protected void assertPersistedCargoMsgToMatchAllProperties(CargoMsg expectedCargoMsg) {
        assertCargoMsgAllPropertiesEquals(expectedCargoMsg, getPersistedCargoMsg(expectedCargoMsg));
    }

    protected void assertPersistedCargoMsgToMatchUpdatableProperties(CargoMsg expectedCargoMsg) {
        assertCargoMsgAllUpdatablePropertiesEquals(expectedCargoMsg, getPersistedCargoMsg(expectedCargoMsg));
    }
}

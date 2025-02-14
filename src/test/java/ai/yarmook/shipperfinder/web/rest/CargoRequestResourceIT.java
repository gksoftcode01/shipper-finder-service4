package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.CargoRequestAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.domain.StateProvince;
import ai.yarmook.shipperfinder.domain.enumeration.CargoRequestStatus;
import ai.yarmook.shipperfinder.repository.CargoRequestRepository;
import ai.yarmook.shipperfinder.repository.search.CargoRequestSearchRepository;
import ai.yarmook.shipperfinder.service.CargoRequestService;
import ai.yarmook.shipperfinder.service.dto.CargoRequestDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoRequestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CargoRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CargoRequestResourceIT {

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALID_UNTIL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_UNTIL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CargoRequestStatus DEFAULT_STATUS = CargoRequestStatus.NEW;
    private static final CargoRequestStatus UPDATED_STATUS = CargoRequestStatus.PENDING;

    private static final Boolean DEFAULT_IS_NEGOTIABLE = false;
    private static final Boolean UPDATED_IS_NEGOTIABLE = true;

    private static final Float DEFAULT_BUDGET = 1F;
    private static final Float UPDATED_BUDGET = 2F;
    private static final Float SMALLER_BUDGET = 1F - 1F;

    private static final UUID DEFAULT_CREATED_BY_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_CREATED_BY_ENC_ID = UUID.randomUUID();

    private static final UUID DEFAULT_TAKEN_BY_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_TAKEN_BY_ENC_ID = UUID.randomUUID();

    private static final UUID DEFAULT_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_ENC_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/cargo-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/cargo-requests/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CargoRequestRepository cargoRequestRepository;

    @Mock
    private CargoRequestRepository cargoRequestRepositoryMock;

    @Autowired
    private CargoRequestMapper cargoRequestMapper;

    @Mock
    private CargoRequestService cargoRequestServiceMock;

    @Autowired
    private CargoRequestSearchRepository cargoRequestSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoRequestMockMvc;

    private CargoRequest cargoRequest;

    private CargoRequest insertedCargoRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequest createEntity() {
        return new CargoRequest()
            .createDate(DEFAULT_CREATE_DATE)
            .validUntil(DEFAULT_VALID_UNTIL)
            .status(DEFAULT_STATUS)
            .isNegotiable(DEFAULT_IS_NEGOTIABLE)
            .budget(DEFAULT_BUDGET)
            .createdByEncId(DEFAULT_CREATED_BY_ENC_ID)
            .takenByEncId(DEFAULT_TAKEN_BY_ENC_ID)
            .encId(DEFAULT_ENC_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequest createUpdatedEntity() {
        return new CargoRequest()
            .createDate(UPDATED_CREATE_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .status(UPDATED_STATUS)
            .isNegotiable(UPDATED_IS_NEGOTIABLE)
            .budget(UPDATED_BUDGET)
            .createdByEncId(UPDATED_CREATED_BY_ENC_ID)
            .takenByEncId(UPDATED_TAKEN_BY_ENC_ID)
            .encId(UPDATED_ENC_ID);
    }

    @BeforeEach
    public void initTest() {
        cargoRequest = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCargoRequest != null) {
            cargoRequestRepository.delete(insertedCargoRequest);
            cargoRequestSearchRepository.delete(insertedCargoRequest);
            insertedCargoRequest = null;
        }
    }

    @Test
    @Transactional
    void createCargoRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        // Create the CargoRequest
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(cargoRequest);
        var returnedCargoRequestDTO = om.readValue(
            restCargoRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CargoRequestDTO.class
        );

        // Validate the CargoRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCargoRequest = cargoRequestMapper.toEntity(returnedCargoRequestDTO);
        assertCargoRequestUpdatableFieldsEquals(returnedCargoRequest, getPersistedCargoRequest(returnedCargoRequest));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCargoRequest = returnedCargoRequest;
    }

    @Test
    @Transactional
    void createCargoRequestWithExistingId() throws Exception {
        // Create the CargoRequest with an existing ID
        cargoRequest.setId(1L);
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(cargoRequest);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCargoRequests() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList
        restCargoRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isNegotiable").value(hasItem(DEFAULT_IS_NEGOTIABLE)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].createdByEncId").value(hasItem(DEFAULT_CREATED_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].takenByEncId").value(hasItem(DEFAULT_TAKEN_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCargoRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cargoRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCargoRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cargoRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCargoRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cargoRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCargoRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cargoRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCargoRequest() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get the cargoRequest
        restCargoRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, cargoRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargoRequest.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.validUntil").value(DEFAULT_VALID_UNTIL.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isNegotiable").value(DEFAULT_IS_NEGOTIABLE))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.doubleValue()))
            .andExpect(jsonPath("$.createdByEncId").value(DEFAULT_CREATED_BY_ENC_ID.toString()))
            .andExpect(jsonPath("$.takenByEncId").value(DEFAULT_TAKEN_BY_ENC_ID.toString()))
            .andExpect(jsonPath("$.encId").value(DEFAULT_ENC_ID.toString()));
    }

    @Test
    @Transactional
    void getCargoRequestsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        Long id = cargoRequest.getId();

        defaultCargoRequestFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCargoRequestFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCargoRequestFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where createDate equals to
        defaultCargoRequestFiltering("createDate.equals=" + DEFAULT_CREATE_DATE, "createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where createDate in
        defaultCargoRequestFiltering(
            "createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE,
            "createDate.in=" + UPDATED_CREATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCargoRequestsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where createDate is not null
        defaultCargoRequestFiltering("createDate.specified=true", "createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestsByValidUntilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where validUntil equals to
        defaultCargoRequestFiltering("validUntil.equals=" + DEFAULT_VALID_UNTIL, "validUntil.equals=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByValidUntilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where validUntil in
        defaultCargoRequestFiltering(
            "validUntil.in=" + DEFAULT_VALID_UNTIL + "," + UPDATED_VALID_UNTIL,
            "validUntil.in=" + UPDATED_VALID_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllCargoRequestsByValidUntilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where validUntil is not null
        defaultCargoRequestFiltering("validUntil.specified=true", "validUntil.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where status equals to
        defaultCargoRequestFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where status in
        defaultCargoRequestFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where status is not null
        defaultCargoRequestFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestsByIsNegotiableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where isNegotiable equals to
        defaultCargoRequestFiltering("isNegotiable.equals=" + DEFAULT_IS_NEGOTIABLE, "isNegotiable.equals=" + UPDATED_IS_NEGOTIABLE);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByIsNegotiableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where isNegotiable in
        defaultCargoRequestFiltering(
            "isNegotiable.in=" + DEFAULT_IS_NEGOTIABLE + "," + UPDATED_IS_NEGOTIABLE,
            "isNegotiable.in=" + UPDATED_IS_NEGOTIABLE
        );
    }

    @Test
    @Transactional
    void getAllCargoRequestsByIsNegotiableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where isNegotiable is not null
        defaultCargoRequestFiltering("isNegotiable.specified=true", "isNegotiable.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestsByBudgetIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where budget equals to
        defaultCargoRequestFiltering("budget.equals=" + DEFAULT_BUDGET, "budget.equals=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByBudgetIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where budget in
        defaultCargoRequestFiltering("budget.in=" + DEFAULT_BUDGET + "," + UPDATED_BUDGET, "budget.in=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByBudgetIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where budget is not null
        defaultCargoRequestFiltering("budget.specified=true", "budget.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestsByBudgetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where budget is greater than or equal to
        defaultCargoRequestFiltering("budget.greaterThanOrEqual=" + DEFAULT_BUDGET, "budget.greaterThanOrEqual=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByBudgetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where budget is less than or equal to
        defaultCargoRequestFiltering("budget.lessThanOrEqual=" + DEFAULT_BUDGET, "budget.lessThanOrEqual=" + SMALLER_BUDGET);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByBudgetIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where budget is less than
        defaultCargoRequestFiltering("budget.lessThan=" + UPDATED_BUDGET, "budget.lessThan=" + DEFAULT_BUDGET);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByBudgetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where budget is greater than
        defaultCargoRequestFiltering("budget.greaterThan=" + SMALLER_BUDGET, "budget.greaterThan=" + DEFAULT_BUDGET);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByCreatedByEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where createdByEncId equals to
        defaultCargoRequestFiltering(
            "createdByEncId.equals=" + DEFAULT_CREATED_BY_ENC_ID,
            "createdByEncId.equals=" + UPDATED_CREATED_BY_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoRequestsByCreatedByEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where createdByEncId in
        defaultCargoRequestFiltering(
            "createdByEncId.in=" + DEFAULT_CREATED_BY_ENC_ID + "," + UPDATED_CREATED_BY_ENC_ID,
            "createdByEncId.in=" + UPDATED_CREATED_BY_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoRequestsByCreatedByEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where createdByEncId is not null
        defaultCargoRequestFiltering("createdByEncId.specified=true", "createdByEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestsByTakenByEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where takenByEncId equals to
        defaultCargoRequestFiltering("takenByEncId.equals=" + DEFAULT_TAKEN_BY_ENC_ID, "takenByEncId.equals=" + UPDATED_TAKEN_BY_ENC_ID);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByTakenByEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where takenByEncId in
        defaultCargoRequestFiltering(
            "takenByEncId.in=" + DEFAULT_TAKEN_BY_ENC_ID + "," + UPDATED_TAKEN_BY_ENC_ID,
            "takenByEncId.in=" + UPDATED_TAKEN_BY_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllCargoRequestsByTakenByEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where takenByEncId is not null
        defaultCargoRequestFiltering("takenByEncId.specified=true", "takenByEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestsByEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where encId equals to
        defaultCargoRequestFiltering("encId.equals=" + DEFAULT_ENC_ID, "encId.equals=" + UPDATED_ENC_ID);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where encId in
        defaultCargoRequestFiltering("encId.in=" + DEFAULT_ENC_ID + "," + UPDATED_ENC_ID, "encId.in=" + UPDATED_ENC_ID);
    }

    @Test
    @Transactional
    void getAllCargoRequestsByEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList where encId is not null
        defaultCargoRequestFiltering("encId.specified=true", "encId.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestsByFromCountryIsEqualToSomething() throws Exception {
        Country fromCountry;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            cargoRequestRepository.saveAndFlush(cargoRequest);
            fromCountry = CountryResourceIT.createEntity();
        } else {
            fromCountry = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(fromCountry);
        em.flush();
        cargoRequest.setFromCountry(fromCountry);
        cargoRequestRepository.saveAndFlush(cargoRequest);
        Long fromCountryId = fromCountry.getId();
        // Get all the cargoRequestList where fromCountry equals to fromCountryId
        defaultCargoRequestShouldBeFound("fromCountryId.equals=" + fromCountryId);

        // Get all the cargoRequestList where fromCountry equals to (fromCountryId + 1)
        defaultCargoRequestShouldNotBeFound("fromCountryId.equals=" + (fromCountryId + 1));
    }

    @Test
    @Transactional
    void getAllCargoRequestsByToCountryIsEqualToSomething() throws Exception {
        Country toCountry;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            cargoRequestRepository.saveAndFlush(cargoRequest);
            toCountry = CountryResourceIT.createEntity();
        } else {
            toCountry = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(toCountry);
        em.flush();
        cargoRequest.setToCountry(toCountry);
        cargoRequestRepository.saveAndFlush(cargoRequest);
        Long toCountryId = toCountry.getId();
        // Get all the cargoRequestList where toCountry equals to toCountryId
        defaultCargoRequestShouldBeFound("toCountryId.equals=" + toCountryId);

        // Get all the cargoRequestList where toCountry equals to (toCountryId + 1)
        defaultCargoRequestShouldNotBeFound("toCountryId.equals=" + (toCountryId + 1));
    }

    @Test
    @Transactional
    void getAllCargoRequestsByFromStateIsEqualToSomething() throws Exception {
        StateProvince fromState;
        if (TestUtil.findAll(em, StateProvince.class).isEmpty()) {
            cargoRequestRepository.saveAndFlush(cargoRequest);
            fromState = StateProvinceResourceIT.createEntity();
        } else {
            fromState = TestUtil.findAll(em, StateProvince.class).get(0);
        }
        em.persist(fromState);
        em.flush();
        cargoRequest.setFromState(fromState);
        cargoRequestRepository.saveAndFlush(cargoRequest);
        Long fromStateId = fromState.getId();
        // Get all the cargoRequestList where fromState equals to fromStateId
        defaultCargoRequestShouldBeFound("fromStateId.equals=" + fromStateId);

        // Get all the cargoRequestList where fromState equals to (fromStateId + 1)
        defaultCargoRequestShouldNotBeFound("fromStateId.equals=" + (fromStateId + 1));
    }

    @Test
    @Transactional
    void getAllCargoRequestsByToStateIsEqualToSomething() throws Exception {
        StateProvince toState;
        if (TestUtil.findAll(em, StateProvince.class).isEmpty()) {
            cargoRequestRepository.saveAndFlush(cargoRequest);
            toState = StateProvinceResourceIT.createEntity();
        } else {
            toState = TestUtil.findAll(em, StateProvince.class).get(0);
        }
        em.persist(toState);
        em.flush();
        cargoRequest.setToState(toState);
        cargoRequestRepository.saveAndFlush(cargoRequest);
        Long toStateId = toState.getId();
        // Get all the cargoRequestList where toState equals to toStateId
        defaultCargoRequestShouldBeFound("toStateId.equals=" + toStateId);

        // Get all the cargoRequestList where toState equals to (toStateId + 1)
        defaultCargoRequestShouldNotBeFound("toStateId.equals=" + (toStateId + 1));
    }

    private void defaultCargoRequestFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCargoRequestShouldBeFound(shouldBeFound);
        defaultCargoRequestShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCargoRequestShouldBeFound(String filter) throws Exception {
        restCargoRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isNegotiable").value(hasItem(DEFAULT_IS_NEGOTIABLE)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].createdByEncId").value(hasItem(DEFAULT_CREATED_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].takenByEncId").value(hasItem(DEFAULT_TAKEN_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));

        // Check, that the count call also returns 1
        restCargoRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCargoRequestShouldNotBeFound(String filter) throws Exception {
        restCargoRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCargoRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCargoRequest() throws Exception {
        // Get the cargoRequest
        restCargoRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCargoRequest() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoRequestSearchRepository.save(cargoRequest);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());

        // Update the cargoRequest
        CargoRequest updatedCargoRequest = cargoRequestRepository.findById(cargoRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCargoRequest are not directly saved in db
        em.detach(updatedCargoRequest);
        updatedCargoRequest
            .createDate(UPDATED_CREATE_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .status(UPDATED_STATUS)
            .isNegotiable(UPDATED_IS_NEGOTIABLE)
            .budget(UPDATED_BUDGET)
            .createdByEncId(UPDATED_CREATED_BY_ENC_ID)
            .takenByEncId(UPDATED_TAKEN_BY_ENC_ID)
            .encId(UPDATED_ENC_ID);
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(updatedCargoRequest);

        restCargoRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCargoRequestToMatchAllProperties(updatedCargoRequest);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CargoRequest> cargoRequestSearchList = Streamable.of(cargoRequestSearchRepository.findAll()).toList();
                CargoRequest testCargoRequestSearch = cargoRequestSearchList.get(searchDatabaseSizeAfter - 1);

                assertCargoRequestAllPropertiesEquals(testCargoRequestSearch, updatedCargoRequest);
            });
    }

    @Test
    @Transactional
    void putNonExistingCargoRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        cargoRequest.setId(longCount.incrementAndGet());

        // Create the CargoRequest
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(cargoRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargoRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        cargoRequest.setId(longCount.incrementAndGet());

        // Create the CargoRequest
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(cargoRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargoRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        cargoRequest.setId(longCount.incrementAndGet());

        // Create the CargoRequest
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(cargoRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCargoRequestWithPatch() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoRequest using partial update
        CargoRequest partialUpdatedCargoRequest = new CargoRequest();
        partialUpdatedCargoRequest.setId(cargoRequest.getId());

        partialUpdatedCargoRequest
            .validUntil(UPDATED_VALID_UNTIL)
            .status(UPDATED_STATUS)
            .isNegotiable(UPDATED_IS_NEGOTIABLE)
            .budget(UPDATED_BUDGET)
            .createdByEncId(UPDATED_CREATED_BY_ENC_ID)
            .takenByEncId(UPDATED_TAKEN_BY_ENC_ID);

        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargoRequest))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCargoRequest, cargoRequest),
            getPersistedCargoRequest(cargoRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateCargoRequestWithPatch() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoRequest using partial update
        CargoRequest partialUpdatedCargoRequest = new CargoRequest();
        partialUpdatedCargoRequest.setId(cargoRequest.getId());

        partialUpdatedCargoRequest
            .createDate(UPDATED_CREATE_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .status(UPDATED_STATUS)
            .isNegotiable(UPDATED_IS_NEGOTIABLE)
            .budget(UPDATED_BUDGET)
            .createdByEncId(UPDATED_CREATED_BY_ENC_ID)
            .takenByEncId(UPDATED_TAKEN_BY_ENC_ID)
            .encId(UPDATED_ENC_ID);

        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargoRequest))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoRequestUpdatableFieldsEquals(partialUpdatedCargoRequest, getPersistedCargoRequest(partialUpdatedCargoRequest));
    }

    @Test
    @Transactional
    void patchNonExistingCargoRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        cargoRequest.setId(longCount.incrementAndGet());

        // Create the CargoRequest
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(cargoRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargoRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        cargoRequest.setId(longCount.incrementAndGet());

        // Create the CargoRequest
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(cargoRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargoRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        cargoRequest.setId(longCount.incrementAndGet());

        // Create the CargoRequest
        CargoRequestDTO cargoRequestDTO = cargoRequestMapper.toDto(cargoRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cargoRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCargoRequest() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);
        cargoRequestRepository.save(cargoRequest);
        cargoRequestSearchRepository.save(cargoRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the cargoRequest
        restCargoRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargoRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCargoRequest() throws Exception {
        // Initialize the database
        insertedCargoRequest = cargoRequestRepository.saveAndFlush(cargoRequest);
        cargoRequestSearchRepository.save(cargoRequest);

        // Search the cargoRequest
        restCargoRequestMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cargoRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isNegotiable").value(hasItem(DEFAULT_IS_NEGOTIABLE)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].createdByEncId").value(hasItem(DEFAULT_CREATED_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].takenByEncId").value(hasItem(DEFAULT_TAKEN_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));
    }

    protected long getRepositoryCount() {
        return cargoRequestRepository.count();
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

    protected CargoRequest getPersistedCargoRequest(CargoRequest cargoRequest) {
        return cargoRequestRepository.findById(cargoRequest.getId()).orElseThrow();
    }

    protected void assertPersistedCargoRequestToMatchAllProperties(CargoRequest expectedCargoRequest) {
        assertCargoRequestAllPropertiesEquals(expectedCargoRequest, getPersistedCargoRequest(expectedCargoRequest));
    }

    protected void assertPersistedCargoRequestToMatchUpdatableProperties(CargoRequest expectedCargoRequest) {
        assertCargoRequestAllUpdatablePropertiesEquals(expectedCargoRequest, getPersistedCargoRequest(expectedCargoRequest));
    }
}

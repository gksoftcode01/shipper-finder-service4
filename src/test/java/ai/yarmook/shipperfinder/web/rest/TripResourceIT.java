package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.TripAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.domain.StateProvince;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.enumeration.TripStatus;
import ai.yarmook.shipperfinder.repository.TripRepository;
import ai.yarmook.shipperfinder.repository.search.TripSearchRepository;
import ai.yarmook.shipperfinder.service.TripService;
import ai.yarmook.shipperfinder.service.dto.TripDTO;
import ai.yarmook.shipperfinder.service.mapper.TripMapper;
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
 * Integration tests for the {@link TripResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TripResourceIT {

    private static final Instant DEFAULT_TRIP_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRIP_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARRIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARRIVE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MAX_WEIGHT = 1L;
    private static final Long UPDATED_MAX_WEIGHT = 2L;
    private static final Long SMALLER_MAX_WEIGHT = 1L - 1L;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_NEGOTIATE = false;
    private static final Boolean UPDATED_IS_NEGOTIATE = true;

    private static final TripStatus DEFAULT_STATUS = TripStatus.NEW;
    private static final TripStatus UPDATED_STATUS = TripStatus.PENDING;

    private static final UUID DEFAULT_CREATED_BY_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_CREATED_BY_ENC_ID = UUID.randomUUID();

    private static final UUID DEFAULT_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_ENC_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/trips/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TripRepository tripRepository;

    @Mock
    private TripRepository tripRepositoryMock;

    @Autowired
    private TripMapper tripMapper;

    @Mock
    private TripService tripServiceMock;

    @Autowired
    private TripSearchRepository tripSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMockMvc;

    private Trip trip;

    private Trip insertedTrip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createEntity() {
        return new Trip()
            .tripDate(DEFAULT_TRIP_DATE)
            .arriveDate(DEFAULT_ARRIVE_DATE)
            .maxWeight(DEFAULT_MAX_WEIGHT)
            .notes(DEFAULT_NOTES)
            .createDate(DEFAULT_CREATE_DATE)
            .isNegotiate(DEFAULT_IS_NEGOTIATE)
            .status(DEFAULT_STATUS)
            .createdByEncId(DEFAULT_CREATED_BY_ENC_ID)
            .encId(DEFAULT_ENC_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createUpdatedEntity() {
        return new Trip()
            .tripDate(UPDATED_TRIP_DATE)
            .arriveDate(UPDATED_ARRIVE_DATE)
            .maxWeight(UPDATED_MAX_WEIGHT)
            .notes(UPDATED_NOTES)
            .createDate(UPDATED_CREATE_DATE)
            .isNegotiate(UPDATED_IS_NEGOTIATE)
            .status(UPDATED_STATUS)
            .createdByEncId(UPDATED_CREATED_BY_ENC_ID)
            .encId(UPDATED_ENC_ID);
    }

    @BeforeEach
    public void initTest() {
        trip = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTrip != null) {
            tripRepository.delete(insertedTrip);
            tripSearchRepository.delete(insertedTrip);
            insertedTrip = null;
        }
    }

    @Test
    @Transactional
    void createTrip() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());
        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);
        var returnedTripDTO = om.readValue(
            restTripMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TripDTO.class
        );

        // Validate the Trip in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrip = tripMapper.toEntity(returnedTripDTO);
        assertTripUpdatableFieldsEquals(returnedTrip, getPersistedTrip(returnedTrip));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedTrip = returnedTrip;
    }

    @Test
    @Transactional
    void createTripWithExistingId() throws Exception {
        // Create the Trip with an existing ID
        trip.setId(1L);
        TripDTO tripDTO = tripMapper.toDto(trip);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTrips() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].tripDate").value(hasItem(DEFAULT_TRIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].arriveDate").value(hasItem(DEFAULT_ARRIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxWeight").value(hasItem(DEFAULT_MAX_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isNegotiate").value(hasItem(DEFAULT_IS_NEGOTIATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdByEncId").value(hasItem(DEFAULT_CREATED_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTripsWithEagerRelationshipsIsEnabled() throws Exception {
        when(tripServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTripMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tripServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTripsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tripServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTripMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tripRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrip() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc
            .perform(get(ENTITY_API_URL_ID, trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.tripDate").value(DEFAULT_TRIP_DATE.toString()))
            .andExpect(jsonPath("$.arriveDate").value(DEFAULT_ARRIVE_DATE.toString()))
            .andExpect(jsonPath("$.maxWeight").value(DEFAULT_MAX_WEIGHT.intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.isNegotiate").value(DEFAULT_IS_NEGOTIATE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdByEncId").value(DEFAULT_CREATED_BY_ENC_ID.toString()))
            .andExpect(jsonPath("$.encId").value(DEFAULT_ENC_ID.toString()));
    }

    @Test
    @Transactional
    void getTripsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        Long id = trip.getId();

        defaultTripFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTripFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTripFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTripsByTripDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where tripDate equals to
        defaultTripFiltering("tripDate.equals=" + DEFAULT_TRIP_DATE, "tripDate.equals=" + UPDATED_TRIP_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByTripDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where tripDate in
        defaultTripFiltering("tripDate.in=" + DEFAULT_TRIP_DATE + "," + UPDATED_TRIP_DATE, "tripDate.in=" + UPDATED_TRIP_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByTripDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where tripDate is not null
        defaultTripFiltering("tripDate.specified=true", "tripDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByArriveDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where arriveDate equals to
        defaultTripFiltering("arriveDate.equals=" + DEFAULT_ARRIVE_DATE, "arriveDate.equals=" + UPDATED_ARRIVE_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByArriveDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where arriveDate in
        defaultTripFiltering("arriveDate.in=" + DEFAULT_ARRIVE_DATE + "," + UPDATED_ARRIVE_DATE, "arriveDate.in=" + UPDATED_ARRIVE_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByArriveDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where arriveDate is not null
        defaultTripFiltering("arriveDate.specified=true", "arriveDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByMaxWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where maxWeight equals to
        defaultTripFiltering("maxWeight.equals=" + DEFAULT_MAX_WEIGHT, "maxWeight.equals=" + UPDATED_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTripsByMaxWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where maxWeight in
        defaultTripFiltering("maxWeight.in=" + DEFAULT_MAX_WEIGHT + "," + UPDATED_MAX_WEIGHT, "maxWeight.in=" + UPDATED_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTripsByMaxWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where maxWeight is not null
        defaultTripFiltering("maxWeight.specified=true", "maxWeight.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByMaxWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where maxWeight is greater than or equal to
        defaultTripFiltering("maxWeight.greaterThanOrEqual=" + DEFAULT_MAX_WEIGHT, "maxWeight.greaterThanOrEqual=" + UPDATED_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTripsByMaxWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where maxWeight is less than or equal to
        defaultTripFiltering("maxWeight.lessThanOrEqual=" + DEFAULT_MAX_WEIGHT, "maxWeight.lessThanOrEqual=" + SMALLER_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTripsByMaxWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where maxWeight is less than
        defaultTripFiltering("maxWeight.lessThan=" + UPDATED_MAX_WEIGHT, "maxWeight.lessThan=" + DEFAULT_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTripsByMaxWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where maxWeight is greater than
        defaultTripFiltering("maxWeight.greaterThan=" + SMALLER_MAX_WEIGHT, "maxWeight.greaterThan=" + DEFAULT_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTripsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where notes equals to
        defaultTripFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllTripsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where notes in
        defaultTripFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllTripsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where notes is not null
        defaultTripFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where notes contains
        defaultTripFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllTripsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where notes does not contain
        defaultTripFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllTripsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where createDate equals to
        defaultTripFiltering("createDate.equals=" + DEFAULT_CREATE_DATE, "createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where createDate in
        defaultTripFiltering("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE, "createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where createDate is not null
        defaultTripFiltering("createDate.specified=true", "createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByIsNegotiateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where isNegotiate equals to
        defaultTripFiltering("isNegotiate.equals=" + DEFAULT_IS_NEGOTIATE, "isNegotiate.equals=" + UPDATED_IS_NEGOTIATE);
    }

    @Test
    @Transactional
    void getAllTripsByIsNegotiateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where isNegotiate in
        defaultTripFiltering(
            "isNegotiate.in=" + DEFAULT_IS_NEGOTIATE + "," + UPDATED_IS_NEGOTIATE,
            "isNegotiate.in=" + UPDATED_IS_NEGOTIATE
        );
    }

    @Test
    @Transactional
    void getAllTripsByIsNegotiateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where isNegotiate is not null
        defaultTripFiltering("isNegotiate.specified=true", "isNegotiate.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where status equals to
        defaultTripFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTripsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where status in
        defaultTripFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTripsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where status is not null
        defaultTripFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByCreatedByEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdByEncId equals to
        defaultTripFiltering("createdByEncId.equals=" + DEFAULT_CREATED_BY_ENC_ID, "createdByEncId.equals=" + UPDATED_CREATED_BY_ENC_ID);
    }

    @Test
    @Transactional
    void getAllTripsByCreatedByEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdByEncId in
        defaultTripFiltering(
            "createdByEncId.in=" + DEFAULT_CREATED_BY_ENC_ID + "," + UPDATED_CREATED_BY_ENC_ID,
            "createdByEncId.in=" + UPDATED_CREATED_BY_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllTripsByCreatedByEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdByEncId is not null
        defaultTripFiltering("createdByEncId.specified=true", "createdByEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where encId equals to
        defaultTripFiltering("encId.equals=" + DEFAULT_ENC_ID, "encId.equals=" + UPDATED_ENC_ID);
    }

    @Test
    @Transactional
    void getAllTripsByEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where encId in
        defaultTripFiltering("encId.in=" + DEFAULT_ENC_ID + "," + UPDATED_ENC_ID, "encId.in=" + UPDATED_ENC_ID);
    }

    @Test
    @Transactional
    void getAllTripsByEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList where encId is not null
        defaultTripFiltering("encId.specified=true", "encId.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByFromCountryIsEqualToSomething() throws Exception {
        Country fromCountry;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            tripRepository.saveAndFlush(trip);
            fromCountry = CountryResourceIT.createEntity();
        } else {
            fromCountry = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(fromCountry);
        em.flush();
        trip.setFromCountry(fromCountry);
        tripRepository.saveAndFlush(trip);
        Long fromCountryId = fromCountry.getId();
        // Get all the tripList where fromCountry equals to fromCountryId
        defaultTripShouldBeFound("fromCountryId.equals=" + fromCountryId);

        // Get all the tripList where fromCountry equals to (fromCountryId + 1)
        defaultTripShouldNotBeFound("fromCountryId.equals=" + (fromCountryId + 1));
    }

    @Test
    @Transactional
    void getAllTripsByToCountryIsEqualToSomething() throws Exception {
        Country toCountry;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            tripRepository.saveAndFlush(trip);
            toCountry = CountryResourceIT.createEntity();
        } else {
            toCountry = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(toCountry);
        em.flush();
        trip.setToCountry(toCountry);
        tripRepository.saveAndFlush(trip);
        Long toCountryId = toCountry.getId();
        // Get all the tripList where toCountry equals to toCountryId
        defaultTripShouldBeFound("toCountryId.equals=" + toCountryId);

        // Get all the tripList where toCountry equals to (toCountryId + 1)
        defaultTripShouldNotBeFound("toCountryId.equals=" + (toCountryId + 1));
    }

    @Test
    @Transactional
    void getAllTripsByFromStateIsEqualToSomething() throws Exception {
        StateProvince fromState;
        if (TestUtil.findAll(em, StateProvince.class).isEmpty()) {
            tripRepository.saveAndFlush(trip);
            fromState = StateProvinceResourceIT.createEntity();
        } else {
            fromState = TestUtil.findAll(em, StateProvince.class).get(0);
        }
        em.persist(fromState);
        em.flush();
        trip.setFromState(fromState);
        tripRepository.saveAndFlush(trip);
        Long fromStateId = fromState.getId();
        // Get all the tripList where fromState equals to fromStateId
        defaultTripShouldBeFound("fromStateId.equals=" + fromStateId);

        // Get all the tripList where fromState equals to (fromStateId + 1)
        defaultTripShouldNotBeFound("fromStateId.equals=" + (fromStateId + 1));
    }

    @Test
    @Transactional
    void getAllTripsByToStateIsEqualToSomething() throws Exception {
        StateProvince toState;
        if (TestUtil.findAll(em, StateProvince.class).isEmpty()) {
            tripRepository.saveAndFlush(trip);
            toState = StateProvinceResourceIT.createEntity();
        } else {
            toState = TestUtil.findAll(em, StateProvince.class).get(0);
        }
        em.persist(toState);
        em.flush();
        trip.setToState(toState);
        tripRepository.saveAndFlush(trip);
        Long toStateId = toState.getId();
        // Get all the tripList where toState equals to toStateId
        defaultTripShouldBeFound("toStateId.equals=" + toStateId);

        // Get all the tripList where toState equals to (toStateId + 1)
        defaultTripShouldNotBeFound("toStateId.equals=" + (toStateId + 1));
    }

    private void defaultTripFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTripShouldBeFound(shouldBeFound);
        defaultTripShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTripShouldBeFound(String filter) throws Exception {
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].tripDate").value(hasItem(DEFAULT_TRIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].arriveDate").value(hasItem(DEFAULT_ARRIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxWeight").value(hasItem(DEFAULT_MAX_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isNegotiate").value(hasItem(DEFAULT_IS_NEGOTIATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdByEncId").value(hasItem(DEFAULT_CREATED_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));

        // Check, that the count call also returns 1
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTripShouldNotBeFound(String filter) throws Exception {
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrip() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripSearchRepository.save(trip);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());

        // Update the trip
        Trip updatedTrip = tripRepository.findById(trip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrip are not directly saved in db
        em.detach(updatedTrip);
        updatedTrip
            .tripDate(UPDATED_TRIP_DATE)
            .arriveDate(UPDATED_ARRIVE_DATE)
            .maxWeight(UPDATED_MAX_WEIGHT)
            .notes(UPDATED_NOTES)
            .createDate(UPDATED_CREATE_DATE)
            .isNegotiate(UPDATED_IS_NEGOTIATE)
            .status(UPDATED_STATUS)
            .createdByEncId(UPDATED_CREATED_BY_ENC_ID)
            .encId(UPDATED_ENC_ID);
        TripDTO tripDTO = tripMapper.toDto(updatedTrip);

        restTripMockMvc
            .perform(put(ENTITY_API_URL_ID, tripDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isOk());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTripToMatchAllProperties(updatedTrip);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Trip> tripSearchList = Streamable.of(tripSearchRepository.findAll()).toList();
                Trip testTripSearch = tripSearchList.get(searchDatabaseSizeAfter - 1);

                assertTripAllPropertiesEquals(testTripSearch, updatedTrip);
            });
    }

    @Test
    @Transactional
    void putNonExistingTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL_ID, tripDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTripWithPatch() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip
            .tripDate(UPDATED_TRIP_DATE)
            .maxWeight(UPDATED_MAX_WEIGHT)
            .isNegotiate(UPDATED_IS_NEGOTIATE)
            .status(UPDATED_STATUS);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTrip, trip), getPersistedTrip(trip));
    }

    @Test
    @Transactional
    void fullUpdateTripWithPatch() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip
            .tripDate(UPDATED_TRIP_DATE)
            .arriveDate(UPDATED_ARRIVE_DATE)
            .maxWeight(UPDATED_MAX_WEIGHT)
            .notes(UPDATED_NOTES)
            .createDate(UPDATED_CREATE_DATE)
            .isNegotiate(UPDATED_IS_NEGOTIATE)
            .status(UPDATED_STATUS)
            .createdByEncId(UPDATED_CREATED_BY_ENC_ID)
            .encId(UPDATED_ENC_ID);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripUpdatableFieldsEquals(partialUpdatedTrip, getPersistedTrip(partialUpdatedTrip));
    }

    @Test
    @Transactional
    void patchNonExistingTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTrip() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);
        tripRepository.save(trip);
        tripSearchRepository.save(trip);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the trip
        restTripMockMvc
            .perform(delete(ENTITY_API_URL_ID, trip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTrip() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);
        tripSearchRepository.save(trip);

        // Search the trip
        restTripMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].tripDate").value(hasItem(DEFAULT_TRIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].arriveDate").value(hasItem(DEFAULT_ARRIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxWeight").value(hasItem(DEFAULT_MAX_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isNegotiate").value(hasItem(DEFAULT_IS_NEGOTIATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdByEncId").value(hasItem(DEFAULT_CREATED_BY_ENC_ID.toString())))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));
    }

    protected long getRepositoryCount() {
        return tripRepository.count();
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

    protected Trip getPersistedTrip(Trip trip) {
        return tripRepository.findById(trip.getId()).orElseThrow();
    }

    protected void assertPersistedTripToMatchAllProperties(Trip expectedTrip) {
        assertTripAllPropertiesEquals(expectedTrip, getPersistedTrip(expectedTrip));
    }

    protected void assertPersistedTripToMatchUpdatableProperties(Trip expectedTrip) {
        assertTripAllUpdatablePropertiesEquals(expectedTrip, getPersistedTrip(expectedTrip));
    }
}

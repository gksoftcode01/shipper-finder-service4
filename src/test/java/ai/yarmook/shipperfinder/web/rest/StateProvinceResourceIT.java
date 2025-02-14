package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.StateProvinceAsserts.*;
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
import ai.yarmook.shipperfinder.repository.StateProvinceRepository;
import ai.yarmook.shipperfinder.repository.search.StateProvinceSearchRepository;
import ai.yarmook.shipperfinder.service.StateProvinceService;
import ai.yarmook.shipperfinder.service.dto.StateProvinceDTO;
import ai.yarmook.shipperfinder.service.mapper.StateProvinceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link StateProvinceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StateProvinceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ISO_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/state-provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/state-provinces/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StateProvinceRepository stateProvinceRepository;

    @Mock
    private StateProvinceRepository stateProvinceRepositoryMock;

    @Autowired
    private StateProvinceMapper stateProvinceMapper;

    @Mock
    private StateProvinceService stateProvinceServiceMock;

    @Autowired
    private StateProvinceSearchRepository stateProvinceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStateProvinceMockMvc;

    private StateProvince stateProvince;

    private StateProvince insertedStateProvince;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateProvince createEntity() {
        return new StateProvince().name(DEFAULT_NAME).localName(DEFAULT_LOCAL_NAME).isoCode(DEFAULT_ISO_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateProvince createUpdatedEntity() {
        return new StateProvince().name(UPDATED_NAME).localName(UPDATED_LOCAL_NAME).isoCode(UPDATED_ISO_CODE);
    }

    @BeforeEach
    public void initTest() {
        stateProvince = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStateProvince != null) {
            stateProvinceRepository.delete(insertedStateProvince);
            stateProvinceSearchRepository.delete(insertedStateProvince);
            insertedStateProvince = null;
        }
    }

    @Test
    @Transactional
    void createStateProvince() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        // Create the StateProvince
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(stateProvince);
        var returnedStateProvinceDTO = om.readValue(
            restStateProvinceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateProvinceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StateProvinceDTO.class
        );

        // Validate the StateProvince in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStateProvince = stateProvinceMapper.toEntity(returnedStateProvinceDTO);
        assertStateProvinceUpdatableFieldsEquals(returnedStateProvince, getPersistedStateProvince(returnedStateProvince));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedStateProvince = returnedStateProvince;
    }

    @Test
    @Transactional
    void createStateProvinceWithExistingId() throws Exception {
        // Create the StateProvince with an existing ID
        stateProvince.setId(1L);
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(stateProvince);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateProvinceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllStateProvinces() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList
        restStateProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateProvince.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].localName").value(hasItem(DEFAULT_LOCAL_NAME)))
            .andExpect(jsonPath("$.[*].isoCode").value(hasItem(DEFAULT_ISO_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStateProvincesWithEagerRelationshipsIsEnabled() throws Exception {
        when(stateProvinceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStateProvinceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(stateProvinceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStateProvincesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(stateProvinceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStateProvinceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(stateProvinceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStateProvince() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get the stateProvince
        restStateProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, stateProvince.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stateProvince.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.localName").value(DEFAULT_LOCAL_NAME))
            .andExpect(jsonPath("$.isoCode").value(DEFAULT_ISO_CODE));
    }

    @Test
    @Transactional
    void getStateProvincesByIdFiltering() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        Long id = stateProvince.getId();

        defaultStateProvinceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStateProvinceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStateProvinceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStateProvincesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where name equals to
        defaultStateProvinceFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStateProvincesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where name in
        defaultStateProvinceFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStateProvincesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where name is not null
        defaultStateProvinceFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllStateProvincesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where name contains
        defaultStateProvinceFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStateProvincesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where name does not contain
        defaultStateProvinceFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllStateProvincesByLocalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where localName equals to
        defaultStateProvinceFiltering("localName.equals=" + DEFAULT_LOCAL_NAME, "localName.equals=" + UPDATED_LOCAL_NAME);
    }

    @Test
    @Transactional
    void getAllStateProvincesByLocalNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where localName in
        defaultStateProvinceFiltering(
            "localName.in=" + DEFAULT_LOCAL_NAME + "," + UPDATED_LOCAL_NAME,
            "localName.in=" + UPDATED_LOCAL_NAME
        );
    }

    @Test
    @Transactional
    void getAllStateProvincesByLocalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where localName is not null
        defaultStateProvinceFiltering("localName.specified=true", "localName.specified=false");
    }

    @Test
    @Transactional
    void getAllStateProvincesByLocalNameContainsSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where localName contains
        defaultStateProvinceFiltering("localName.contains=" + DEFAULT_LOCAL_NAME, "localName.contains=" + UPDATED_LOCAL_NAME);
    }

    @Test
    @Transactional
    void getAllStateProvincesByLocalNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where localName does not contain
        defaultStateProvinceFiltering("localName.doesNotContain=" + UPDATED_LOCAL_NAME, "localName.doesNotContain=" + DEFAULT_LOCAL_NAME);
    }

    @Test
    @Transactional
    void getAllStateProvincesByIsoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where isoCode equals to
        defaultStateProvinceFiltering("isoCode.equals=" + DEFAULT_ISO_CODE, "isoCode.equals=" + UPDATED_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllStateProvincesByIsoCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where isoCode in
        defaultStateProvinceFiltering("isoCode.in=" + DEFAULT_ISO_CODE + "," + UPDATED_ISO_CODE, "isoCode.in=" + UPDATED_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllStateProvincesByIsoCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where isoCode is not null
        defaultStateProvinceFiltering("isoCode.specified=true", "isoCode.specified=false");
    }

    @Test
    @Transactional
    void getAllStateProvincesByIsoCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where isoCode contains
        defaultStateProvinceFiltering("isoCode.contains=" + DEFAULT_ISO_CODE, "isoCode.contains=" + UPDATED_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllStateProvincesByIsoCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList where isoCode does not contain
        defaultStateProvinceFiltering("isoCode.doesNotContain=" + UPDATED_ISO_CODE, "isoCode.doesNotContain=" + DEFAULT_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllStateProvincesByCountryIsEqualToSomething() throws Exception {
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            stateProvinceRepository.saveAndFlush(stateProvince);
            country = CountryResourceIT.createEntity();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(country);
        em.flush();
        stateProvince.setCountry(country);
        stateProvinceRepository.saveAndFlush(stateProvince);
        Long countryId = country.getId();
        // Get all the stateProvinceList where country equals to countryId
        defaultStateProvinceShouldBeFound("countryId.equals=" + countryId);

        // Get all the stateProvinceList where country equals to (countryId + 1)
        defaultStateProvinceShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    private void defaultStateProvinceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStateProvinceShouldBeFound(shouldBeFound);
        defaultStateProvinceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStateProvinceShouldBeFound(String filter) throws Exception {
        restStateProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateProvince.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].localName").value(hasItem(DEFAULT_LOCAL_NAME)))
            .andExpect(jsonPath("$.[*].isoCode").value(hasItem(DEFAULT_ISO_CODE)));

        // Check, that the count call also returns 1
        restStateProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStateProvinceShouldNotBeFound(String filter) throws Exception {
        restStateProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStateProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStateProvince() throws Exception {
        // Get the stateProvince
        restStateProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStateProvince() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateProvinceSearchRepository.save(stateProvince);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());

        // Update the stateProvince
        StateProvince updatedStateProvince = stateProvinceRepository.findById(stateProvince.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStateProvince are not directly saved in db
        em.detach(updatedStateProvince);
        updatedStateProvince.name(UPDATED_NAME).localName(UPDATED_LOCAL_NAME).isoCode(UPDATED_ISO_CODE);
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(updatedStateProvince);

        restStateProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateProvinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateProvinceDTO))
            )
            .andExpect(status().isOk());

        // Validate the StateProvince in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStateProvinceToMatchAllProperties(updatedStateProvince);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<StateProvince> stateProvinceSearchList = Streamable.of(stateProvinceSearchRepository.findAll()).toList();
                StateProvince testStateProvinceSearch = stateProvinceSearchList.get(searchDatabaseSizeAfter - 1);

                assertStateProvinceAllPropertiesEquals(testStateProvinceSearch, updatedStateProvince);
            });
    }

    @Test
    @Transactional
    void putNonExistingStateProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        stateProvince.setId(longCount.incrementAndGet());

        // Create the StateProvince
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(stateProvince);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateProvinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchStateProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        stateProvince.setId(longCount.incrementAndGet());

        // Create the StateProvince
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(stateProvince);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStateProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        stateProvince.setId(longCount.incrementAndGet());

        // Create the StateProvince
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(stateProvince);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateProvinceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateProvince in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateStateProvinceWithPatch() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateProvince using partial update
        StateProvince partialUpdatedStateProvince = new StateProvince();
        partialUpdatedStateProvince.setId(stateProvince.getId());

        partialUpdatedStateProvince.name(UPDATED_NAME).localName(UPDATED_LOCAL_NAME).isoCode(UPDATED_ISO_CODE);

        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStateProvince))
            )
            .andExpect(status().isOk());

        // Validate the StateProvince in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStateProvinceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStateProvince, stateProvince),
            getPersistedStateProvince(stateProvince)
        );
    }

    @Test
    @Transactional
    void fullUpdateStateProvinceWithPatch() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateProvince using partial update
        StateProvince partialUpdatedStateProvince = new StateProvince();
        partialUpdatedStateProvince.setId(stateProvince.getId());

        partialUpdatedStateProvince.name(UPDATED_NAME).localName(UPDATED_LOCAL_NAME).isoCode(UPDATED_ISO_CODE);

        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStateProvince))
            )
            .andExpect(status().isOk());

        // Validate the StateProvince in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStateProvinceUpdatableFieldsEquals(partialUpdatedStateProvince, getPersistedStateProvince(partialUpdatedStateProvince));
    }

    @Test
    @Transactional
    void patchNonExistingStateProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        stateProvince.setId(longCount.incrementAndGet());

        // Create the StateProvince
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(stateProvince);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stateProvinceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stateProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStateProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        stateProvince.setId(longCount.incrementAndGet());

        // Create the StateProvince
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(stateProvince);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stateProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStateProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        stateProvince.setId(longCount.incrementAndGet());

        // Create the StateProvince
        StateProvinceDTO stateProvinceDTO = stateProvinceMapper.toDto(stateProvince);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stateProvinceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateProvince in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteStateProvince() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);
        stateProvinceRepository.save(stateProvince);
        stateProvinceSearchRepository.save(stateProvince);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the stateProvince
        restStateProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, stateProvince.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateProvinceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchStateProvince() throws Exception {
        // Initialize the database
        insertedStateProvince = stateProvinceRepository.saveAndFlush(stateProvince);
        stateProvinceSearchRepository.save(stateProvince);

        // Search the stateProvince
        restStateProvinceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + stateProvince.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateProvince.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].localName").value(hasItem(DEFAULT_LOCAL_NAME)))
            .andExpect(jsonPath("$.[*].isoCode").value(hasItem(DEFAULT_ISO_CODE)));
    }

    protected long getRepositoryCount() {
        return stateProvinceRepository.count();
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

    protected StateProvince getPersistedStateProvince(StateProvince stateProvince) {
        return stateProvinceRepository.findById(stateProvince.getId()).orElseThrow();
    }

    protected void assertPersistedStateProvinceToMatchAllProperties(StateProvince expectedStateProvince) {
        assertStateProvinceAllPropertiesEquals(expectedStateProvince, getPersistedStateProvince(expectedStateProvince));
    }

    protected void assertPersistedStateProvinceToMatchUpdatableProperties(StateProvince expectedStateProvince) {
        assertStateProvinceAllUpdatablePropertiesEquals(expectedStateProvince, getPersistedStateProvince(expectedStateProvince));
    }
}

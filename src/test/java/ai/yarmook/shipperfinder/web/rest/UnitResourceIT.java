package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.UnitAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.Unit;
import ai.yarmook.shipperfinder.repository.UnitRepository;
import ai.yarmook.shipperfinder.repository.search.UnitSearchRepository;
import ai.yarmook.shipperfinder.service.dto.UnitDTO;
import ai.yarmook.shipperfinder.service.mapper.UnitMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link UnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_FR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_DE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_URDU = "AAAAAAAAAA";
    private static final String UPDATED_NAME_URDU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/units/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    private UnitSearchRepository unitSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitMockMvc;

    private Unit unit;

    private Unit insertedUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unit createEntity() {
        return new Unit()
            .name(DEFAULT_NAME)
            .nameEn(DEFAULT_NAME_EN)
            .nameAr(DEFAULT_NAME_AR)
            .nameFr(DEFAULT_NAME_FR)
            .nameDe(DEFAULT_NAME_DE)
            .nameUrdu(DEFAULT_NAME_URDU);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unit createUpdatedEntity() {
        return new Unit()
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU);
    }

    @BeforeEach
    public void initTest() {
        unit = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUnit != null) {
            unitRepository.delete(insertedUnit);
            unitSearchRepository.delete(insertedUnit);
            insertedUnit = null;
        }
    }

    @Test
    @Transactional
    void createUnit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());
        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);
        var returnedUnitDTO = om.readValue(
            restUnitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UnitDTO.class
        );

        // Validate the Unit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUnit = unitMapper.toEntity(returnedUnitDTO);
        assertUnitUpdatableFieldsEquals(returnedUnit, getPersistedUnit(returnedUnit));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedUnit = returnedUnit;
    }

    @Test
    @Transactional
    void createUnitWithExistingId() throws Exception {
        // Create the Unit with an existing ID
        unit.setId(1L);
        UnitDTO unitDTO = unitMapper.toDto(unit);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUnits() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList
        restUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)));
    }

    @Test
    @Transactional
    void getUnit() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get the unit
        restUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, unit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameFr").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.nameDe").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.nameUrdu").value(DEFAULT_NAME_URDU));
    }

    @Test
    @Transactional
    void getUnitsByIdFiltering() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        Long id = unit.getId();

        defaultUnitFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUnitFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUnitFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUnitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where name equals to
        defaultUnitFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUnitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where name in
        defaultUnitFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUnitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where name is not null
        defaultUnitFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUnitsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where name contains
        defaultUnitFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUnitsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where name does not contain
        defaultUnitFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUnitsByNameEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameEn equals to
        defaultUnitFiltering("nameEn.equals=" + DEFAULT_NAME_EN, "nameEn.equals=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllUnitsByNameEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameEn in
        defaultUnitFiltering("nameEn.in=" + DEFAULT_NAME_EN + "," + UPDATED_NAME_EN, "nameEn.in=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllUnitsByNameEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameEn is not null
        defaultUnitFiltering("nameEn.specified=true", "nameEn.specified=false");
    }

    @Test
    @Transactional
    void getAllUnitsByNameEnContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameEn contains
        defaultUnitFiltering("nameEn.contains=" + DEFAULT_NAME_EN, "nameEn.contains=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllUnitsByNameEnNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameEn does not contain
        defaultUnitFiltering("nameEn.doesNotContain=" + UPDATED_NAME_EN, "nameEn.doesNotContain=" + DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void getAllUnitsByNameArIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameAr equals to
        defaultUnitFiltering("nameAr.equals=" + DEFAULT_NAME_AR, "nameAr.equals=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllUnitsByNameArIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameAr in
        defaultUnitFiltering("nameAr.in=" + DEFAULT_NAME_AR + "," + UPDATED_NAME_AR, "nameAr.in=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllUnitsByNameArIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameAr is not null
        defaultUnitFiltering("nameAr.specified=true", "nameAr.specified=false");
    }

    @Test
    @Transactional
    void getAllUnitsByNameArContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameAr contains
        defaultUnitFiltering("nameAr.contains=" + DEFAULT_NAME_AR, "nameAr.contains=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllUnitsByNameArNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameAr does not contain
        defaultUnitFiltering("nameAr.doesNotContain=" + UPDATED_NAME_AR, "nameAr.doesNotContain=" + DEFAULT_NAME_AR);
    }

    @Test
    @Transactional
    void getAllUnitsByNameFrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameFr equals to
        defaultUnitFiltering("nameFr.equals=" + DEFAULT_NAME_FR, "nameFr.equals=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllUnitsByNameFrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameFr in
        defaultUnitFiltering("nameFr.in=" + DEFAULT_NAME_FR + "," + UPDATED_NAME_FR, "nameFr.in=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllUnitsByNameFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameFr is not null
        defaultUnitFiltering("nameFr.specified=true", "nameFr.specified=false");
    }

    @Test
    @Transactional
    void getAllUnitsByNameFrContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameFr contains
        defaultUnitFiltering("nameFr.contains=" + DEFAULT_NAME_FR, "nameFr.contains=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllUnitsByNameFrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameFr does not contain
        defaultUnitFiltering("nameFr.doesNotContain=" + UPDATED_NAME_FR, "nameFr.doesNotContain=" + DEFAULT_NAME_FR);
    }

    @Test
    @Transactional
    void getAllUnitsByNameDeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameDe equals to
        defaultUnitFiltering("nameDe.equals=" + DEFAULT_NAME_DE, "nameDe.equals=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllUnitsByNameDeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameDe in
        defaultUnitFiltering("nameDe.in=" + DEFAULT_NAME_DE + "," + UPDATED_NAME_DE, "nameDe.in=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllUnitsByNameDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameDe is not null
        defaultUnitFiltering("nameDe.specified=true", "nameDe.specified=false");
    }

    @Test
    @Transactional
    void getAllUnitsByNameDeContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameDe contains
        defaultUnitFiltering("nameDe.contains=" + DEFAULT_NAME_DE, "nameDe.contains=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllUnitsByNameDeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameDe does not contain
        defaultUnitFiltering("nameDe.doesNotContain=" + UPDATED_NAME_DE, "nameDe.doesNotContain=" + DEFAULT_NAME_DE);
    }

    @Test
    @Transactional
    void getAllUnitsByNameUrduIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameUrdu equals to
        defaultUnitFiltering("nameUrdu.equals=" + DEFAULT_NAME_URDU, "nameUrdu.equals=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllUnitsByNameUrduIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameUrdu in
        defaultUnitFiltering("nameUrdu.in=" + DEFAULT_NAME_URDU + "," + UPDATED_NAME_URDU, "nameUrdu.in=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllUnitsByNameUrduIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameUrdu is not null
        defaultUnitFiltering("nameUrdu.specified=true", "nameUrdu.specified=false");
    }

    @Test
    @Transactional
    void getAllUnitsByNameUrduContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameUrdu contains
        defaultUnitFiltering("nameUrdu.contains=" + DEFAULT_NAME_URDU, "nameUrdu.contains=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllUnitsByNameUrduNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        // Get all the unitList where nameUrdu does not contain
        defaultUnitFiltering("nameUrdu.doesNotContain=" + UPDATED_NAME_URDU, "nameUrdu.doesNotContain=" + DEFAULT_NAME_URDU);
    }

    private void defaultUnitFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUnitShouldBeFound(shouldBeFound);
        defaultUnitShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUnitShouldBeFound(String filter) throws Exception {
        restUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)));

        // Check, that the count call also returns 1
        restUnitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUnitShouldNotBeFound(String filter) throws Exception {
        restUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUnitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUnit() throws Exception {
        // Get the unit
        restUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnit() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitSearchRepository.save(unit);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());

        // Update the unit
        Unit updatedUnit = unitRepository.findById(unit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUnit are not directly saved in db
        em.detach(updatedUnit);
        updatedUnit
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU);
        UnitDTO unitDTO = unitMapper.toDto(updatedUnit);

        restUnitMockMvc
            .perform(put(ENTITY_API_URL_ID, unitDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitDTO)))
            .andExpect(status().isOk());

        // Validate the Unit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUnitToMatchAllProperties(updatedUnit);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Unit> unitSearchList = Streamable.of(unitSearchRepository.findAll()).toList();
                Unit testUnitSearch = unitSearchList.get(searchDatabaseSizeAfter - 1);

                assertUnitAllPropertiesEquals(testUnitSearch, updatedUnit);
            });
    }

    @Test
    @Transactional
    void putNonExistingUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());
        unit.setId(longCount.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(put(ENTITY_API_URL_ID, unitDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());
        unit.setId(longCount.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(unitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());
        unit.setId(longCount.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Unit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUnitWithPatch() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unit using partial update
        Unit partialUpdatedUnit = new Unit();
        partialUpdatedUnit.setId(unit.getId());

        partialUpdatedUnit.name(UPDATED_NAME).nameEn(UPDATED_NAME_EN).nameFr(UPDATED_NAME_FR).nameDe(UPDATED_NAME_DE);

        restUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnit))
            )
            .andExpect(status().isOk());

        // Validate the Unit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUnitUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUnit, unit), getPersistedUnit(unit));
    }

    @Test
    @Transactional
    void fullUpdateUnitWithPatch() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unit using partial update
        Unit partialUpdatedUnit = new Unit();
        partialUpdatedUnit.setId(unit.getId());

        partialUpdatedUnit
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU);

        restUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnit))
            )
            .andExpect(status().isOk());

        // Validate the Unit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUnitUpdatableFieldsEquals(partialUpdatedUnit, getPersistedUnit(partialUpdatedUnit));
    }

    @Test
    @Transactional
    void patchNonExistingUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());
        unit.setId(longCount.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(unitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());
        unit.setId(longCount.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(unitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());
        unit.setId(longCount.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(unitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Unit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUnit() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);
        unitRepository.save(unit);
        unitSearchRepository.save(unit);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the unit
        restUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, unit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(unitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUnit() throws Exception {
        // Initialize the database
        insertedUnit = unitRepository.saveAndFlush(unit);
        unitSearchRepository.save(unit);

        // Search the unit
        restUnitMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + unit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)));
    }

    protected long getRepositoryCount() {
        return unitRepository.count();
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

    protected Unit getPersistedUnit(Unit unit) {
        return unitRepository.findById(unit.getId()).orElseThrow();
    }

    protected void assertPersistedUnitToMatchAllProperties(Unit expectedUnit) {
        assertUnitAllPropertiesEquals(expectedUnit, getPersistedUnit(expectedUnit));
    }

    protected void assertPersistedUnitToMatchUpdatableProperties(Unit expectedUnit) {
        assertUnitAllUpdatablePropertiesEquals(expectedUnit, getPersistedUnit(expectedUnit));
    }
}

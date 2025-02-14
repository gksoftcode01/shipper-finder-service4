package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.ItemTypeAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.ItemType;
import ai.yarmook.shipperfinder.repository.ItemTypeRepository;
import ai.yarmook.shipperfinder.repository.search.ItemTypeSearchRepository;
import ai.yarmook.shipperfinder.service.dto.ItemTypeDTO;
import ai.yarmook.shipperfinder.service.mapper.ItemTypeMapper;
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
 * Integration tests for the {@link ItemTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemTypeResourceIT {

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

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/item-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/item-types/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemTypeMapper itemTypeMapper;

    @Autowired
    private ItemTypeSearchRepository itemTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemTypeMockMvc;

    private ItemType itemType;

    private ItemType insertedItemType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemType createEntity() {
        return new ItemType()
            .name(DEFAULT_NAME)
            .nameEn(DEFAULT_NAME_EN)
            .nameAr(DEFAULT_NAME_AR)
            .nameFr(DEFAULT_NAME_FR)
            .nameDe(DEFAULT_NAME_DE)
            .nameUrdu(DEFAULT_NAME_URDU)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemType createUpdatedEntity() {
        return new ItemType()
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        itemType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedItemType != null) {
            itemTypeRepository.delete(insertedItemType);
            itemTypeSearchRepository.delete(insertedItemType);
            insertedItemType = null;
        }
    }

    @Test
    @Transactional
    void createItemType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);
        var returnedItemTypeDTO = om.readValue(
            restItemTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ItemTypeDTO.class
        );

        // Validate the ItemType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedItemType = itemTypeMapper.toEntity(returnedItemTypeDTO);
        assertItemTypeUpdatableFieldsEquals(returnedItemType, getPersistedItemType(returnedItemType));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedItemType = returnedItemType;
    }

    @Test
    @Transactional
    void createItemTypeWithExistingId() throws Exception {
        // Create the ItemType with an existing ID
        itemType.setId(1L);
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        // set the field null
        itemType.setName(null);

        // Create the ItemType, which fails.
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        restItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllItemTypes() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList
        restItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getItemType() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get the itemType
        restItemTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, itemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameFr").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.nameDe").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.nameUrdu").value(DEFAULT_NAME_URDU))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getItemTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        Long id = itemType.getId();

        defaultItemTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultItemTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultItemTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where name equals to
        defaultItemTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where name in
        defaultItemTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where name is not null
        defaultItemTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where name contains
        defaultItemTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where name does not contain
        defaultItemTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameEn equals to
        defaultItemTypeFiltering("nameEn.equals=" + DEFAULT_NAME_EN, "nameEn.equals=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameEn in
        defaultItemTypeFiltering("nameEn.in=" + DEFAULT_NAME_EN + "," + UPDATED_NAME_EN, "nameEn.in=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameEn is not null
        defaultItemTypeFiltering("nameEn.specified=true", "nameEn.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTypesByNameEnContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameEn contains
        defaultItemTypeFiltering("nameEn.contains=" + DEFAULT_NAME_EN, "nameEn.contains=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameEnNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameEn does not contain
        defaultItemTypeFiltering("nameEn.doesNotContain=" + UPDATED_NAME_EN, "nameEn.doesNotContain=" + DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameArIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameAr equals to
        defaultItemTypeFiltering("nameAr.equals=" + DEFAULT_NAME_AR, "nameAr.equals=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameArIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameAr in
        defaultItemTypeFiltering("nameAr.in=" + DEFAULT_NAME_AR + "," + UPDATED_NAME_AR, "nameAr.in=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameArIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameAr is not null
        defaultItemTypeFiltering("nameAr.specified=true", "nameAr.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTypesByNameArContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameAr contains
        defaultItemTypeFiltering("nameAr.contains=" + DEFAULT_NAME_AR, "nameAr.contains=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameArNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameAr does not contain
        defaultItemTypeFiltering("nameAr.doesNotContain=" + UPDATED_NAME_AR, "nameAr.doesNotContain=" + DEFAULT_NAME_AR);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameFrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameFr equals to
        defaultItemTypeFiltering("nameFr.equals=" + DEFAULT_NAME_FR, "nameFr.equals=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameFrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameFr in
        defaultItemTypeFiltering("nameFr.in=" + DEFAULT_NAME_FR + "," + UPDATED_NAME_FR, "nameFr.in=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameFr is not null
        defaultItemTypeFiltering("nameFr.specified=true", "nameFr.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTypesByNameFrContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameFr contains
        defaultItemTypeFiltering("nameFr.contains=" + DEFAULT_NAME_FR, "nameFr.contains=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameFrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameFr does not contain
        defaultItemTypeFiltering("nameFr.doesNotContain=" + UPDATED_NAME_FR, "nameFr.doesNotContain=" + DEFAULT_NAME_FR);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameDeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameDe equals to
        defaultItemTypeFiltering("nameDe.equals=" + DEFAULT_NAME_DE, "nameDe.equals=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameDeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameDe in
        defaultItemTypeFiltering("nameDe.in=" + DEFAULT_NAME_DE + "," + UPDATED_NAME_DE, "nameDe.in=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameDe is not null
        defaultItemTypeFiltering("nameDe.specified=true", "nameDe.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTypesByNameDeContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameDe contains
        defaultItemTypeFiltering("nameDe.contains=" + DEFAULT_NAME_DE, "nameDe.contains=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameDeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameDe does not contain
        defaultItemTypeFiltering("nameDe.doesNotContain=" + UPDATED_NAME_DE, "nameDe.doesNotContain=" + DEFAULT_NAME_DE);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameUrduIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameUrdu equals to
        defaultItemTypeFiltering("nameUrdu.equals=" + DEFAULT_NAME_URDU, "nameUrdu.equals=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameUrduIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameUrdu in
        defaultItemTypeFiltering("nameUrdu.in=" + DEFAULT_NAME_URDU + "," + UPDATED_NAME_URDU, "nameUrdu.in=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameUrduIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameUrdu is not null
        defaultItemTypeFiltering("nameUrdu.specified=true", "nameUrdu.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTypesByNameUrduContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameUrdu contains
        defaultItemTypeFiltering("nameUrdu.contains=" + DEFAULT_NAME_URDU, "nameUrdu.contains=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllItemTypesByNameUrduNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where nameUrdu does not contain
        defaultItemTypeFiltering("nameUrdu.doesNotContain=" + UPDATED_NAME_URDU, "nameUrdu.doesNotContain=" + DEFAULT_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllItemTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where isActive equals to
        defaultItemTypeFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllItemTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where isActive in
        defaultItemTypeFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllItemTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList where isActive is not null
        defaultItemTypeFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultItemTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultItemTypeShouldBeFound(shouldBeFound);
        defaultItemTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemTypeShouldBeFound(String filter) throws Exception {
        restItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemTypeShouldNotBeFound(String filter) throws Exception {
        restItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingItemType() throws Exception {
        // Get the itemType
        restItemTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemType() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemTypeSearchRepository.save(itemType);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());

        // Update the itemType
        ItemType updatedItemType = itemTypeRepository.findById(itemType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedItemType are not directly saved in db
        em.detach(updatedItemType);
        updatedItemType
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .isActive(UPDATED_IS_ACTIVE);
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(updatedItemType);

        restItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItemTypeToMatchAllProperties(updatedItemType);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ItemType> itemTypeSearchList = Streamable.of(itemTypeSearchRepository.findAll()).toList();
                ItemType testItemTypeSearch = itemTypeSearchList.get(searchDatabaseSizeAfter - 1);

                assertItemTypeAllPropertiesEquals(testItemTypeSearch, updatedItemType);
            });
    }

    @Test
    @Transactional
    void putNonExistingItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        itemType.setId(longCount.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        itemType.setId(longCount.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        itemType.setId(longCount.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateItemTypeWithPatch() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemType using partial update
        ItemType partialUpdatedItemType = new ItemType();
        partialUpdatedItemType.setId(itemType.getId());

        partialUpdatedItemType.name(UPDATED_NAME).nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR).nameFr(UPDATED_NAME_FR);

        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemType))
            )
            .andExpect(status().isOk());

        // Validate the ItemType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedItemType, itemType), getPersistedItemType(itemType));
    }

    @Test
    @Transactional
    void fullUpdateItemTypeWithPatch() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemType using partial update
        ItemType partialUpdatedItemType = new ItemType();
        partialUpdatedItemType.setId(itemType.getId());

        partialUpdatedItemType
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .isActive(UPDATED_IS_ACTIVE);

        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemType))
            )
            .andExpect(status().isOk());

        // Validate the ItemType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemTypeUpdatableFieldsEquals(partialUpdatedItemType, getPersistedItemType(partialUpdatedItemType));
    }

    @Test
    @Transactional
    void patchNonExistingItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        itemType.setId(longCount.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        itemType.setId(longCount.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        itemType.setId(longCount.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteItemType() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);
        itemTypeRepository.save(itemType);
        itemTypeSearchRepository.save(itemType);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the itemType
        restItemTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchItemType() throws Exception {
        // Initialize the database
        insertedItemType = itemTypeRepository.saveAndFlush(itemType);
        itemTypeSearchRepository.save(itemType);

        // Search the itemType
        restItemTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + itemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    protected long getRepositoryCount() {
        return itemTypeRepository.count();
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

    protected ItemType getPersistedItemType(ItemType itemType) {
        return itemTypeRepository.findById(itemType.getId()).orElseThrow();
    }

    protected void assertPersistedItemTypeToMatchAllProperties(ItemType expectedItemType) {
        assertItemTypeAllPropertiesEquals(expectedItemType, getPersistedItemType(expectedItemType));
    }

    protected void assertPersistedItemTypeToMatchUpdatableProperties(ItemType expectedItemType) {
        assertItemTypeAllUpdatablePropertiesEquals(expectedItemType, getPersistedItemType(expectedItemType));
    }
}

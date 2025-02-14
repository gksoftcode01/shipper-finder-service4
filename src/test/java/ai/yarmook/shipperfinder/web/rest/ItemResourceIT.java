package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.ItemAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.Item;
import ai.yarmook.shipperfinder.domain.ItemType;
import ai.yarmook.shipperfinder.repository.ItemRepository;
import ai.yarmook.shipperfinder.repository.search.ItemSearchRepository;
import ai.yarmook.shipperfinder.service.ItemService;
import ai.yarmook.shipperfinder.service.dto.ItemDTO;
import ai.yarmook.shipperfinder.service.mapper.ItemMapper;
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
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

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

    private static final String DEFAULT_DEFAULT_UOM = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_UOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/items/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItemRepository itemRepository;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Autowired
    private ItemMapper itemMapper;

    @Mock
    private ItemService itemServiceMock;

    @Autowired
    private ItemSearchRepository itemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    private Item insertedItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity() {
        return new Item()
            .name(DEFAULT_NAME)
            .nameEn(DEFAULT_NAME_EN)
            .nameAr(DEFAULT_NAME_AR)
            .nameFr(DEFAULT_NAME_FR)
            .nameDe(DEFAULT_NAME_DE)
            .nameUrdu(DEFAULT_NAME_URDU)
            .isActive(DEFAULT_IS_ACTIVE)
            .defaultUOM(DEFAULT_DEFAULT_UOM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity() {
        return new Item()
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .isActive(UPDATED_IS_ACTIVE)
            .defaultUOM(UPDATED_DEFAULT_UOM);
    }

    @BeforeEach
    public void initTest() {
        item = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedItem != null) {
            itemRepository.delete(insertedItem);
            itemSearchRepository.delete(insertedItem);
            insertedItem = null;
        }
    }

    @Test
    @Transactional
    void createItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());
        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);
        var returnedItemDTO = om.readValue(
            restItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ItemDTO.class
        );

        // Validate the Item in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedItem = itemMapper.toEntity(returnedItemDTO);
        assertItemUpdatableFieldsEquals(returnedItem, getPersistedItem(returnedItem));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedItem = returnedItem;
    }

    @Test
    @Transactional
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);
        ItemDTO itemDTO = itemMapper.toDto(item);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllItems() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].defaultUOM").value(hasItem(DEFAULT_DEFAULT_UOM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(itemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc
            .perform(get(ENTITY_API_URL_ID, item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameFr").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.nameDe").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.nameUrdu").value(DEFAULT_NAME_URDU))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.defaultUOM").value(DEFAULT_DEFAULT_UOM));
    }

    @Test
    @Transactional
    void getItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        Long id = item.getId();

        defaultItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllItemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where name equals to
        defaultItemFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllItemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where name in
        defaultItemFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllItemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where name is not null
        defaultItemFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where name contains
        defaultItemFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllItemsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where name does not contain
        defaultItemFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllItemsByNameEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameEn equals to
        defaultItemFiltering("nameEn.equals=" + DEFAULT_NAME_EN, "nameEn.equals=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllItemsByNameEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameEn in
        defaultItemFiltering("nameEn.in=" + DEFAULT_NAME_EN + "," + UPDATED_NAME_EN, "nameEn.in=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllItemsByNameEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameEn is not null
        defaultItemFiltering("nameEn.specified=true", "nameEn.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByNameEnContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameEn contains
        defaultItemFiltering("nameEn.contains=" + DEFAULT_NAME_EN, "nameEn.contains=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllItemsByNameEnNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameEn does not contain
        defaultItemFiltering("nameEn.doesNotContain=" + UPDATED_NAME_EN, "nameEn.doesNotContain=" + DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void getAllItemsByNameArIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameAr equals to
        defaultItemFiltering("nameAr.equals=" + DEFAULT_NAME_AR, "nameAr.equals=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllItemsByNameArIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameAr in
        defaultItemFiltering("nameAr.in=" + DEFAULT_NAME_AR + "," + UPDATED_NAME_AR, "nameAr.in=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllItemsByNameArIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameAr is not null
        defaultItemFiltering("nameAr.specified=true", "nameAr.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByNameArContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameAr contains
        defaultItemFiltering("nameAr.contains=" + DEFAULT_NAME_AR, "nameAr.contains=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllItemsByNameArNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameAr does not contain
        defaultItemFiltering("nameAr.doesNotContain=" + UPDATED_NAME_AR, "nameAr.doesNotContain=" + DEFAULT_NAME_AR);
    }

    @Test
    @Transactional
    void getAllItemsByNameFrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameFr equals to
        defaultItemFiltering("nameFr.equals=" + DEFAULT_NAME_FR, "nameFr.equals=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllItemsByNameFrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameFr in
        defaultItemFiltering("nameFr.in=" + DEFAULT_NAME_FR + "," + UPDATED_NAME_FR, "nameFr.in=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllItemsByNameFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameFr is not null
        defaultItemFiltering("nameFr.specified=true", "nameFr.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByNameFrContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameFr contains
        defaultItemFiltering("nameFr.contains=" + DEFAULT_NAME_FR, "nameFr.contains=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllItemsByNameFrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameFr does not contain
        defaultItemFiltering("nameFr.doesNotContain=" + UPDATED_NAME_FR, "nameFr.doesNotContain=" + DEFAULT_NAME_FR);
    }

    @Test
    @Transactional
    void getAllItemsByNameDeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameDe equals to
        defaultItemFiltering("nameDe.equals=" + DEFAULT_NAME_DE, "nameDe.equals=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllItemsByNameDeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameDe in
        defaultItemFiltering("nameDe.in=" + DEFAULT_NAME_DE + "," + UPDATED_NAME_DE, "nameDe.in=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllItemsByNameDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameDe is not null
        defaultItemFiltering("nameDe.specified=true", "nameDe.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByNameDeContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameDe contains
        defaultItemFiltering("nameDe.contains=" + DEFAULT_NAME_DE, "nameDe.contains=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllItemsByNameDeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameDe does not contain
        defaultItemFiltering("nameDe.doesNotContain=" + UPDATED_NAME_DE, "nameDe.doesNotContain=" + DEFAULT_NAME_DE);
    }

    @Test
    @Transactional
    void getAllItemsByNameUrduIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameUrdu equals to
        defaultItemFiltering("nameUrdu.equals=" + DEFAULT_NAME_URDU, "nameUrdu.equals=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllItemsByNameUrduIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameUrdu in
        defaultItemFiltering("nameUrdu.in=" + DEFAULT_NAME_URDU + "," + UPDATED_NAME_URDU, "nameUrdu.in=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllItemsByNameUrduIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameUrdu is not null
        defaultItemFiltering("nameUrdu.specified=true", "nameUrdu.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByNameUrduContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameUrdu contains
        defaultItemFiltering("nameUrdu.contains=" + DEFAULT_NAME_URDU, "nameUrdu.contains=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllItemsByNameUrduNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where nameUrdu does not contain
        defaultItemFiltering("nameUrdu.doesNotContain=" + UPDATED_NAME_URDU, "nameUrdu.doesNotContain=" + DEFAULT_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllItemsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where isActive equals to
        defaultItemFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllItemsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where isActive in
        defaultItemFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllItemsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where isActive is not null
        defaultItemFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByDefaultUOMIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where defaultUOM equals to
        defaultItemFiltering("defaultUOM.equals=" + DEFAULT_DEFAULT_UOM, "defaultUOM.equals=" + UPDATED_DEFAULT_UOM);
    }

    @Test
    @Transactional
    void getAllItemsByDefaultUOMIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where defaultUOM in
        defaultItemFiltering("defaultUOM.in=" + DEFAULT_DEFAULT_UOM + "," + UPDATED_DEFAULT_UOM, "defaultUOM.in=" + UPDATED_DEFAULT_UOM);
    }

    @Test
    @Transactional
    void getAllItemsByDefaultUOMIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where defaultUOM is not null
        defaultItemFiltering("defaultUOM.specified=true", "defaultUOM.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByDefaultUOMContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where defaultUOM contains
        defaultItemFiltering("defaultUOM.contains=" + DEFAULT_DEFAULT_UOM, "defaultUOM.contains=" + UPDATED_DEFAULT_UOM);
    }

    @Test
    @Transactional
    void getAllItemsByDefaultUOMNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where defaultUOM does not contain
        defaultItemFiltering("defaultUOM.doesNotContain=" + UPDATED_DEFAULT_UOM, "defaultUOM.doesNotContain=" + DEFAULT_DEFAULT_UOM);
    }

    @Test
    @Transactional
    void getAllItemsByItemTypeIsEqualToSomething() throws Exception {
        ItemType itemType;
        if (TestUtil.findAll(em, ItemType.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            itemType = ItemTypeResourceIT.createEntity();
        } else {
            itemType = TestUtil.findAll(em, ItemType.class).get(0);
        }
        em.persist(itemType);
        em.flush();
        item.setItemType(itemType);
        itemRepository.saveAndFlush(item);
        Long itemTypeId = itemType.getId();
        // Get all the itemList where itemType equals to itemTypeId
        defaultItemShouldBeFound("itemTypeId.equals=" + itemTypeId);

        // Get all the itemList where itemType equals to (itemTypeId + 1)
        defaultItemShouldNotBeFound("itemTypeId.equals=" + (itemTypeId + 1));
    }

    private void defaultItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultItemShouldBeFound(shouldBeFound);
        defaultItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemShouldBeFound(String filter) throws Exception {
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].defaultUOM").value(hasItem(DEFAULT_DEFAULT_UOM)));

        // Check, that the count call also returns 1
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemShouldNotBeFound(String filter) throws Exception {
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemSearchRepository.save(item);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .isActive(UPDATED_IS_ACTIVE)
            .defaultUOM(UPDATED_DEFAULT_UOM);
        ItemDTO itemDTO = itemMapper.toDto(updatedItem);

        restItemMockMvc
            .perform(put(ENTITY_API_URL_ID, itemDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isOk());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItemToMatchAllProperties(updatedItem);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Item> itemSearchList = Streamable.of(itemSearchRepository.findAll()).toList();
                Item testItemSearch = itemSearchList.get(searchDatabaseSizeAfter - 1);

                assertItemAllPropertiesEquals(testItemSearch, updatedItem);
            });
    }

    @Test
    @Transactional
    void putNonExistingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL_ID, itemDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem.nameAr(UPDATED_NAME_AR).nameFr(UPDATED_NAME_FR).nameUrdu(UPDATED_NAME_URDU);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedItem, item), getPersistedItem(item));
    }

    @Test
    @Transactional
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .name(UPDATED_NAME)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .isActive(UPDATED_IS_ACTIVE)
            .defaultUOM(UPDATED_DEFAULT_UOM);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemUpdatableFieldsEquals(partialUpdatedItem, getPersistedItem(partialUpdatedItem));
    }

    @Test
    @Transactional
    void patchNonExistingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);
        itemRepository.save(item);
        itemSearchRepository.save(item);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the item
        restItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(itemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);
        itemSearchRepository.save(item);

        // Search the item
        restItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].defaultUOM").value(hasItem(DEFAULT_DEFAULT_UOM)));
    }

    protected long getRepositoryCount() {
        return itemRepository.count();
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

    protected Item getPersistedItem(Item item) {
        return itemRepository.findById(item.getId()).orElseThrow();
    }

    protected void assertPersistedItemToMatchAllProperties(Item expectedItem) {
        assertItemAllPropertiesEquals(expectedItem, getPersistedItem(expectedItem));
    }

    protected void assertPersistedItemToMatchUpdatableProperties(Item expectedItem) {
        assertItemAllUpdatablePropertiesEquals(expectedItem, getPersistedItem(expectedItem));
    }
}

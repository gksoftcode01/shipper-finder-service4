package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.TripItemAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.Item;
import ai.yarmook.shipperfinder.domain.Tag;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.TripItem;
import ai.yarmook.shipperfinder.domain.Unit;
import ai.yarmook.shipperfinder.repository.TripItemRepository;
import ai.yarmook.shipperfinder.repository.search.TripItemSearchRepository;
import ai.yarmook.shipperfinder.service.TripItemService;
import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
import ai.yarmook.shipperfinder.service.mapper.TripItemMapper;
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
 * Integration tests for the {@link TripItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TripItemResourceIT {

    private static final Float DEFAULT_ITEM_PRICE = 1F;
    private static final Float UPDATED_ITEM_PRICE = 2F;
    private static final Float SMALLER_ITEM_PRICE = 1F - 1F;

    private static final Long DEFAULT_MAX_QTY = 1L;
    private static final Long UPDATED_MAX_QTY = 2L;
    private static final Long SMALLER_MAX_QTY = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/trip-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/trip-items/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TripItemRepository tripItemRepository;

    @Mock
    private TripItemRepository tripItemRepositoryMock;

    @Autowired
    private TripItemMapper tripItemMapper;

    @Mock
    private TripItemService tripItemServiceMock;

    @Autowired
    private TripItemSearchRepository tripItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripItemMockMvc;

    private TripItem tripItem;

    private TripItem insertedTripItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripItem createEntity() {
        return new TripItem().itemPrice(DEFAULT_ITEM_PRICE).maxQty(DEFAULT_MAX_QTY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripItem createUpdatedEntity() {
        return new TripItem().itemPrice(UPDATED_ITEM_PRICE).maxQty(UPDATED_MAX_QTY);
    }

    @BeforeEach
    public void initTest() {
        tripItem = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTripItem != null) {
            tripItemRepository.delete(insertedTripItem);
            tripItemSearchRepository.delete(insertedTripItem);
            insertedTripItem = null;
        }
    }

    @Test
    @Transactional
    void createTripItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        // Create the TripItem
        TripItemDTO tripItemDTO = tripItemMapper.toDto(tripItem);
        var returnedTripItemDTO = om.readValue(
            restTripItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TripItemDTO.class
        );

        // Validate the TripItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTripItem = tripItemMapper.toEntity(returnedTripItemDTO);
        assertTripItemUpdatableFieldsEquals(returnedTripItem, getPersistedTripItem(returnedTripItem));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedTripItem = returnedTripItem;
    }

    @Test
    @Transactional
    void createTripItemWithExistingId() throws Exception {
        // Create the TripItem with an existing ID
        tripItem.setId(1L);
        TripItemDTO tripItemDTO = tripItemMapper.toDto(tripItem);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TripItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTripItems() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList
        restTripItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].maxQty").value(hasItem(DEFAULT_MAX_QTY.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTripItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(tripItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTripItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tripItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTripItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tripItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTripItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tripItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTripItem() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get the tripItem
        restTripItemMockMvc
            .perform(get(ENTITY_API_URL_ID, tripItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripItem.getId().intValue()))
            .andExpect(jsonPath("$.itemPrice").value(DEFAULT_ITEM_PRICE.doubleValue()))
            .andExpect(jsonPath("$.maxQty").value(DEFAULT_MAX_QTY.intValue()));
    }

    @Test
    @Transactional
    void getTripItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        Long id = tripItem.getId();

        defaultTripItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTripItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTripItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTripItemsByItemPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where itemPrice equals to
        defaultTripItemFiltering("itemPrice.equals=" + DEFAULT_ITEM_PRICE, "itemPrice.equals=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllTripItemsByItemPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where itemPrice in
        defaultTripItemFiltering("itemPrice.in=" + DEFAULT_ITEM_PRICE + "," + UPDATED_ITEM_PRICE, "itemPrice.in=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllTripItemsByItemPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where itemPrice is not null
        defaultTripItemFiltering("itemPrice.specified=true", "itemPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllTripItemsByItemPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where itemPrice is greater than or equal to
        defaultTripItemFiltering(
            "itemPrice.greaterThanOrEqual=" + DEFAULT_ITEM_PRICE,
            "itemPrice.greaterThanOrEqual=" + UPDATED_ITEM_PRICE
        );
    }

    @Test
    @Transactional
    void getAllTripItemsByItemPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where itemPrice is less than or equal to
        defaultTripItemFiltering("itemPrice.lessThanOrEqual=" + DEFAULT_ITEM_PRICE, "itemPrice.lessThanOrEqual=" + SMALLER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllTripItemsByItemPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where itemPrice is less than
        defaultTripItemFiltering("itemPrice.lessThan=" + UPDATED_ITEM_PRICE, "itemPrice.lessThan=" + DEFAULT_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllTripItemsByItemPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where itemPrice is greater than
        defaultTripItemFiltering("itemPrice.greaterThan=" + SMALLER_ITEM_PRICE, "itemPrice.greaterThan=" + DEFAULT_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllTripItemsByMaxQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where maxQty equals to
        defaultTripItemFiltering("maxQty.equals=" + DEFAULT_MAX_QTY, "maxQty.equals=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllTripItemsByMaxQtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where maxQty in
        defaultTripItemFiltering("maxQty.in=" + DEFAULT_MAX_QTY + "," + UPDATED_MAX_QTY, "maxQty.in=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllTripItemsByMaxQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where maxQty is not null
        defaultTripItemFiltering("maxQty.specified=true", "maxQty.specified=false");
    }

    @Test
    @Transactional
    void getAllTripItemsByMaxQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where maxQty is greater than or equal to
        defaultTripItemFiltering("maxQty.greaterThanOrEqual=" + DEFAULT_MAX_QTY, "maxQty.greaterThanOrEqual=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllTripItemsByMaxQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where maxQty is less than or equal to
        defaultTripItemFiltering("maxQty.lessThanOrEqual=" + DEFAULT_MAX_QTY, "maxQty.lessThanOrEqual=" + SMALLER_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllTripItemsByMaxQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where maxQty is less than
        defaultTripItemFiltering("maxQty.lessThan=" + UPDATED_MAX_QTY, "maxQty.lessThan=" + DEFAULT_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllTripItemsByMaxQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        // Get all the tripItemList where maxQty is greater than
        defaultTripItemFiltering("maxQty.greaterThan=" + SMALLER_MAX_QTY, "maxQty.greaterThan=" + DEFAULT_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllTripItemsByItemIsEqualToSomething() throws Exception {
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            tripItemRepository.saveAndFlush(tripItem);
            item = ItemResourceIT.createEntity();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        em.persist(item);
        em.flush();
        tripItem.setItem(item);
        tripItemRepository.saveAndFlush(tripItem);
        Long itemId = item.getId();
        // Get all the tripItemList where item equals to itemId
        defaultTripItemShouldBeFound("itemId.equals=" + itemId);

        // Get all the tripItemList where item equals to (itemId + 1)
        defaultTripItemShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    @Test
    @Transactional
    void getAllTripItemsByUnitIsEqualToSomething() throws Exception {
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            tripItemRepository.saveAndFlush(tripItem);
            unit = UnitResourceIT.createEntity();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        em.persist(unit);
        em.flush();
        tripItem.setUnit(unit);
        tripItemRepository.saveAndFlush(tripItem);
        Long unitId = unit.getId();
        // Get all the tripItemList where unit equals to unitId
        defaultTripItemShouldBeFound("unitId.equals=" + unitId);

        // Get all the tripItemList where unit equals to (unitId + 1)
        defaultTripItemShouldNotBeFound("unitId.equals=" + (unitId + 1));
    }

    @Test
    @Transactional
    void getAllTripItemsByTagIsEqualToSomething() throws Exception {
        Tag tag;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            tripItemRepository.saveAndFlush(tripItem);
            tag = TagResourceIT.createEntity();
        } else {
            tag = TestUtil.findAll(em, Tag.class).get(0);
        }
        em.persist(tag);
        em.flush();
        tripItem.addTag(tag);
        tripItemRepository.saveAndFlush(tripItem);
        Long tagId = tag.getId();
        // Get all the tripItemList where tag equals to tagId
        defaultTripItemShouldBeFound("tagId.equals=" + tagId);

        // Get all the tripItemList where tag equals to (tagId + 1)
        defaultTripItemShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }

    @Test
    @Transactional
    void getAllTripItemsByTripIsEqualToSomething() throws Exception {
        Trip trip;
        if (TestUtil.findAll(em, Trip.class).isEmpty()) {
            tripItemRepository.saveAndFlush(tripItem);
            trip = TripResourceIT.createEntity();
        } else {
            trip = TestUtil.findAll(em, Trip.class).get(0);
        }
        em.persist(trip);
        em.flush();
        tripItem.setTrip(trip);
        tripItemRepository.saveAndFlush(tripItem);
        Long tripId = trip.getId();
        // Get all the tripItemList where trip equals to tripId
        defaultTripItemShouldBeFound("tripId.equals=" + tripId);

        // Get all the tripItemList where trip equals to (tripId + 1)
        defaultTripItemShouldNotBeFound("tripId.equals=" + (tripId + 1));
    }

    private void defaultTripItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTripItemShouldBeFound(shouldBeFound);
        defaultTripItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTripItemShouldBeFound(String filter) throws Exception {
        restTripItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].maxQty").value(hasItem(DEFAULT_MAX_QTY.intValue())));

        // Check, that the count call also returns 1
        restTripItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTripItemShouldNotBeFound(String filter) throws Exception {
        restTripItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTripItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTripItem() throws Exception {
        // Get the tripItem
        restTripItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTripItem() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripItemSearchRepository.save(tripItem);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());

        // Update the tripItem
        TripItem updatedTripItem = tripItemRepository.findById(tripItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTripItem are not directly saved in db
        em.detach(updatedTripItem);
        updatedTripItem.itemPrice(UPDATED_ITEM_PRICE).maxQty(UPDATED_MAX_QTY);
        TripItemDTO tripItemDTO = tripItemMapper.toDto(updatedTripItem);

        restTripItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the TripItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTripItemToMatchAllProperties(updatedTripItem);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TripItem> tripItemSearchList = Streamable.of(tripItemSearchRepository.findAll()).toList();
                TripItem testTripItemSearch = tripItemSearchList.get(searchDatabaseSizeAfter - 1);

                assertTripItemAllPropertiesEquals(testTripItemSearch, updatedTripItem);
            });
    }

    @Test
    @Transactional
    void putNonExistingTripItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        tripItem.setId(longCount.incrementAndGet());

        // Create the TripItem
        TripItemDTO tripItemDTO = tripItemMapper.toDto(tripItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        tripItem.setId(longCount.incrementAndGet());

        // Create the TripItem
        TripItemDTO tripItemDTO = tripItemMapper.toDto(tripItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        tripItem.setId(longCount.incrementAndGet());

        // Create the TripItem
        TripItemDTO tripItemDTO = tripItemMapper.toDto(tripItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTripItemWithPatch() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tripItem using partial update
        TripItem partialUpdatedTripItem = new TripItem();
        partialUpdatedTripItem.setId(tripItem.getId());

        partialUpdatedTripItem.itemPrice(UPDATED_ITEM_PRICE).maxQty(UPDATED_MAX_QTY);

        restTripItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTripItem))
            )
            .andExpect(status().isOk());

        // Validate the TripItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTripItem, tripItem), getPersistedTripItem(tripItem));
    }

    @Test
    @Transactional
    void fullUpdateTripItemWithPatch() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tripItem using partial update
        TripItem partialUpdatedTripItem = new TripItem();
        partialUpdatedTripItem.setId(tripItem.getId());

        partialUpdatedTripItem.itemPrice(UPDATED_ITEM_PRICE).maxQty(UPDATED_MAX_QTY);

        restTripItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTripItem))
            )
            .andExpect(status().isOk());

        // Validate the TripItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripItemUpdatableFieldsEquals(partialUpdatedTripItem, getPersistedTripItem(partialUpdatedTripItem));
    }

    @Test
    @Transactional
    void patchNonExistingTripItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        tripItem.setId(longCount.incrementAndGet());

        // Create the TripItem
        TripItemDTO tripItemDTO = tripItemMapper.toDto(tripItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tripItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        tripItem.setId(longCount.incrementAndGet());

        // Create the TripItem
        TripItemDTO tripItemDTO = tripItemMapper.toDto(tripItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tripItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        tripItem.setId(longCount.incrementAndGet());

        // Create the TripItem
        TripItemDTO tripItemDTO = tripItemMapper.toDto(tripItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tripItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTripItem() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);
        tripItemRepository.save(tripItem);
        tripItemSearchRepository.save(tripItem);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tripItem
        restTripItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tripItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTripItem() throws Exception {
        // Initialize the database
        insertedTripItem = tripItemRepository.saveAndFlush(tripItem);
        tripItemSearchRepository.save(tripItem);

        // Search the tripItem
        restTripItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tripItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].maxQty").value(hasItem(DEFAULT_MAX_QTY.intValue())));
    }

    protected long getRepositoryCount() {
        return tripItemRepository.count();
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

    protected TripItem getPersistedTripItem(TripItem tripItem) {
        return tripItemRepository.findById(tripItem.getId()).orElseThrow();
    }

    protected void assertPersistedTripItemToMatchAllProperties(TripItem expectedTripItem) {
        assertTripItemAllPropertiesEquals(expectedTripItem, getPersistedTripItem(expectedTripItem));
    }

    protected void assertPersistedTripItemToMatchUpdatableProperties(TripItem expectedTripItem) {
        assertTripItemAllUpdatablePropertiesEquals(expectedTripItem, getPersistedTripItem(expectedTripItem));
    }
}

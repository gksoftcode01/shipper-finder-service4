package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.CargoRequestItemAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.domain.Item;
import ai.yarmook.shipperfinder.domain.Tag;
import ai.yarmook.shipperfinder.domain.Unit;
import ai.yarmook.shipperfinder.repository.CargoRequestItemRepository;
import ai.yarmook.shipperfinder.repository.search.CargoRequestItemSearchRepository;
import ai.yarmook.shipperfinder.service.CargoRequestItemService;
import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoRequestItemMapper;
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
 * Integration tests for the {@link CargoRequestItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CargoRequestItemResourceIT {

    private static final Long DEFAULT_MAX_QTY = 1L;
    private static final Long UPDATED_MAX_QTY = 2L;
    private static final Long SMALLER_MAX_QTY = 1L - 1L;

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cargo-request-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/cargo-request-items/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CargoRequestItemRepository cargoRequestItemRepository;

    @Mock
    private CargoRequestItemRepository cargoRequestItemRepositoryMock;

    @Autowired
    private CargoRequestItemMapper cargoRequestItemMapper;

    @Mock
    private CargoRequestItemService cargoRequestItemServiceMock;

    @Autowired
    private CargoRequestItemSearchRepository cargoRequestItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoRequestItemMockMvc;

    private CargoRequestItem cargoRequestItem;

    private CargoRequestItem insertedCargoRequestItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequestItem createEntity() {
        return new CargoRequestItem().maxQty(DEFAULT_MAX_QTY).photoUrl(DEFAULT_PHOTO_URL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequestItem createUpdatedEntity() {
        return new CargoRequestItem().maxQty(UPDATED_MAX_QTY).photoUrl(UPDATED_PHOTO_URL);
    }

    @BeforeEach
    public void initTest() {
        cargoRequestItem = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCargoRequestItem != null) {
            cargoRequestItemRepository.delete(insertedCargoRequestItem);
            cargoRequestItemSearchRepository.delete(insertedCargoRequestItem);
            insertedCargoRequestItem = null;
        }
    }

    @Test
    @Transactional
    void createCargoRequestItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        // Create the CargoRequestItem
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(cargoRequestItem);
        var returnedCargoRequestItemDTO = om.readValue(
            restCargoRequestItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoRequestItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CargoRequestItemDTO.class
        );

        // Validate the CargoRequestItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCargoRequestItem = cargoRequestItemMapper.toEntity(returnedCargoRequestItemDTO);
        assertCargoRequestItemUpdatableFieldsEquals(returnedCargoRequestItem, getPersistedCargoRequestItem(returnedCargoRequestItem));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCargoRequestItem = returnedCargoRequestItem;
    }

    @Test
    @Transactional
    void createCargoRequestItemWithExistingId() throws Exception {
        // Create the CargoRequestItem with an existing ID
        cargoRequestItem.setId(1L);
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(cargoRequestItem);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoRequestItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoRequestItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCargoRequestItems() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList
        restCargoRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequestItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxQty").value(hasItem(DEFAULT_MAX_QTY.intValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCargoRequestItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cargoRequestItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCargoRequestItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cargoRequestItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCargoRequestItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cargoRequestItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCargoRequestItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cargoRequestItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCargoRequestItem() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get the cargoRequestItem
        restCargoRequestItemMockMvc
            .perform(get(ENTITY_API_URL_ID, cargoRequestItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargoRequestItem.getId().intValue()))
            .andExpect(jsonPath("$.maxQty").value(DEFAULT_MAX_QTY.intValue()))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL));
    }

    @Test
    @Transactional
    void getCargoRequestItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        Long id = cargoRequestItem.getId();

        defaultCargoRequestItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCargoRequestItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCargoRequestItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByMaxQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where maxQty equals to
        defaultCargoRequestItemFiltering("maxQty.equals=" + DEFAULT_MAX_QTY, "maxQty.equals=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByMaxQtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where maxQty in
        defaultCargoRequestItemFiltering("maxQty.in=" + DEFAULT_MAX_QTY + "," + UPDATED_MAX_QTY, "maxQty.in=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByMaxQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where maxQty is not null
        defaultCargoRequestItemFiltering("maxQty.specified=true", "maxQty.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByMaxQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where maxQty is greater than or equal to
        defaultCargoRequestItemFiltering("maxQty.greaterThanOrEqual=" + DEFAULT_MAX_QTY, "maxQty.greaterThanOrEqual=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByMaxQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where maxQty is less than or equal to
        defaultCargoRequestItemFiltering("maxQty.lessThanOrEqual=" + DEFAULT_MAX_QTY, "maxQty.lessThanOrEqual=" + SMALLER_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByMaxQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where maxQty is less than
        defaultCargoRequestItemFiltering("maxQty.lessThan=" + UPDATED_MAX_QTY, "maxQty.lessThan=" + DEFAULT_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByMaxQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where maxQty is greater than
        defaultCargoRequestItemFiltering("maxQty.greaterThan=" + SMALLER_MAX_QTY, "maxQty.greaterThan=" + DEFAULT_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where photoUrl equals to
        defaultCargoRequestItemFiltering("photoUrl.equals=" + DEFAULT_PHOTO_URL, "photoUrl.equals=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where photoUrl in
        defaultCargoRequestItemFiltering("photoUrl.in=" + DEFAULT_PHOTO_URL + "," + UPDATED_PHOTO_URL, "photoUrl.in=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where photoUrl is not null
        defaultCargoRequestItemFiltering("photoUrl.specified=true", "photoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByPhotoUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where photoUrl contains
        defaultCargoRequestItemFiltering("photoUrl.contains=" + DEFAULT_PHOTO_URL, "photoUrl.contains=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByPhotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        // Get all the cargoRequestItemList where photoUrl does not contain
        defaultCargoRequestItemFiltering("photoUrl.doesNotContain=" + UPDATED_PHOTO_URL, "photoUrl.doesNotContain=" + DEFAULT_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByItemIsEqualToSomething() throws Exception {
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
            item = ItemResourceIT.createEntity();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        em.persist(item);
        em.flush();
        cargoRequestItem.setItem(item);
        cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
        Long itemId = item.getId();
        // Get all the cargoRequestItemList where item equals to itemId
        defaultCargoRequestItemShouldBeFound("itemId.equals=" + itemId);

        // Get all the cargoRequestItemList where item equals to (itemId + 1)
        defaultCargoRequestItemShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByUnitIsEqualToSomething() throws Exception {
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
            unit = UnitResourceIT.createEntity();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        em.persist(unit);
        em.flush();
        cargoRequestItem.setUnit(unit);
        cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
        Long unitId = unit.getId();
        // Get all the cargoRequestItemList where unit equals to unitId
        defaultCargoRequestItemShouldBeFound("unitId.equals=" + unitId);

        // Get all the cargoRequestItemList where unit equals to (unitId + 1)
        defaultCargoRequestItemShouldNotBeFound("unitId.equals=" + (unitId + 1));
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByTagIsEqualToSomething() throws Exception {
        Tag tag;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
            tag = TagResourceIT.createEntity();
        } else {
            tag = TestUtil.findAll(em, Tag.class).get(0);
        }
        em.persist(tag);
        em.flush();
        cargoRequestItem.addTag(tag);
        cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
        Long tagId = tag.getId();
        // Get all the cargoRequestItemList where tag equals to tagId
        defaultCargoRequestItemShouldBeFound("tagId.equals=" + tagId);

        // Get all the cargoRequestItemList where tag equals to (tagId + 1)
        defaultCargoRequestItemShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }

    @Test
    @Transactional
    void getAllCargoRequestItemsByCargoRequestIsEqualToSomething() throws Exception {
        CargoRequest cargoRequest;
        if (TestUtil.findAll(em, CargoRequest.class).isEmpty()) {
            cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
            cargoRequest = CargoRequestResourceIT.createEntity();
        } else {
            cargoRequest = TestUtil.findAll(em, CargoRequest.class).get(0);
        }
        em.persist(cargoRequest);
        em.flush();
        cargoRequestItem.setCargoRequest(cargoRequest);
        cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
        Long cargoRequestId = cargoRequest.getId();
        // Get all the cargoRequestItemList where cargoRequest equals to cargoRequestId
        defaultCargoRequestItemShouldBeFound("cargoRequestId.equals=" + cargoRequestId);

        // Get all the cargoRequestItemList where cargoRequest equals to (cargoRequestId + 1)
        defaultCargoRequestItemShouldNotBeFound("cargoRequestId.equals=" + (cargoRequestId + 1));
    }

    private void defaultCargoRequestItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCargoRequestItemShouldBeFound(shouldBeFound);
        defaultCargoRequestItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCargoRequestItemShouldBeFound(String filter) throws Exception {
        restCargoRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequestItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxQty").value(hasItem(DEFAULT_MAX_QTY.intValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)));

        // Check, that the count call also returns 1
        restCargoRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCargoRequestItemShouldNotBeFound(String filter) throws Exception {
        restCargoRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCargoRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCargoRequestItem() throws Exception {
        // Get the cargoRequestItem
        restCargoRequestItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCargoRequestItem() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoRequestItemSearchRepository.save(cargoRequestItem);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());

        // Update the cargoRequestItem
        CargoRequestItem updatedCargoRequestItem = cargoRequestItemRepository.findById(cargoRequestItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCargoRequestItem are not directly saved in db
        em.detach(updatedCargoRequestItem);
        updatedCargoRequestItem.maxQty(UPDATED_MAX_QTY).photoUrl(UPDATED_PHOTO_URL);
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(updatedCargoRequestItem);

        restCargoRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoRequestItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoRequestItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCargoRequestItemToMatchAllProperties(updatedCargoRequestItem);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CargoRequestItem> cargoRequestItemSearchList = Streamable.of(cargoRequestItemSearchRepository.findAll()).toList();
                CargoRequestItem testCargoRequestItemSearch = cargoRequestItemSearchList.get(searchDatabaseSizeAfter - 1);

                assertCargoRequestItemAllPropertiesEquals(testCargoRequestItemSearch, updatedCargoRequestItem);
            });
    }

    @Test
    @Transactional
    void putNonExistingCargoRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        cargoRequestItem.setId(longCount.incrementAndGet());

        // Create the CargoRequestItem
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(cargoRequestItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoRequestItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoRequestItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargoRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        cargoRequestItem.setId(longCount.incrementAndGet());

        // Create the CargoRequestItem
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(cargoRequestItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoRequestItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargoRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        cargoRequestItem.setId(longCount.incrementAndGet());

        // Create the CargoRequestItem
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(cargoRequestItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoRequestItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCargoRequestItemWithPatch() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoRequestItem using partial update
        CargoRequestItem partialUpdatedCargoRequestItem = new CargoRequestItem();
        partialUpdatedCargoRequestItem.setId(cargoRequestItem.getId());

        restCargoRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequestItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargoRequestItem))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoRequestItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCargoRequestItem, cargoRequestItem),
            getPersistedCargoRequestItem(cargoRequestItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateCargoRequestItemWithPatch() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoRequestItem using partial update
        CargoRequestItem partialUpdatedCargoRequestItem = new CargoRequestItem();
        partialUpdatedCargoRequestItem.setId(cargoRequestItem.getId());

        partialUpdatedCargoRequestItem.maxQty(UPDATED_MAX_QTY).photoUrl(UPDATED_PHOTO_URL);

        restCargoRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequestItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargoRequestItem))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoRequestItemUpdatableFieldsEquals(
            partialUpdatedCargoRequestItem,
            getPersistedCargoRequestItem(partialUpdatedCargoRequestItem)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCargoRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        cargoRequestItem.setId(longCount.incrementAndGet());

        // Create the CargoRequestItem
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(cargoRequestItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoRequestItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoRequestItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargoRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        cargoRequestItem.setId(longCount.incrementAndGet());

        // Create the CargoRequestItem
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(cargoRequestItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoRequestItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargoRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        cargoRequestItem.setId(longCount.incrementAndGet());

        // Create the CargoRequestItem
        CargoRequestItemDTO cargoRequestItemDTO = cargoRequestItemMapper.toDto(cargoRequestItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cargoRequestItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCargoRequestItem() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
        cargoRequestItemRepository.save(cargoRequestItem);
        cargoRequestItemSearchRepository.save(cargoRequestItem);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the cargoRequestItem
        restCargoRequestItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargoRequestItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cargoRequestItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCargoRequestItem() throws Exception {
        // Initialize the database
        insertedCargoRequestItem = cargoRequestItemRepository.saveAndFlush(cargoRequestItem);
        cargoRequestItemSearchRepository.save(cargoRequestItem);

        // Search the cargoRequestItem
        restCargoRequestItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cargoRequestItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequestItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxQty").value(hasItem(DEFAULT_MAX_QTY.intValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)));
    }

    protected long getRepositoryCount() {
        return cargoRequestItemRepository.count();
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

    protected CargoRequestItem getPersistedCargoRequestItem(CargoRequestItem cargoRequestItem) {
        return cargoRequestItemRepository.findById(cargoRequestItem.getId()).orElseThrow();
    }

    protected void assertPersistedCargoRequestItemToMatchAllProperties(CargoRequestItem expectedCargoRequestItem) {
        assertCargoRequestItemAllPropertiesEquals(expectedCargoRequestItem, getPersistedCargoRequestItem(expectedCargoRequestItem));
    }

    protected void assertPersistedCargoRequestItemToMatchUpdatableProperties(CargoRequestItem expectedCargoRequestItem) {
        assertCargoRequestItemAllUpdatablePropertiesEquals(
            expectedCargoRequestItem,
            getPersistedCargoRequestItem(expectedCargoRequestItem)
        );
    }
}

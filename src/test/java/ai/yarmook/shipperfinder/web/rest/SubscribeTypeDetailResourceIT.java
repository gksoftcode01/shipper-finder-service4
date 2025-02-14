package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.SubscribeTypeDetailAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.SubscribeTypeDetail;
import ai.yarmook.shipperfinder.repository.SubscribeTypeDetailRepository;
import ai.yarmook.shipperfinder.repository.search.SubscribeTypeDetailSearchRepository;
import ai.yarmook.shipperfinder.service.SubscribeTypeDetailService;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDetailDTO;
import ai.yarmook.shipperfinder.service.mapper.SubscribeTypeDetailMapper;
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
 * Integration tests for the {@link SubscribeTypeDetailResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubscribeTypeDetailResourceIT {

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Integer DEFAULT_MAX_TRIP = 1;
    private static final Integer UPDATED_MAX_TRIP = 2;

    private static final Integer DEFAULT_MAX_ITEMS = 1;
    private static final Integer UPDATED_MAX_ITEMS = 2;

    private static final Integer DEFAULT_MAX_REQUEST = 1;
    private static final Integer UPDATED_MAX_REQUEST = 2;

    private static final Integer DEFAULT_MAX_NUMBER_VIEW = 1;
    private static final Integer UPDATED_MAX_NUMBER_VIEW = 2;

    private static final String ENTITY_API_URL = "/api/subscribe-type-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/subscribe-type-details/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscribeTypeDetailRepository subscribeTypeDetailRepository;

    @Mock
    private SubscribeTypeDetailRepository subscribeTypeDetailRepositoryMock;

    @Autowired
    private SubscribeTypeDetailMapper subscribeTypeDetailMapper;

    @Mock
    private SubscribeTypeDetailService subscribeTypeDetailServiceMock;

    @Autowired
    private SubscribeTypeDetailSearchRepository subscribeTypeDetailSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscribeTypeDetailMockMvc;

    private SubscribeTypeDetail subscribeTypeDetail;

    private SubscribeTypeDetail insertedSubscribeTypeDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscribeTypeDetail createEntity() {
        return new SubscribeTypeDetail()
            .price(DEFAULT_PRICE)
            .maxTrip(DEFAULT_MAX_TRIP)
            .maxItems(DEFAULT_MAX_ITEMS)
            .maxRequest(DEFAULT_MAX_REQUEST)
            .maxNumberView(DEFAULT_MAX_NUMBER_VIEW);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscribeTypeDetail createUpdatedEntity() {
        return new SubscribeTypeDetail()
            .price(UPDATED_PRICE)
            .maxTrip(UPDATED_MAX_TRIP)
            .maxItems(UPDATED_MAX_ITEMS)
            .maxRequest(UPDATED_MAX_REQUEST)
            .maxNumberView(UPDATED_MAX_NUMBER_VIEW);
    }

    @BeforeEach
    public void initTest() {
        subscribeTypeDetail = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubscribeTypeDetail != null) {
            subscribeTypeDetailRepository.delete(insertedSubscribeTypeDetail);
            subscribeTypeDetailSearchRepository.delete(insertedSubscribeTypeDetail);
            insertedSubscribeTypeDetail = null;
        }
    }

    @Test
    @Transactional
    void createSubscribeTypeDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        // Create the SubscribeTypeDetail
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(subscribeTypeDetail);
        var returnedSubscribeTypeDetailDTO = om.readValue(
            restSubscribeTypeDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscribeTypeDetailDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscribeTypeDetailDTO.class
        );

        // Validate the SubscribeTypeDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubscribeTypeDetail = subscribeTypeDetailMapper.toEntity(returnedSubscribeTypeDetailDTO);
        assertSubscribeTypeDetailUpdatableFieldsEquals(
            returnedSubscribeTypeDetail,
            getPersistedSubscribeTypeDetail(returnedSubscribeTypeDetail)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSubscribeTypeDetail = returnedSubscribeTypeDetail;
    }

    @Test
    @Transactional
    void createSubscribeTypeDetailWithExistingId() throws Exception {
        // Create the SubscribeTypeDetail with an existing ID
        subscribeTypeDetail.setId(1L);
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(subscribeTypeDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscribeTypeDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscribeTypeDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscribeTypeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSubscribeTypeDetails() throws Exception {
        // Initialize the database
        insertedSubscribeTypeDetail = subscribeTypeDetailRepository.saveAndFlush(subscribeTypeDetail);

        // Get all the subscribeTypeDetailList
        restSubscribeTypeDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribeTypeDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].maxTrip").value(hasItem(DEFAULT_MAX_TRIP)))
            .andExpect(jsonPath("$.[*].maxItems").value(hasItem(DEFAULT_MAX_ITEMS)))
            .andExpect(jsonPath("$.[*].maxRequest").value(hasItem(DEFAULT_MAX_REQUEST)))
            .andExpect(jsonPath("$.[*].maxNumberView").value(hasItem(DEFAULT_MAX_NUMBER_VIEW)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscribeTypeDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(subscribeTypeDetailServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscribeTypeDetailMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subscribeTypeDetailServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscribeTypeDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subscribeTypeDetailServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscribeTypeDetailMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(subscribeTypeDetailRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSubscribeTypeDetail() throws Exception {
        // Initialize the database
        insertedSubscribeTypeDetail = subscribeTypeDetailRepository.saveAndFlush(subscribeTypeDetail);

        // Get the subscribeTypeDetail
        restSubscribeTypeDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, subscribeTypeDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscribeTypeDetail.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.maxTrip").value(DEFAULT_MAX_TRIP))
            .andExpect(jsonPath("$.maxItems").value(DEFAULT_MAX_ITEMS))
            .andExpect(jsonPath("$.maxRequest").value(DEFAULT_MAX_REQUEST))
            .andExpect(jsonPath("$.maxNumberView").value(DEFAULT_MAX_NUMBER_VIEW));
    }

    @Test
    @Transactional
    void getNonExistingSubscribeTypeDetail() throws Exception {
        // Get the subscribeTypeDetail
        restSubscribeTypeDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscribeTypeDetail() throws Exception {
        // Initialize the database
        insertedSubscribeTypeDetail = subscribeTypeDetailRepository.saveAndFlush(subscribeTypeDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscribeTypeDetailSearchRepository.save(subscribeTypeDetail);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());

        // Update the subscribeTypeDetail
        SubscribeTypeDetail updatedSubscribeTypeDetail = subscribeTypeDetailRepository.findById(subscribeTypeDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscribeTypeDetail are not directly saved in db
        em.detach(updatedSubscribeTypeDetail);
        updatedSubscribeTypeDetail
            .price(UPDATED_PRICE)
            .maxTrip(UPDATED_MAX_TRIP)
            .maxItems(UPDATED_MAX_ITEMS)
            .maxRequest(UPDATED_MAX_REQUEST)
            .maxNumberView(UPDATED_MAX_NUMBER_VIEW);
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(updatedSubscribeTypeDetail);

        restSubscribeTypeDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscribeTypeDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscribeTypeDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscribeTypeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscribeTypeDetailToMatchAllProperties(updatedSubscribeTypeDetail);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SubscribeTypeDetail> subscribeTypeDetailSearchList = Streamable.of(
                    subscribeTypeDetailSearchRepository.findAll()
                ).toList();
                SubscribeTypeDetail testSubscribeTypeDetailSearch = subscribeTypeDetailSearchList.get(searchDatabaseSizeAfter - 1);

                assertSubscribeTypeDetailAllPropertiesEquals(testSubscribeTypeDetailSearch, updatedSubscribeTypeDetail);
            });
    }

    @Test
    @Transactional
    void putNonExistingSubscribeTypeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        subscribeTypeDetail.setId(longCount.incrementAndGet());

        // Create the SubscribeTypeDetail
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(subscribeTypeDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscribeTypeDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscribeTypeDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscribeTypeDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribeTypeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscribeTypeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        subscribeTypeDetail.setId(longCount.incrementAndGet());

        // Create the SubscribeTypeDetail
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(subscribeTypeDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribeTypeDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscribeTypeDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribeTypeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscribeTypeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        subscribeTypeDetail.setId(longCount.incrementAndGet());

        // Create the SubscribeTypeDetail
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(subscribeTypeDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribeTypeDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscribeTypeDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscribeTypeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSubscribeTypeDetailWithPatch() throws Exception {
        // Initialize the database
        insertedSubscribeTypeDetail = subscribeTypeDetailRepository.saveAndFlush(subscribeTypeDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscribeTypeDetail using partial update
        SubscribeTypeDetail partialUpdatedSubscribeTypeDetail = new SubscribeTypeDetail();
        partialUpdatedSubscribeTypeDetail.setId(subscribeTypeDetail.getId());

        partialUpdatedSubscribeTypeDetail.maxRequest(UPDATED_MAX_REQUEST).maxNumberView(UPDATED_MAX_NUMBER_VIEW);

        restSubscribeTypeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscribeTypeDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscribeTypeDetail))
            )
            .andExpect(status().isOk());

        // Validate the SubscribeTypeDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscribeTypeDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscribeTypeDetail, subscribeTypeDetail),
            getPersistedSubscribeTypeDetail(subscribeTypeDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscribeTypeDetailWithPatch() throws Exception {
        // Initialize the database
        insertedSubscribeTypeDetail = subscribeTypeDetailRepository.saveAndFlush(subscribeTypeDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscribeTypeDetail using partial update
        SubscribeTypeDetail partialUpdatedSubscribeTypeDetail = new SubscribeTypeDetail();
        partialUpdatedSubscribeTypeDetail.setId(subscribeTypeDetail.getId());

        partialUpdatedSubscribeTypeDetail
            .price(UPDATED_PRICE)
            .maxTrip(UPDATED_MAX_TRIP)
            .maxItems(UPDATED_MAX_ITEMS)
            .maxRequest(UPDATED_MAX_REQUEST)
            .maxNumberView(UPDATED_MAX_NUMBER_VIEW);

        restSubscribeTypeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscribeTypeDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscribeTypeDetail))
            )
            .andExpect(status().isOk());

        // Validate the SubscribeTypeDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscribeTypeDetailUpdatableFieldsEquals(
            partialUpdatedSubscribeTypeDetail,
            getPersistedSubscribeTypeDetail(partialUpdatedSubscribeTypeDetail)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubscribeTypeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        subscribeTypeDetail.setId(longCount.incrementAndGet());

        // Create the SubscribeTypeDetail
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(subscribeTypeDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscribeTypeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscribeTypeDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscribeTypeDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribeTypeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscribeTypeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        subscribeTypeDetail.setId(longCount.incrementAndGet());

        // Create the SubscribeTypeDetail
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(subscribeTypeDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribeTypeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscribeTypeDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribeTypeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscribeTypeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        subscribeTypeDetail.setId(longCount.incrementAndGet());

        // Create the SubscribeTypeDetail
        SubscribeTypeDetailDTO subscribeTypeDetailDTO = subscribeTypeDetailMapper.toDto(subscribeTypeDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribeTypeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subscribeTypeDetailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscribeTypeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSubscribeTypeDetail() throws Exception {
        // Initialize the database
        insertedSubscribeTypeDetail = subscribeTypeDetailRepository.saveAndFlush(subscribeTypeDetail);
        subscribeTypeDetailRepository.save(subscribeTypeDetail);
        subscribeTypeDetailSearchRepository.save(subscribeTypeDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the subscribeTypeDetail
        restSubscribeTypeDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscribeTypeDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeDetailSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSubscribeTypeDetail() throws Exception {
        // Initialize the database
        insertedSubscribeTypeDetail = subscribeTypeDetailRepository.saveAndFlush(subscribeTypeDetail);
        subscribeTypeDetailSearchRepository.save(subscribeTypeDetail);

        // Search the subscribeTypeDetail
        restSubscribeTypeDetailMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + subscribeTypeDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribeTypeDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].maxTrip").value(hasItem(DEFAULT_MAX_TRIP)))
            .andExpect(jsonPath("$.[*].maxItems").value(hasItem(DEFAULT_MAX_ITEMS)))
            .andExpect(jsonPath("$.[*].maxRequest").value(hasItem(DEFAULT_MAX_REQUEST)))
            .andExpect(jsonPath("$.[*].maxNumberView").value(hasItem(DEFAULT_MAX_NUMBER_VIEW)));
    }

    protected long getRepositoryCount() {
        return subscribeTypeDetailRepository.count();
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

    protected SubscribeTypeDetail getPersistedSubscribeTypeDetail(SubscribeTypeDetail subscribeTypeDetail) {
        return subscribeTypeDetailRepository.findById(subscribeTypeDetail.getId()).orElseThrow();
    }

    protected void assertPersistedSubscribeTypeDetailToMatchAllProperties(SubscribeTypeDetail expectedSubscribeTypeDetail) {
        assertSubscribeTypeDetailAllPropertiesEquals(
            expectedSubscribeTypeDetail,
            getPersistedSubscribeTypeDetail(expectedSubscribeTypeDetail)
        );
    }

    protected void assertPersistedSubscribeTypeDetailToMatchUpdatableProperties(SubscribeTypeDetail expectedSubscribeTypeDetail) {
        assertSubscribeTypeDetailAllUpdatablePropertiesEquals(
            expectedSubscribeTypeDetail,
            getPersistedSubscribeTypeDetail(expectedSubscribeTypeDetail)
        );
    }
}

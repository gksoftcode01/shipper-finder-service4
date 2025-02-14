package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.UserSubscribeAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.domain.UserSubscribe;
import ai.yarmook.shipperfinder.repository.UserSubscribeRepository;
import ai.yarmook.shipperfinder.repository.search.UserSubscribeSearchRepository;
import ai.yarmook.shipperfinder.service.UserSubscribeService;
import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import ai.yarmook.shipperfinder.service.mapper.UserSubscribeMapper;
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
 * Integration tests for the {@link UserSubscribeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserSubscribeResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final UUID DEFAULT_SUBSCRIBED_USER_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_SUBSCRIBED_USER_ENC_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/user-subscribes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-subscribes/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserSubscribeRepository userSubscribeRepository;

    @Mock
    private UserSubscribeRepository userSubscribeRepositoryMock;

    @Autowired
    private UserSubscribeMapper userSubscribeMapper;

    @Mock
    private UserSubscribeService userSubscribeServiceMock;

    @Autowired
    private UserSubscribeSearchRepository userSubscribeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSubscribeMockMvc;

    private UserSubscribe userSubscribe;

    private UserSubscribe insertedUserSubscribe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSubscribe createEntity() {
        return new UserSubscribe()
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .isActive(DEFAULT_IS_ACTIVE)
            .subscribedUserEncId(DEFAULT_SUBSCRIBED_USER_ENC_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSubscribe createUpdatedEntity() {
        return new UserSubscribe()
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .subscribedUserEncId(UPDATED_SUBSCRIBED_USER_ENC_ID);
    }

    @BeforeEach
    public void initTest() {
        userSubscribe = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserSubscribe != null) {
            userSubscribeRepository.delete(insertedUserSubscribe);
            userSubscribeSearchRepository.delete(insertedUserSubscribe);
            insertedUserSubscribe = null;
        }
    }

    @Test
    @Transactional
    void createUserSubscribe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        // Create the UserSubscribe
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(userSubscribe);
        var returnedUserSubscribeDTO = om.readValue(
            restUserSubscribeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSubscribeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserSubscribeDTO.class
        );

        // Validate the UserSubscribe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserSubscribe = userSubscribeMapper.toEntity(returnedUserSubscribeDTO);
        assertUserSubscribeUpdatableFieldsEquals(returnedUserSubscribe, getPersistedUserSubscribe(returnedUserSubscribe));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedUserSubscribe = returnedUserSubscribe;
    }

    @Test
    @Transactional
    void createUserSubscribeWithExistingId() throws Exception {
        // Create the UserSubscribe with an existing ID
        userSubscribe.setId(1L);
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(userSubscribe);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSubscribeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSubscribeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserSubscribe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUserSubscribes() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList
        restUserSubscribeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscribe.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].subscribedUserEncId").value(hasItem(DEFAULT_SUBSCRIBED_USER_ENC_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserSubscribesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userSubscribeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserSubscribeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userSubscribeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserSubscribesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userSubscribeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserSubscribeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userSubscribeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserSubscribe() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get the userSubscribe
        restUserSubscribeMockMvc
            .perform(get(ENTITY_API_URL_ID, userSubscribe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSubscribe.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.subscribedUserEncId").value(DEFAULT_SUBSCRIBED_USER_ENC_ID.toString()));
    }

    @Test
    @Transactional
    void getUserSubscribesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        Long id = userSubscribe.getId();

        defaultUserSubscribeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserSubscribeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserSubscribeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserSubscribesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where fromDate equals to
        defaultUserSubscribeFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllUserSubscribesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where fromDate in
        defaultUserSubscribeFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllUserSubscribesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where fromDate is not null
        defaultUserSubscribeFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscribesByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where toDate equals to
        defaultUserSubscribeFiltering("toDate.equals=" + DEFAULT_TO_DATE, "toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllUserSubscribesByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where toDate in
        defaultUserSubscribeFiltering("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE, "toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllUserSubscribesByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where toDate is not null
        defaultUserSubscribeFiltering("toDate.specified=true", "toDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscribesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where isActive equals to
        defaultUserSubscribeFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUserSubscribesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where isActive in
        defaultUserSubscribeFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUserSubscribesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where isActive is not null
        defaultUserSubscribeFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscribesBySubscribedUserEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where subscribedUserEncId equals to
        defaultUserSubscribeFiltering(
            "subscribedUserEncId.equals=" + DEFAULT_SUBSCRIBED_USER_ENC_ID,
            "subscribedUserEncId.equals=" + UPDATED_SUBSCRIBED_USER_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllUserSubscribesBySubscribedUserEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where subscribedUserEncId in
        defaultUserSubscribeFiltering(
            "subscribedUserEncId.in=" + DEFAULT_SUBSCRIBED_USER_ENC_ID + "," + UPDATED_SUBSCRIBED_USER_ENC_ID,
            "subscribedUserEncId.in=" + UPDATED_SUBSCRIBED_USER_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllUserSubscribesBySubscribedUserEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        // Get all the userSubscribeList where subscribedUserEncId is not null
        defaultUserSubscribeFiltering("subscribedUserEncId.specified=true", "subscribedUserEncId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscribesBySubscribeTypeIsEqualToSomething() throws Exception {
        SubscribeType subscribeType;
        if (TestUtil.findAll(em, SubscribeType.class).isEmpty()) {
            userSubscribeRepository.saveAndFlush(userSubscribe);
            subscribeType = SubscribeTypeResourceIT.createEntity();
        } else {
            subscribeType = TestUtil.findAll(em, SubscribeType.class).get(0);
        }
        em.persist(subscribeType);
        em.flush();
        userSubscribe.setSubscribeType(subscribeType);
        userSubscribeRepository.saveAndFlush(userSubscribe);
        Long subscribeTypeId = subscribeType.getId();
        // Get all the userSubscribeList where subscribeType equals to subscribeTypeId
        defaultUserSubscribeShouldBeFound("subscribeTypeId.equals=" + subscribeTypeId);

        // Get all the userSubscribeList where subscribeType equals to (subscribeTypeId + 1)
        defaultUserSubscribeShouldNotBeFound("subscribeTypeId.equals=" + (subscribeTypeId + 1));
    }

    private void defaultUserSubscribeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserSubscribeShouldBeFound(shouldBeFound);
        defaultUserSubscribeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserSubscribeShouldBeFound(String filter) throws Exception {
        restUserSubscribeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscribe.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].subscribedUserEncId").value(hasItem(DEFAULT_SUBSCRIBED_USER_ENC_ID.toString())));

        // Check, that the count call also returns 1
        restUserSubscribeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserSubscribeShouldNotBeFound(String filter) throws Exception {
        restUserSubscribeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserSubscribeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserSubscribe() throws Exception {
        // Get the userSubscribe
        restUserSubscribeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSubscribe() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSubscribeSearchRepository.save(userSubscribe);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());

        // Update the userSubscribe
        UserSubscribe updatedUserSubscribe = userSubscribeRepository.findById(userSubscribe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserSubscribe are not directly saved in db
        em.detach(updatedUserSubscribe);
        updatedUserSubscribe
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .subscribedUserEncId(UPDATED_SUBSCRIBED_USER_ENC_ID);
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(updatedUserSubscribe);

        restUserSubscribeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSubscribeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSubscribeDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscribe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserSubscribeToMatchAllProperties(updatedUserSubscribe);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserSubscribe> userSubscribeSearchList = Streamable.of(userSubscribeSearchRepository.findAll()).toList();
                UserSubscribe testUserSubscribeSearch = userSubscribeSearchList.get(searchDatabaseSizeAfter - 1);

                assertUserSubscribeAllPropertiesEquals(testUserSubscribeSearch, updatedUserSubscribe);
            });
    }

    @Test
    @Transactional
    void putNonExistingUserSubscribe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        userSubscribe.setId(longCount.incrementAndGet());

        // Create the UserSubscribe
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(userSubscribe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSubscribeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSubscribeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSubscribeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscribe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSubscribe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        userSubscribe.setId(longCount.incrementAndGet());

        // Create the UserSubscribe
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(userSubscribe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscribeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSubscribeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscribe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSubscribe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        userSubscribe.setId(longCount.incrementAndGet());

        // Create the UserSubscribe
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(userSubscribe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscribeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSubscribeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSubscribe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUserSubscribeWithPatch() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSubscribe using partial update
        UserSubscribe partialUpdatedUserSubscribe = new UserSubscribe();
        partialUpdatedUserSubscribe.setId(userSubscribe.getId());

        partialUpdatedUserSubscribe
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .subscribedUserEncId(UPDATED_SUBSCRIBED_USER_ENC_ID);

        restUserSubscribeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSubscribe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSubscribe))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscribe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSubscribeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserSubscribe, userSubscribe),
            getPersistedUserSubscribe(userSubscribe)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserSubscribeWithPatch() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSubscribe using partial update
        UserSubscribe partialUpdatedUserSubscribe = new UserSubscribe();
        partialUpdatedUserSubscribe.setId(userSubscribe.getId());

        partialUpdatedUserSubscribe
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .subscribedUserEncId(UPDATED_SUBSCRIBED_USER_ENC_ID);

        restUserSubscribeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSubscribe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSubscribe))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscribe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSubscribeUpdatableFieldsEquals(partialUpdatedUserSubscribe, getPersistedUserSubscribe(partialUpdatedUserSubscribe));
    }

    @Test
    @Transactional
    void patchNonExistingUserSubscribe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        userSubscribe.setId(longCount.incrementAndGet());

        // Create the UserSubscribe
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(userSubscribe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSubscribeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSubscribeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSubscribeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscribe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSubscribe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        userSubscribe.setId(longCount.incrementAndGet());

        // Create the UserSubscribe
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(userSubscribe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscribeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSubscribeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscribe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSubscribe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        userSubscribe.setId(longCount.incrementAndGet());

        // Create the UserSubscribe
        UserSubscribeDTO userSubscribeDTO = userSubscribeMapper.toDto(userSubscribe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscribeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userSubscribeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSubscribe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUserSubscribe() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);
        userSubscribeRepository.save(userSubscribe);
        userSubscribeSearchRepository.save(userSubscribe);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userSubscribe
        restUserSubscribeMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSubscribe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSubscribeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUserSubscribe() throws Exception {
        // Initialize the database
        insertedUserSubscribe = userSubscribeRepository.saveAndFlush(userSubscribe);
        userSubscribeSearchRepository.save(userSubscribe);

        // Search the userSubscribe
        restUserSubscribeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + userSubscribe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscribe.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].subscribedUserEncId").value(hasItem(DEFAULT_SUBSCRIBED_USER_ENC_ID.toString())));
    }

    protected long getRepositoryCount() {
        return userSubscribeRepository.count();
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

    protected UserSubscribe getPersistedUserSubscribe(UserSubscribe userSubscribe) {
        return userSubscribeRepository.findById(userSubscribe.getId()).orElseThrow();
    }

    protected void assertPersistedUserSubscribeToMatchAllProperties(UserSubscribe expectedUserSubscribe) {
        assertUserSubscribeAllPropertiesEquals(expectedUserSubscribe, getPersistedUserSubscribe(expectedUserSubscribe));
    }

    protected void assertPersistedUserSubscribeToMatchUpdatableProperties(UserSubscribe expectedUserSubscribe) {
        assertUserSubscribeAllUpdatablePropertiesEquals(expectedUserSubscribe, getPersistedUserSubscribe(expectedUserSubscribe));
    }
}
